create table user(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(300) NOT NULL,
  `phone` VARCHAR(10) NOT NULL,
  `birthday` DATE NULL,
  `status` TINYINT(1),
  PRIMARY KEY (`id`)
);
INSERT INTO user (name, last_name, email, password, phone, birthday, status)
VALUES ('user', 'admin', 'user@admin.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO', '3001111111', '2024-01-01', 1);

INSERT INTO user (name, last_name, email, password, phone, birthday, status)
VALUES ('Alejandra', 'Arteaga', 'alejandra@gmail.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO', '3002222222', '2024-01-02', 1);

INSERT INTO user (name, last_name, email, password, phone, birthday, status)
VALUES ('Ana', 'Acosta', 'ana@gmail.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO', '3003333333', '2024-01-03', 1);

INSERT INTO user (name, last_name, email, password, phone, birthday, status)
VALUES ('Pedro', 'Perez', 'pedro@gmail.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO', '3004444444', '2024-01-04', 1);

INSERT INTO user (name, last_name, email, password, phone, birthday, status)
VALUES ('Armando', 'Cazas', 'armado@gmail.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO', '3005555555', '2024-01-05', 1);

INSERT INTO user (name, last_name, email, password, phone, birthday, status)
VALUES ('Fulanito', 'Detal', 'fulanito@gmail.com', '$2a$10$4DnFl0oGGyfPSdChwfHKMuX1lCS6ZqoeuLHW.DzMsDSSJyQLjxZSO', '3006666666', '2024-01-06', 1);