CREATE TABLE `employee` (
  `id` INT(11) NOT NULL,
  `position` VARCHAR(45) NOT NULL,
  `hire_date` DATE NOT NULL,
  `termination_date` DATE NULL DEFAULT NULL,
  `hair_salon_id` INT(11) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`hair_salon_id`)
    REFERENCES `siac`.`hair_salon` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_employee_user1`
    FOREIGN KEY (`id`)
    REFERENCES `siac`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
  );
INSERT INTO employee (id, position, hire_date, hair_salon_id)
VALUES (2, 'Dueña', '2020-01-01', 1);

INSERT INTO employee (id, position, hire_date, hair_salon_id)
VALUES (3, 'Peluquera', '2020-02-02', 1);