create table user(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(300) NOT NULL,
  `phone` VARCHAR(10) NOT NULL,
  `birthday` DATE NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO user (name, last_name, email, password, phone, birthday)
VALUES ('user', 'admin', 'user@admin.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO', '3001112233', '2024-01-01');