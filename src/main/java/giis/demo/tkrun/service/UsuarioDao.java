package giis.demo.tkrun.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import giis.demo.tkrun.model.UsuarioDto;
import giis.demo.util.Database;

public class UsuarioDao {
	private Database db = new Database();
	
	public UsuarioDto findByEmail(String email) {
		String sql = "SELECT * from Usuarios WHERE email = ? AND estado = 'activo'";
		try (Connection cn = db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapUsuario(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private UsuarioDto mapUsuario(ResultSet rs) throws SQLException{
		return new UsuarioDto(rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("email"),
				rs.getString("telefono"), rs.getString("direccion"));
	}
}
