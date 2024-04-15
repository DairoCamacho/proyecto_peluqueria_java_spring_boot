create table `service` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` INT(11) NOT NULL,
  `duration` TIME NOT NULL,
  `hair_salon_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_service_hair_salon1_idx` (`hair_salon_id` ASC),
  FOREIGN KEY (`hair_salon_id`)
    REFERENCES `siac`.`hair_salon` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
INSERT INTO service (name, price, duration, hair_salon_id)
VALUES ('Corte de pelo', 15000, '00:30:00', 1);

INSERT INTO service (name, price, duration, hair_salon_id)
VALUES ('Manicura', 15000, '00:30:00', 1);

INSERT INTO service (name, price, duration, hair_salon_id)
VALUES ('Pedicura', 25000, '01:00:00', 1);

INSERT INTO service (name, price, duration, hair_salon_id)
VALUES ('Maquillaje', 35000, '01:30:00', 1);

INSERT INTO service (name, price, duration, hair_salon_id)
VALUES ('Cepillado de pelo', 25000, '02:00:00', 1);

INSERT INTO service (name, price, duration, hair_salon_id)
VALUES ('Tintura de pelo', 75000, '03:00:00', 1);
