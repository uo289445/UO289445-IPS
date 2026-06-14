package giis.demo.tkrun.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.model.EnvioDto;
import giis.demo.util.Database;

public class EnvioDao {
	private Database db = new Database();
	
	public int registrarEnvio(EnvioDto envio) {
		String sql = "INSERT INTO Envios (id_usuario, origen, destino, distancia, tarifa, peso_inicial, estado, num_intentos_entrega, fecha, ubicacion_actual) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection cn = db.getConnection(); 
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
        	ps.setInt(1, envio.getIdUsuario());
            ps.setString(2, envio.getOrigen());
            ps.setString(3, envio.getDestino());
            ps.setString(4, envio.getDistancia());
            ps.setDouble(5, envio.getTarifa());
            ps.setDouble(6, envio.getPesoInicial());
            ps.setString(7, envio.getEstado());
            ps.setInt(8, envio.getNumIntentosEntrega());
            ps.setString(9, envio.getFecha());
            ps.setString(10, envio.getUbicacionActual());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    envio.setIdEnvio(idGenerado);
                    return idGenerado;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
	}
	
	public double calcularTarifa(double peso, String distancia) {
		String sql = "SELECT precio FROM Tarifas WHERE ? >= peso_min AND ? <= peso_max AND distancia = ?";
		
		try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDouble(1, peso);
            ps.setDouble(2, peso);
            ps.setString(3, distancia);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio"); // Retorna la tarifa encontrada
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1.0;
	}
	
	public boolean asignarTransportista(int idEnvio, int idTransportista) {
        String sql = "UPDATE Envios SET id_transportista = ? WHERE id_envio = ?";
        
        try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idTransportista);
            ps.setInt(2, idEnvio);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public EnvioDto obtenerEnvioPorId(int idEnvio) {
        String sql = "SELECT * FROM Envios WHERE id_envio = ?";
        
        try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idEnvio);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EnvioDto envio = new EnvioDto();
                    envio.setIdEnvio(rs.getInt("id_envio"));
                    envio.setIdUsuario(rs.getInt("id_usuario"));
                    envio.setIdTransportista(rs.getInt("id_transportista")); 
                    envio.setOrigen(rs.getString("origen"));
                    envio.setDestino(rs.getString("destino"));
                    envio.setPesoInicial(rs.getDouble("peso_inicial"));
                    envio.setPesoReal(rs.getDouble("peso_real"));
                    envio.setDanado(rs.getInt("dañado"));
                    envio.setObservaciones(rs.getString("observaciones"));
                    envio.setEstado(rs.getString("estado"));
                    envio.setNumIntentosEntrega(rs.getInt("num_intentos_entrega"));
                    envio.setFecha(rs.getString("fecha"));
                    envio.setUbicacionActual(rs.getString("ubicacion_actual"));
                    return envio;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public List<EnvioDto> obtenerRecogidasPendientes(int idTransportista) {
        List<EnvioDto> lista = new ArrayList<>();
        String sql = "SELECT id_envio, id_usuario, origen FROM Envios WHERE id_transportista = ? AND estado = 'Solicitado'";
        
        try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idTransportista);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnvioDto envio = new EnvioDto();
                    envio.setIdEnvio(rs.getInt("id_envio"));
                    envio.setIdUsuario(rs.getInt("id_usuario"));
                    envio.setOrigen(rs.getString("origen"));
                    lista.add(envio);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
	
