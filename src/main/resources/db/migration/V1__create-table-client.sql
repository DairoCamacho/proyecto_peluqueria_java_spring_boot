CREATE TABLE `client` (
  `id` INT(11) NOT NULL AUTO_INCREMENT, 
  `birthday` DATE NOT NULL, 
  `last_name` VARCHAR(50) NOT NULL, 
  `name` VARCHAR(50) NOT NULL, 
  `phone` VARCHAR(15) NOT NULL, 
  `type` VARCHAR(50) NOT NULL, 
  PRIMARY KEY (`id`));

INSERT INTO client (id, birthday, last_name, name, phone, type) VALUES ('2024-01-01', 'admin','user','3001111111','admin');
INSERT INTO client (id, type) VALUES (5, 'regular');
INSERT INTO client (id, type) VALUES (6, 'new');

INSERT INTO user (name, last_name, email, password, phone, birthday, status)
VALUES (  'user@admin.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO',  , 1);