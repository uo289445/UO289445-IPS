PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS Usuarios;
DROP TABLE IF EXISTS Envios;
DROP TABLE IF EXISTS Tarifas;
DROP TABLE IF EXISTS Estados_envio;
DROP TABLE IF EXISTS Transportistas;
DROP TABLE IF EXISTS Seguimiento;

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
	id_transportista INTEGER,
	origen TEXT NOT NULL,
	destino TEXT NOT NULL,
	peso_inicial REAL NOT NULL,
	peso_real REAL,
	dañado INTEGER CHECK (dañado IN (0,1)) NOT NULL DEFAULT 0,
    observaciones TEXT,
	estado TEXT NOT NULL,
	num_intentos_entrega INTEGER NOT NULL,
	fecha TEXT,
	ubicacion_actual TEXT NOT NULL,
	FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
	FOREIGN KEY (id_transportista) REFERENCES Transportistas(id_transportista)
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

CREATE TABLE Transportistas (
    id_transportista INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL
);

CREATE TABLE Seguimiento (
	id_seguimiento INTEGER PRIMARY KEY AUTOINCREMENT,
    id_envio INTEGER NOT NULL,
    estado TEXT NOT NULL,
    fecha_hora TEXT NOT NULL,
    ubicacion TEXT NOT NULL,
    FOREIGN KEY (id_envio) REFERENCES Envios(id_envio)
);