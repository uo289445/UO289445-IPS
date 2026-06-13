--Datos para carga inicial de la base de datos
	
DELETE FROM Usuarios;
INSERT INTO Usuarios (nombre, email, telefono, direccion, estado) VALUES
	('Tomás Cuesta', 'email1', '123456789', 'dir1', 'activo');
	
DELETE FROM Envios;
INSERT INTO Envios (id_usuario, id_transportista, origen, destino, peso_inicial, estado, num_intentos_entrega, fecha, ubicacion_actual) VALUES 
(1, NULL, 'Madrid', 'Gijón', 2.50, 'En almacén', 0, '2026-06-13 10:00:00', 'Almacén Central'),
(1, NULL, 'Barcelona', 'Gijón', 5.00, 'En almacén', 0, '2026-06-13 11:30:00', 'Almacén Central');

INSERT INTO Envios (id_usuario, origen, destino, peso_inicial, estado, num_intentos_entrega, fecha, ubicacion_actual) VALUES
	(1, 'a', 'b', 3.5, 'Solicitado', 0, '2026-06-13 12:00:00', 'a');

DELETE FROM Tarifas;
INSERT INTO Tarifas (peso_min, peso_max, precio) VALUES
	(0, 2.00, 4.50),
	(2.01, 5.00, 7.90),
	(5.01, 10.00, 12.50),
	(10.01, 5000.00, 20.00);
	
DELETE FROM Transportistas;
INSERT INTO Transportistas (nombre) VALUES
	('Furgoneta 01 - Carlos Pérez'),
	('Furgoneta 02 - María Gómez'),
	('Camión 01 - Juan López');

DELETE FROM Seguimiento;
INSERT INTO Seguimiento (id_envio, estado, fecha_hora, ubicacion) VALUES	
(1, 'En almacén', '2026-06-13 10:00:00', 'Almacén Central'),
(2, 'En almacén', '2026-06-13 11:30:00', 'Almacén Central'),
(3, 'Solicitado', '2026-06-13 12:00:00', 'a');