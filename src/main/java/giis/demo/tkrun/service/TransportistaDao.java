package giis.demo.tkrun.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.model.TransportistaDto;
import giis.demo.util.Database;

public class TransportistaDao {
	private Database db = new Database();

    public List<TransportistaDto> obtenerTodosLosTransportistas() {
        List<TransportistaDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Transportistas";
        
        try (Connection cn = db.getConnection(); 
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                TransportistaDto t = new TransportistaDto(
                    rs.getInt("id_transportista"),
                    rs.getString("nombre")
                );
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public TransportistaDto buscarPorId(int idTransportista) {
        String sql = "SELECT * FROM Transportistas WHERE id_transportista = ?";
        try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idTransportista);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TransportistaDto(rs.getInt("id_transportista"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
