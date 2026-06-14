--Datos para carga inicial de la base de datos
	
DELETE FROM Usuarios;
INSERT INTO Usuarios (nombre, email, telefono, direccion, estado) VALUES
	('Tomás Cuesta', 'email1', '123456789', 'dir1', 'activo');
	
DELETE FROM Envios;
INSERT INTO Envios (id_usuario, id_transportista, origen, destino, distancia, tarifa, peso_inicial, estado, num_intentos_entrega, fecha, ubicacion_actual) VALUES 
(1, NULL, 'Madrid', 'Gijón', 'Nacional', 10.50, 2.50, 'En almacén', 0, '2026-06-13 10:00:00', 'Almacén Central'),
(1, NULL, 'Barcelona', 'Gijón', 'Nacional', 10.50, 5.00, 'En almacén', 0, '2026-06-13 11:30:00', 'Almacén Central');

INSERT INTO Envios (id_usuario, origen, destino, distancia, tarifa, peso_inicial, estado, num_intentos_entrega, fecha, ubicacion_actual) VALUES
(1, 'a', 'b', 'Local', 6.50, 3.5, 'Solicitado', 0, '2026-06-13 12:00:00', 'a');

INSERT INTO Envios (id_usuario, id_transportista, origen, destino, distancia, tarifa, peso_inicial, peso_real, dañado, observaciones, estado, num_intentos_entrega, fecha, ubicacion_actual) 
VALUES (1, 1, 'Calle Origen 12', 'Calle Destino 45, Oviedo', 'Local', 6.50, 2.5, 2.5, 0, '', 'En reparto', 0, '2026-06-14 09:00:00', 'Vehículo: Furgoneta 01 - Carlos Pérez');

INSERT INTO Envios (id_usuario, id_transportista, origen, destino, distancia, tarifa, peso_inicial, peso_real, dañado, observaciones, estado, num_intentos_entrega, fecha, ubicacion_actual) 
VALUES (1, 1, 'Gran Vía 12, Barcelona', 'Calle Uría 40, Gijón', 'Nacional', 10.50, 4.00, 4.00, 0, '', 'Entrega fallida', 4, '2026-06-13 19:00:00', 'En almacén');

DELETE FROM Tarifas;
INSERT INTO Tarifas (peso_min, peso_max, distancia, precio) VALUES
	(0, 2.00, 'Local', 4.50),
	(2.01, 5.00, 'Local', 6.50),
	(0, 2.00, 'Nacional', 7.90),
	(2.01, 5.00, 'Nacional', 10.50),
	(5.01, 10.00, 'Nacional', 12.50),
	(10.01, 50.00, 'Nacional', 20.00),
	(0, 5.00, 'Internacional', 25.00),
	(5.01, 50.00, 'Internacional', 40.00);
	
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