package giis.demo.tkrun.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            
            // Recuperamos el ID autogenerado
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
}
