create table `client` (
  `id` INT(11) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_client_user1`
    FOREIGN KEY (`id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
INSERT INTO client (id, type) VALUES (4, 'premium');
INSERT INTO client (id, type) VALUES (5, 'regular');
INSERT INTO client (id, type) VALUES (6, 'new');