	public boolean confirmarRecogida(int idEnvio, String nombreTransportista) {
        String updateEnvio = "UPDATE Envios SET estado = ?, ubicacion_actual = ? WHERE id_envio = ?";
        String insertSeguimiento = "INSERT INTO Seguimiento (id_envio, estado, fecha_hora, ubicacion) VALUES (?, ?, ?, ?)";
        
        String nuevoEstado = "En tránsito";
        String ubicacionVehiculo = "Vehículo: " + nombreTransportista;
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        try (Connection cn = db.getConnection()) {
            cn.setAutoCommit(false); 
            
            try (PreparedStatement psUpdate = cn.prepareStatement(updateEnvio)) {
                psUpdate.setString(1, nuevoEstado);
                psUpdate.setString(2, ubicacionVehiculo);
                psUpdate.setInt(3, idEnvio);
                psUpdate.executeUpdate();
            }
            
            try (PreparedStatement psInsert = cn.prepareStatement(insertSeguimiento)) {
                psInsert.setInt(1, idEnvio);
                psInsert.setString(2, nuevoEstado);
                psInsert.setString(3, fechaHoraActual);
                psInsert.setString(4, ubicacionVehiculo);
                psInsert.executeUpdate();
            }
            
            cn.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean registrarInspeccion(EnvioDto envio) {
        String updateEnvio = "UPDATE Envios SET peso_real = ?, dañado = ?, observaciones = ?, estado = ? WHERE id_envio = ?";
        String insertSeguimiento = "INSERT INTO Seguimiento (id_envio, estado, fecha_hora, ubicacion) VALUES (?, ?, ?, ?)";
        
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String ubicacion = "Almacén Central";
        
        try (Connection cn = db.getConnection()) {
            cn.setAutoCommit(false); 
            
            try (PreparedStatement psUpdate = cn.prepareStatement(updateEnvio)) {
                psUpdate.setDouble(1, envio.getPesoReal());
                psUpdate.setInt(2, envio.getDanado());
                psUpdate.setString(3, envio.getObservaciones());
                psUpdate.setString(4, envio.getEstado());
                psUpdate.setInt(5, envio.getIdEnvio());
                psUpdate.executeUpdate();
            }
            
            try (PreparedStatement psInsert = cn.prepareStatement(insertSeguimiento)) {
                psInsert.setInt(1, envio.getIdEnvio());
                
                String estadoHistorial = envio.getDanado() == 1 ? envio.getEstado() : "En almacén - Inspección superada";
                
                psInsert.setString(2, estadoHistorial);
                psInsert.setString(3, fechaHoraActual);
                psInsert.setString(4, ubicacion);
                psInsert.executeUpdate();
            }
            
            cn.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public String obtenerUltimaFechaSeguimiento(int idEnvio) {
        String sql = "SELECT fecha_hora FROM Seguimiento WHERE id_envio = ? ORDER BY id_seguimiento DESC LIMIT 1";
        
        try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idEnvio);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("fecha_hora");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Fecha no disponible";
    }
	
	public List<EnvioDto> obtenerEntregasPendientes(int idTransportista) {
        List<EnvioDto> lista = new ArrayList<>();
        String sql = "SELECT id_envio, id_usuario, destino, num_intentos_entrega FROM Envios WHERE id_transportista = ? AND estado = 'En reparto'";
        
        try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idTransportista);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnvioDto envio = new EnvioDto();
                    envio.setIdEnvio(rs.getInt("id_envio"));
                    envio.setIdUsuario(rs.getInt("id_usuario"));
                    envio.setDestino(rs.getString("destino"));
                    envio.setNumIntentosEntrega(rs.getInt("num_intentos_entrega"));
                    lista.add(envio);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
	
	public boolean registrarAusencia(EnvioDto envio) {
        String updateEnvio = "UPDATE Envios SET estado = ?, num_intentos_entrega = ?, fecha = ?, ubicacion_actual = ? WHERE id_envio = ?";
        String insertSeguimiento = "INSERT INTO Seguimiento (id_envio, estado, fecha_hora, ubicacion) VALUES (?, ?, ?, ?)";
        
        String nuevoEstado = "Entrega fallida";
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int nuevosIntentos = envio.getNumIntentosEntrega() + 1;
        
        try (Connection cn = db.getConnection()) {
            cn.setAutoCommit(false); 
            
            try (PreparedStatement psUpdate = cn.prepareStatement(updateEnvio)) {
                psUpdate.setString(1, nuevoEstado);
                psUpdate.setInt(2, nuevosIntentos);
                psUpdate.setString(3, fechaHoraActual);
                psUpdate.setString(4, "Almacén Central");
                psUpdate.setInt(5, envio.getIdEnvio());
                psUpdate.executeUpdate();
            }
            
            try (PreparedStatement psInsert = cn.prepareStatement(insertSeguimiento)) {
                psInsert.setInt(1, envio.getIdEnvio());
                psInsert.setString(2, nuevoEstado);
                psInsert.setString(3, fechaHoraActual);
                psInsert.setString(4, envio.getDestino());
                psInsert.executeUpdate();
            }
            
            cn.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public List<EnvioDto> obtenerEntregasFallidas() {
        List<EnvioDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Envios WHERE estado = 'Entrega fallida'";
        
        try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnvioDto envio = new EnvioDto();
                    envio.setIdEnvio(rs.getInt("id_envio"));
                    envio.setIdUsuario(rs.getInt("id_usuario"));
                    envio.setDestino(rs.getString("destino"));
                    envio.setNumIntentosEntrega(rs.getInt("num_intentos_entrega"));
                    lista.add(envio);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
	
	public boolean gestionarFalloEntrega(int idEnvio, boolean depositarEnOficina, String destino) {
        String updateEnvio = "UPDATE Envios SET estado = ?, ubicacion_actual = ?, id_transportista = NULL WHERE id_envio = ?";
        String insertSeguimiento = "INSERT INTO Seguimiento (id_envio, estado, fecha_hora, ubicacion) VALUES (?, ?, ?, ?)";
        
        String nuevoEstado = depositarEnOficina ? "A la espera en oficina de destino" : "Solicitado";
        String ubicacion = depositarEnOficina ? "Oficina Central" : "Almacén Central";
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        try (Connection cn = db.getConnection()) {
            cn.setAutoCommit(false); 
            
            try (PreparedStatement psUpdate = cn.prepareStatement(updateEnvio)) {
                psUpdate.setString(1, nuevoEstado);
                psUpdate.setString(2, ubicacion);
                psUpdate.setInt(3, idEnvio);
                psUpdate.executeUpdate();
            }
            
            try (PreparedStatement psInsert = cn.prepareStatement(insertSeguimiento)) {
                psInsert.setInt(1, idEnvio);
                psInsert.setString(2, nuevoEstado);
                psInsert.setString(3, fechaHoraActual);
                psInsert.setString(4, ubicacion);
                psInsert.executeUpdate();
            }
            
            cn.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
