DROP DATABASE IF EXISTS TripManagement;
CREATE DATABASE TripManagement CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE TripManagement;

CREATE TABLE professors (
    professor_Id INT AUTO_INCREMENT PRIMARY KEY,
	professor_name VARCHAR(50) NOT NULL,
    professor_surname VARCHAR(50) NOT NULL,
    professor_birthDate DATE NOT NULL,
    professor_phone VARCHAR(20) NOT NULL
);

CREATE TABLE `Groups`(
    group_Id INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(50) NOT NULL,
    educationalStage VARCHAR(50) NOT NULL,
    numberOfStudents INT NOT NULL CHECK (numberOfStudents > 0)
);

CREATE TABLE trips (
    trips_id INT AUTO_INCREMENT PRIMARY KEY,
    group_Id INT NOT NULL,
    trip_destination VARCHAR(100) NOT NULL,
    trip_duration INT NOT NULL,
    trip_date DATE NOT NULL,
    trip_cost DECIMAL(8,2) NOT NULL,
    trip_status VARCHAR(20) DEFAULT NULL,
    FOREIGN KEY (group_Id) REFERENCES `Groups`(group_Id)
);

CREATE TABLE professorsgoing (
    trip_id INT NOT NULL,
    professor_id INT NOT NULL,
    PRIMARY KEY (trip_id, professor_id),
    FOREIGN KEY (trip_id) REFERENCES trips(trips_id) ON DELETE CASCADE,
    FOREIGN KEY (professor_id) REFERENCES professors(professor_Id) ON DELETE CASCADE
);


INSERT INTO professors (professor_name, professor_surname, professor_birthDate, professor_phone) VALUES
('María', 'García López', '1980-03-15', '612345678'),
('Carlos', 'Rodríguez Martín', '1975-11-22', '623456789'),
('Ana', 'Fernández Silva', '1982-07-08', '634567890'),
('David', 'Martínez Cruz', '1978-09-30', '645678901'),
('Elena', 'Sánchez Ruiz', '1985-12-14', '656789012');

INSERT INTO `Groups` (group_name, educationalStage, numberOfStudents) VALUES
('1ºA Primaria', 'Primaria', 25),
('2ºB Primaria', 'Primaria', 28),
('4ºA ESO', 'ESO', 30),
('1º Bachillerato', 'Bachillerato', 28),
('1ºSMR', 'FP', 24),
('2ºDAM', 'FP', 20);

INSERT INTO trips (group_Id, trip_destination, trip_duration, trip_date, trip_cost, trip_status) VALUES
-- Finalizadas
(1, 'Zoo Municipal', 4, '2024-01-15', 12.50, 'Finalizada'),
(2, 'Granja Escuela', 6, '2024-02-10', 22.00, 'Finalizada'),
(3, 'Museo de Ciencias', 5, '2024-06-20', 9.50, 'Finalizada'),
-- Pendientes
(4, 'Visita Universidad', 4, '2024-07-10', 6.00, 'Pendiente'),
(5, 'Taller Programación', 6, '2024-09-15', 28.00, 'Pendiente'),
(6, 'Teatro Infantil', 3, '2024-08-20', 8.00, 'Pendiente');

INSERT INTO professorsgoing (trip_id, professor_id) VALUES

(1, 1), (1, 2),
(2, 3), (2, 4),
(3, 1), (3, 3), (3, 5),
(4, 2), (4, 4),
(5, 1), (5, 5),
(6, 3), (6, 4),
(7, 2), (7, 4), (7, 5),
(8, 1), (8, 3);