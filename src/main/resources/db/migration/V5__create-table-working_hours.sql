CREATE TABLE `working_hours` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`start_date` TIMESTAMP NOT NULL,
`end_date` TIMESTAMP NULL,
`employee_id` INT(11) NOT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`employee_id`) REFERENCES `siac`.`employee` (`id`)
);

INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (1, '2024-06-01 09:00:00', '2024-06-01 18:00:00', 2);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (2, '2024-06-02 09:00:00', '2024-06-02 18:00:00', 2);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (3, '2024-06-03 09:00:00', '2024-06-03 18:00:00', 2);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (4, '2024-06-04 09:00:00', '2024-06-04 18:00:00', 2);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (5, '2024-06-05 09:00:00', '2024-06-05 18:00:00', 2);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (6, '2024-06-06 09:00:00', '2024-06-01 18:00:00', 2);

INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (7, '2024-06-01 14:00:00', '2024-06-01 20:00:00', 3);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (8, '2024-06-02 14:00:00', '2024-06-02 20:00:00', 3);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (9, '2024-06-03 14:00:00', '2024-06-03 20:00:00', 3);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (10, '2024-06-04 14:00:00', '2024-06-04 20:00:00', 3);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (11, '2024-06-05 14:00:00', '2024-06-05 20:00:00', 3);
INSERT INTO `working_hours` (`id`, `start_date`, `end_date`, `employee_id`) 
VALUES (12, '2024-06-06 14:00:00', '2024-06-01 20:00:00', 3);