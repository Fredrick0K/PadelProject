
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
--     contrasena VARCHAR(255) NOT NULL, 
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


