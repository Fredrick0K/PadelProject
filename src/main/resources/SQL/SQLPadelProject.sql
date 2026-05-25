
-- ¡¡IMPORTANTE!! Descomente las clausulas CREATE si solo el servidor no ha creado las tablas.


-- DROP TABLE IF EXISTS Reserva;
-- DROP TABLE IF EXISTS Pista;
-- DROP TABLE IF EXISTS Usuario;

-- -- =========================================================================
-- -- 1. CREACIÓN DE LA TABLA: Usuario
-- -- =========================================================================
-- CREATE TABLE Usuario (
--     id SERIAL,
--     nombre VARCHAR(255) NOT NULL,
--     email VARCHAR(255) NOT NULL,
--     telefono VARCHAR(50),
--     contraseña VARCHAR(255) NOT NULL, 
--     rol VARCHAR(50) NOT NULL,
--     activo BOOLEAN NOT NULL DEFAULT TRUE,
--     fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
--     CONSTRAINT pk_usuario PRIMARY KEY (id),
--     CONSTRAINT uq_usuario_email UNIQUE (email)
-- );

-- -- =========================================================================
-- -- 2. CREACIÓN DE LA TABLA: Pista
-- -- =========================================================================
-- CREATE TABLE Pista (
--     id SERIAL,
--     numero_pista INTEGER NOT NULL,
--     nombre VARCHAR(255) NOT NULL,
--     estado VARCHAR(50) NOT NULL,
--     precio_hora DECIMAL(10, 2) NOT NULL,
--     fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
--     CONSTRAINT pk_pista PRIMARY KEY (id)
-- );

-- -- =========================================================================
-- -- 3. CREACIÓN DE LA TABLA: Reserva
-- -- =========================================================================
-- CREATE TABLE Reserva (
--     id SERIAL,
--     hora_inicio TIMESTAMP NOT NULL,
--     hora_fin TIMESTAMP NOT NULL,
--     estado VARCHAR(50) NOT NULL,
--     precio_total DECIMAL(10, 2) NOT NULL,
--     fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     id_usuario INTEGER NOT NULL,
--     id_pista INTEGER NOT NULL,
    
--     -- Definición explícita de Restricciones (Constraints)
--     CONSTRAINT pk_reserva PRIMARY KEY (id),
    
--     CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario) 
--         REFERENCES Usuario(id) 
--         ON DELETE CASCADE,
        
--     CONSTRAINT fk_reserva_pista FOREIGN KEY (id_pista) 
--         REFERENCES Pista(id) 
--         ON DELETE RESTRICT
-- );

-- =========================================================================
-- 1. INSERTAR DATOS EN LA TABLA: Usuario
-- =========================================================================
-- Insertamos 2 usuarios.
INSERT INTO Usuario (nombre, email, telefono, contraseña, rol, activo)
VALUES 
('Mohammad', 'mohammad@email.com', '+34600111222', 'clave123', 'CLIENTE', TRUE),
('Pilar', 'pilar@email.com', '+34600333444', 'admin456', 'ADMIN', TRUE);


-- =========================================================================
-- 2. INSERTAR DATOS EN LA TABLA: Pista
-- =========================================================================
-- Insertamos 2 pistas de ejemplo.
INSERT INTO Pista (numero_pista, nombre, estado, precio_hora)
VALUES 
(1, 'Pista Central Cristal', 'DISPONIBLE', 15.50),
(2, 'Pista 2 Muro', 'DISPONIBLE', 12.00);


-- =========================================================================
-- 3. INSERTAR DATOS EN LA TABLA: Reserva
-- =========================================================================
-- Como es una base de datos nueva, sabemos que los usuarios creados tienen ID 1 y 2,
-- y las pistas tienen ID 1 y 2.
-- Las fechas se introducen en formato 'YYYY-MM-DD HH:MM:SS'.
INSERT INTO Reserva (hora_inicio, hora_fin, estado, precio_total, id_usuario, id_pista)
VALUES 
-- Reserva 1: El usuario 1 reserva la pista 1 por una hora 
('2026-06-01 10:00:00', '2026-06-01 11:00:00', 'CONFIRMADA', 23.25, 1, 1),

-- Reserva 2: El usuario 2 reserva la pista 2 por dos horas
('2026-06-02 18:00:00', '2026-06-02 20:00:00', 'PENDIENTE', 24.00, 2, 2);

