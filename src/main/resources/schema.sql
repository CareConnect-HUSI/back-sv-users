
--TABLA TIPO IDENTIFICACIÓN
CREATE TABLE IF NOT EXISTS tipo_identificacion(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--TABLA TURNO
CREATE TABLE IF NOT EXISTS turno(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--TABLA ROL
CREATE TABLE IF NOT EXISTS rol(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--TABLA TIPO DE ACTIVIDAD
CREATE TABLE IF NOT EXISTS tipo_actividad(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--TABLA ACTIVIDAD
CREATE TABLE IF NOT EXISTS actividad(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    tipo_actividad_id BIGINT NOT NULL,
    descripcion VARCHAR(255),
    cantidad INT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_registra VARCHAR(255),
    CONSTRAINT fk_actividad_tipo_actividad FOREIGN KEY (tipo_actividad_id) REFERENCES tipo_actividad(id)
);

--TABLA PACIENTE
CREATE TABLE IF NOT EXISTS paciente(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(255),
    tipo_identificacion_id BIGINT NOT NULL,
    numero_identificacion VARCHAR(255) NOT NULL,
    nombre_acudiente VARCHAR(255),
    parentezco_acudiente VARCHAR(255),
    telefono_acudiente VARCHAR(255),
    barrio VARCHAR(255),
    conjunto VARCHAR(255) DEFAULT NULL,
    localidad VARCHAR(255),
    latitud DECIMAL(9, 6),
    longitud DECIMAL(9, 6),
    CONSTRAINT fk_paciente_tipo_identificacion FOREIGN KEY (tipo_identificacion_id) REFERENCES tipo_identificacion(id)
);

-- TABLA ENFERMERA 
CREATE TABLE IF NOT EXISTS enfermera(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_identificacion_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    turno_id BIGINT NOT NULL,
    numero_identificacion VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(255),
    barrio VARCHAR(255),
    conjunto VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255),
    password VARCHAR(255),
    latitud DECIMAL(9, 6),
    longitud DECIMAL(9, 6),
    CONSTRAINT fk_enfermera_tipo_identificacion FOREIGN KEY (tipo_identificacion_id) REFERENCES tipo_identificacion(id),
    CONSTRAINT fk_enfermera_rol FOREIGN KEY (rol_id) REFERENCES rol(id),
    CONSTRAINT fk_enfermera_turno FOREIGN KEY (turno_id) REFERENCES turno(id)
);

--TABLA ACTIVIDAD PACIENTE VISITA
CREATE TABLE IF NOT EXISTS actividad_paciente_visita(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    actividad_id BIGINT NOT NULL,
    dosis INT,
    frecuencia INT,
    dias_tratamiento INT,
    fecha_inicio DATE,
    fecha_fin DATE,
    hora TIME,
    duracion_visita TIME,
    CONSTRAINT fk_actividad_paciente_visita_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    CONSTRAINT fk_actividad_paciente_visita_actividad FOREIGN KEY (actividad_id) REFERENCES actividad(id)
);

--TABLA VISITA
CREATE TABLE IF NOT EXISTS visita(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    enfermera_id BIGINT NOT NULL,
    actividad_paciente_visita_id BIGINT NOT NULL,
    fecha_visita DATE,
    hora_inicio_ejecutada TIME,
    hora_fin_ejecutada TIME,
    estado VARCHAR(255),
    hora_inicio_calculada TIME,
    hora_fin_calculada TIME,
    CONSTRAINT fk_visita_enfermera FOREIGN KEY (enfermera_id) REFERENCES enfermera(id),
    CONSTRAINT fk_visita_actividad_paciente_visita FOREIGN KEY (actividad_paciente_visita_id) REFERENCES actividad_paciente_visita(id)
);

-- TABLA INSTALACION INSUMOS PACIENTE
CREATE TABLE IF NOT EXISTS instalacion_insumos_paciente(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    actividad_id BIGINT NOT NULL,
    fecha_instalacion DATE,
    cantidad_disponible INT,
    CONSTRAINT fk_instalacion_insumos_paciente_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    CONSTRAINT fk_instalacion_insumos_paciente_actividad FOREIGN KEY (actividad_id) REFERENCES actividad(id)
);

--TABLA INSUMOS CONSUMIDOS
CREATE TABLE IF NOT EXISTS insumos_consumidos(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    instalacion_insumos_paciente_id BIGINT NOT NULL,
    visita_id BIGINT NOT NULL,
    cantidad_consumida INT,
    CONSTRAINT fk_insumos_consumidos_instalacion_insumos_paciente FOREIGN KEY (instalacion_insumos_paciente_id) REFERENCES instalacion_insumos_paciente(id),
    CONSTRAINT fk_insumos_consumidos_visita FOREIGN KEY (visita_id) REFERENCES visita(id)
);