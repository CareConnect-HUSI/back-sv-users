
CREATE TABLE IF NOT EXISTS ROLES ( 
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS patients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    identification_number VARCHAR(50) UNIQUE NOT NULL,
    identification_type VARCHAR(10) NOT NULL,
    nombre_familiar VARCHAR(255),
    parentesco_familiar VARCHAR(100),
    telefono_familiar VARCHAR(20),
    barrio VARCHAR(100),
    conjunto VARCHAR(100), -- nullable
    latitud DOUBLE,
    longitud DOUBLE
);


CREATE TABLE IF NOT EXISTS tratamiento (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    paciente_id BIGINT NOT NULL,
    medicamento VARCHAR(255) NOT NULL,
    dosis INT NOT NULL,
    frecuencia INT NOT NULL,
    dias_tratamiento INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    duracion INT NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS procedimientos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    paciente_id BIGINT NOT NULL,
    procedimiento VARCHAR(255) NOT NULL,
    frecuencia INT NOT NULL,
    dias_tratamiento INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    duracion INT NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES patients(id) ON DELETE CASCADE
);

