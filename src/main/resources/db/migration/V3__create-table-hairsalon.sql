CREATE TABLE `hair_salon` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`phone` VARCHAR(45) NOT NULL,
`address` VARCHAR(45) NOT NULL,
`neighborhood` VARCHAR(45) NOT NULL,
`city` VARCHAR(45) NOT NULL,
`country` VARCHAR(45) NOT NULL,
`status` TINYINT(1),
PRIMARY KEY (`id`)
);

INSERT INTO hair_salon (name, phone, address, neighborhood, city, country, status)
VALUES ('Alejandra', 'tel111', 'cll 1 cra 1 #11-11', 'Centro', 'city', 'country', 1);

INSERT INTO hair_salon (name, phone, address, neighborhood, city, country, status)
VALUES ('Alejandra Sucursal', 'tel222', 'cll 2 cra 2 #22-22', 'Centro', 'city', 'country', 1);