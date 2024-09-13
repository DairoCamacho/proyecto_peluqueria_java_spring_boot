package com.unaux.dairo.api.controller;

import java.net.URI;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.unaux.dairo.api.domain.product.Product;
import com.unaux.dairo.api.domain.product.ProductCreateDto;
import com.unaux.dairo.api.domain.product.ProductFindDto;
import com.unaux.dairo.api.domain.product.ProductResponseDto;
import com.unaux.dairo.api.domain.product.ProductUpdateDto;
import com.unaux.dairo.api.infra.errors.ResourceNotFoundException;
import com.unaux.dairo.api.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/product")
@Tag(name = "Product", description = "The Product API")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product based on the given data", tags = {
            "Product" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product data", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCreateDto.class), examples = @ExampleObject(name = "example", value = "{\"name\": \"Hair dye\", \"price\": 15000, \"duration\": \"01:00:00\", \"hairSalonId\": 1}"))))
    @ApiResponse(responseCode = "201", description = "Product created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class), examples = @ExampleObject(name = "example", value = "{\"id\": 1, \"name\": \"Hair dye\", \"price\": 15000, \"duration\": \"01:00:00\", \"hairSalonId\": 1, \"status\": true}")))
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "error", value = "{\"message\": \"<Resource> not found with ID: <id>\"}")))
    public ResponseEntity<?> createProduct(UriComponentsBuilder uriComponentsBuilder,
            @RequestBody @Valid ProductCreateDto productCreateDto) {
        // Extraer los datos
        String name = productCreateDto.name();
        int price = productCreateDto.price();
        LocalTime duration = productCreateDto.duration();
        int hairSalonId = productCreateDto.hairSalonId();

        try {
            Product product = productService.save(name, price, duration, hairSalonId);
            // Creamos un Dto para retornar el objeto creado al frontend
            ProductResponseDto response = mapProductoToResponseDto(product);
            // Aquí crearemos una url que corresponde al objeto que se creó en la base de datos.
            URI url = uriComponentsBuilder
                    .path("api/v1/product/{id}")
                    .buildAndExpand(product.getId())
                    .toUri();

            return ResponseEntity.created(url).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header(e.getClass().getSimpleName(), e.getMessage())
                    .body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "List all products", description = "Get a paginated list of all products", tags = {
            "Product" })
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class), examples = @ExampleObject(name = "example", value = "{\"content\": [{\"id\": 1, \"name\": \"Hair dye\", \"price\": 100, \"duration\": \"01:00:00\", \"hairSalonId\": 1, \"status\": true}], \"pageable\": \"INSTANCE\", \"totalPages\": 1, \"totalElements\": 1}")))
    public ResponseEntity<Page<ProductFindDto>> listAllProduct(Pageable pagination) {
        Page<Product> listProducts = productService.findAll(pagination);
        return ResponseEntity.ok(listProducts.map(ProductFindDto::new));
    }

    @GetMapping("/enabled")
    @Operation(summary = "List enabled products", description = "Get a paginated list of all enabled products", tags = {
            "Product" })
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class), examples = @ExampleObject(name = "example", value = "{\"content\": [{\"id\": 1, \"name\": \"Hair dye\", \"price\": 100, \"duration\": \"01:00:00\", \"hairSalonId\": 1, \"status\": true}], \"pageable\": \"INSTANCE\", \"totalPages\": 1, \"totalElements\": 1}")))
    public ResponseEntity<Page<ProductFindDto>> listEnabledStatusProduct(Pageable pagination) {
        Page<Product> listProducts = productService.findEnabled(pagination);
        return ResponseEntity.ok(listProducts.map(ProductFindDto::new));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find product by ID", description = "Returns a single product", tags = { "Product" })
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class), examples = @ExampleObject(name = "example", value = "{\"id\": 1, \"name\": \"Hair dye\", \"price\": 100, \"duration\": \"01:00:00\", \"hairSalonId\": 1, \"status\": true}")))
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "error", value = "{\"code\": \"RESOURCE_NOT_FOUND\", \"message\": \"The requested resource was not found\", \"details\": \"No record with the ID 1 was found in the database.\"}")))
    public ResponseEntity<?> findProduct(@PathVariable int id) {
        // *** No hay validaciones menores para realizar
        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isEmpty()) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("code", "RESOURCE_NOT_FOUND");
            errorDetails.put("message", "The requested resource was not found");
            errorDetails.put("details", "No record with the ID " + id + " was found in the database.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

            // Enviar un código de estado HTTP 404 Not Found con un mensaje en el cuerpo
            //! return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found");
        }
        // Creamos un DTO para retornar el objeto al frontend
        Product product = productOptional.get();
        ProductResponseDto response = mapProductoToResponseDto(product);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Update an existing product", description = "Updates a product based on the given data", tags = {
            "Product" })
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class), examples = @ExampleObject(name = "example", value = "{\"id\": 1, \"name\": \"Hair dye\", \"price\": 100, \"duration\": \"01:00:00\", \"hairSalonId\": 1, \"status\": true}")))
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "error", value = "{\"message\": \"<Resource> not found with ID: <id>\"}")))
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductUpdateDto productUpdateDto) {
        // Extraer los Datos
        int id = productUpdateDto.id();
        String name = productUpdateDto.name();
        int price = productUpdateDto.price();
        LocalTime duration = productUpdateDto.duration();
        int hairSalonId = productUpdateDto.hairSalonId();
        Boolean status = productUpdateDto.status();

        try {
            Product product = productService.update(id, name, price, duration, hairSalonId, status);
            // creamos el DTO para la respuesta
            ProductResponseDto response = mapProductoToResponseDto(product);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            String errorMessage = "Resource not found with ID: %d".formatted(productUpdateDto.id());
            return ((BodyBuilder) ResponseEntity.notFound()).body(errorMessage);
        } catch (ResourceNotFoundException e) {
            return ((BodyBuilder) ResponseEntity.notFound()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Delete a product", description = "Deletes a product")
    @ApiResponse(responseCode = "204", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "error", value = "{\"message\": \"Resource not found with ID: <id>\"}")))
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            productService.delete(id);
            // Retornamos una respuesta vacía
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            String errorMessage = "Resource not found with ID: %d".formatted(id);
            return ((BodyBuilder) ResponseEntity.notFound()).body(errorMessage);
        }
    }

    private ProductResponseDto mapProductoToResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDuration(),
                product.getHairSalon().getId(),
                product.isStatus());
    }
}
