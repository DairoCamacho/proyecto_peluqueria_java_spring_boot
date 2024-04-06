ALTER TABLE user ADD status TINYINT(1);
UPDATE user SET status = 1; -- actualiza todos los registros anteriores