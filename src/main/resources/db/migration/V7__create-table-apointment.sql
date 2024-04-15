create table `appointment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `time` TIME NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `notes` VARCHAR(45) NULL,
  `service_id` INT(11) NOT NULL,
  `employee_id` INT(11) NOT NULL,
  `client_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`service_id`)
    REFERENCES `siac`.`service` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (`employee_id`)
    REFERENCES `siac`.`employee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (`client_id`)
    REFERENCES `siac`.`client` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
-- cita con intermedio de 30 minutos
INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-01-01', '09:00:00', 'cumplida', 'Nota adicional', 1, 2, 4);

INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-01-01', '10:00:00', 'anulada', 'Nota adicional', 2, 2, 5);

-- cita con intermedio de 1 hora
INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-01-01', '14:00:00', 'cumplida', 'Nota adicional', 1, 3, 5);

INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-01-01', '15:30:00', 'anulada', 'Nota adicional', 1, 3, 5);

-- citas futuras con intermedio de 2 horas
INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-12-12', '09:00:00', 'pendiente', 'Nota adicional', 1, 2, 4);

INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-12-12', '11:30:00', 'pendiente', 'Nota adicional', 2, 2, 4);

-- citas futuras con intermedio de 3 hora
INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-12-12', '14:00:00', 'pendiente', 'Nota adicional', 1, 3, 5);

INSERT INTO appointment (date, time, status, notes, service_id, employee_id, client_id)
VALUES ('2024-12-12', '17:30:00', 'pendiente', 'Nota adicional', 1, 3, 5);