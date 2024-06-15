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

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/product")
// @PreAuthorize("hasRole('ADMIN')")
// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ProductController {

  public final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
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
          .path("api/product/{id}")
          .buildAndExpand(product.getId())
          .toUri();

      return ResponseEntity.created(url).body(response);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .header(e.getClass().getSimpleName(), e.getMessage())
          .body(e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<Page<ProductFindDto>> listAllService(Pageable pagination) {
    Page<Product> listProducts = productService.findAll(pagination);
    return ResponseEntity.ok(listProducts.map(ProductFindDto::new));
  }

  @GetMapping("/{id}")
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
      return ResponseEntity.badRequest().body(errorMessage);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<?> deleteService(@PathVariable int id) {
    try {
      productService.delete(id);
      // Retornamos una respuesta vacía
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      String errorMessage = "Resource not found with ID: %d".formatted(id);
      return ResponseEntity.badRequest().body(errorMessage);
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
