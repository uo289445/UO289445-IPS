package giis.demo.tkrun.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.model.EnvioDto;
import giis.demo.util.Database;

public class EnvioDao {
	private Database db = new Database();
	
	public int registrarEnvio(EnvioDto envio) {
		String sql = "INSERT INTO Envios (id_usuario, origen, destino, peso_inicial, estado, num_intentos_entrega, fecha, ubicacion_actual) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection cn = db.getConnection(); 
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, envio.getIdUsuario());
            ps.setString(2, envio.getOrigen());
            ps.setString(3, envio.getDestino());
            ps.setDouble(4, envio.getPesoInicial());
            ps.setString(5, envio.getEstado());
            ps.setInt(6, envio.getNumIntentosEntrega());
            ps.setString(7, envio.getFecha());
            ps.setString(8, envio.getUbicacionActual());
            
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
	
	public double calcularTarifa(double peso) {
		String sql = "SELECT precio FROM Tarifas WHERE ? >= peso_min AND ? <= peso_max";
		
		try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDouble(1, peso);
            ps.setDouble(2, peso);
            
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
}
