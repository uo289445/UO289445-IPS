package giis.demo.tkrun.view.usuario;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.UsuarioDto;
import giis.demo.tkrun.service.UsuarioDao;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
	private JLabel lbEmail;
	private JTextField txEmail;
	private JPanel panel;
	private JButton btCancelar;
	private JButton btAcceso;
	private UsuarioDao uD = new UsuarioDao();

	/**
	 * Create the frame.
	 */
	public VentanaUsuario() {
		setResizable(false);
		setTitle("Panel del Usuario");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 542, 297);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbTitulo());
		contentPane.add(getLbEmail());
		contentPane.add(getTxEmail());
		contentPane.add(getPanel());

	}
	private JLabel getLbTitulo() {
		if (lbTitulo == null) {
			lbTitulo = new JLabel("Menu de Usuario");
			lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lbTitulo.setBounds(10, 11, 508, 55);
		}
		return lbTitulo;
	}
	private JLabel getLbEmail() {
		if (lbEmail == null) {
			lbEmail = new JLabel("Introduzca su Email:");
			lbEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lbEmail.setHorizontalAlignment(SwingConstants.CENTER);
			lbEmail.setBounds(57, 113, 176, 35);
		}
		return lbEmail;
	}
	private JTextField getTxEmail() {
		if (txEmail == null) {
			txEmail = new JTextField();
			txEmail.setBounds(275, 113, 176, 35);
			txEmail.setColumns(10);
		}
		return txEmail;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBounds(10, 205, 508, 55);
			panel.setLayout(new GridLayout(1, 2, 0, 0));
			panel.add(getBtCancelar());
			panel.add(getBtAcceso());
		}
		return panel;
	}
	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Cancelar");
			btCancelar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btCancelar;
	}
	private JButton getBtAcceso() {
		if (btAcceso == null) {
			btAcceso = new JButton("Acceder");
			btAcceso.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					comprobarID();
				}
			});
			btAcceso.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btAcceso;
	}
	
	private void comprobarID() {
		String email = getTxEmail().getText();
		if(email.isEmpty() || email.isBlank()) {
			JOptionPane.showMessageDialog(this, "Por favor, introduzca su Email.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		UsuarioDto usuario =uD.findByEmail(email);
		if(usuario!=null) {
			JOptionPane.showMessageDialog(this,
                    "Bienvenido/a, " + usuario.getNombre(),
                    "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
			new VentanaUsuarioOpciones(usuario).setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(this, "El Email introducido no está registrado o está inactivo.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
		}
	}
}
