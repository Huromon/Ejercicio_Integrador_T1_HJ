CREATE DATABASE IF NOT EXISTS Candidaturas;
USE Candidaturas;

CREATE TABLE IF NOT EXISTS EMPRESA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    sector VARCHAR(80),
    web VARCHAR(255),
    fechaRegistro DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS OFERTA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    estado VARCHAR(20) NOT NULL,
    fechaPostulacion DATE NOT NULL,
    enlace VARCHAR(500),
    salario DECIMAL(10,2),
    notas TEXT,
    FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id)
);

CREATE TABLE IF NOT EXISTS ENTREVISTA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_oferta INT NOT NULL,
    fecha DATE NOT NULL,
    tipo VARCHAR(50),
    notas TEXT,
    resultado VARCHAR(50),
    FOREIGN KEY (id_oferta) REFERENCES OFERTA(id)
);
