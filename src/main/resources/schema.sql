PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS Usuarios;
DROP TABLE IF EXISTS Envios;
DROP TABLE IF EXISTS Tarifas;
DROP TABLE IF EXISTS Estados_envio;

CREATE TABLE Usuarios (
    id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    telefono VARCHAR(9),
    direccion TEXT NOT NULL,
    estado TEXT CHECK (estado IN ('activo','borrado')) NOT NULL DEFAULT 'activo'
);

CREATE TABLE Envios (
	id_envio INTEGER PRIMARY KEY AUTOINCREMENT,
	id_usuario INTEGER NOT NULL,
	origen TEXT NOT NULL,
	destino TEXT NOT NULL,
	peso_inicial REAL NOT NULL,
	estado TEXT NOT NULL,
	num_intentos_entrega INTEGER NOT NULL,
	fecha TEXT,
	ubicacion_actual TEXT NOT NULL,
	FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Tarifas (
	id_tarifa INTEGER PRIMARY KEY AUTOINCREMENT,
	peso_min DECIMAL(8,2) NOT NULL,
	peso_max DECIMAL(8,2) NOT NULL,
	precio REAL NOT NULL
);

CREATE TABLE Estados_envio (
	id_estado INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_estado TEXT UNIQUE NOT NULL
);