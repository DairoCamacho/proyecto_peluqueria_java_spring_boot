CREATE TABLE `employee` (
`id` INT(11) NOT NULL,
`position` VARCHAR(45) NOT NULL,
`hire_date` DATE NOT NULL,
`termination_date` DATE NULL DEFAULT NULL,
`hair_salon_id` INT(11) NOT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`hair_salon_id`) REFERENCES `hair_salon` (`id`),
FOREIGN KEY (`id`) REFERENCES `user` (`id`)
);

INSERT INTO employee (id, position, hire_date, hair_salon_id)
VALUES (2, 'Dueña', '2020-01-01', 1);

INSERT INTO employee (id, position, hire_date, hair_salon_id)
VALUES (3, 'Peluquera', '2020-02-02', 1);