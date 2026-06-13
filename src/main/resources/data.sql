--Datos para carga inicial de la base de datos

delete from Estados_envio;
insert into Estados_envio (nombre_estado) values 
	('Solicitado'),
	('En tránsito'),
	('En almacén'),
	('Cargado en vehículo'),
	('En reparto'),
	('Entregado'),
	('Entrega fallida'),
	('A la espera en oficina de destino');
	
DELETE FROM Usuarios;
INSERT INTO Usuarios (nombre, email, telefono, direccion, estado) VALUES
	('Tomás Cuesta', 'email1', '123456789', 'dir1', 'activo');

DELETE FROM Tarifas;
INSERT INTO Tarifas (peso_min, peso_max, precio) VALUES
	(0, 2.00, 4.50),
	(2.01, 5.00, 7.90),
	(5.01, 10.00, 12.50),
	(10.01, 5000.00, 20.00);

DELETE FROM Envios;
INSERT INTO Envios (id_usuario, origen, destino, peso_inicial, estado, num_intentos_entrega, fecha, ubicacion_actual) VALUES
	(1, 'a', 'b', 3.5, 'Solicitado', 0, '2026-06-13 12:00:00', 'a');
	
DELETE FROM Transportistas;
INSERT INTO Transportistas (nombre) VALUES
	('Furgoneta 01 - Carlos Pérez'),
	('Furgoneta 02 - María Gómez'),
	('Camión 01 - Juan López');