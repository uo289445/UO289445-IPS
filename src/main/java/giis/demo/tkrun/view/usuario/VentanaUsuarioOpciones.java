package giis.demo.tkrun.view.usuario;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.UsuarioDto;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaUsuarioOpciones extends JFrame {

	private static final long serialVersionUID = 1L;
	private UsuarioDto usuario;
	private JPanel contentPane;
	private JLabel lbTitulo;
	private JPanel pnCentral;
	private JButton btRegistrarEnvio;
	private JButton btConsultarEnvio;

	/**
	 * Create the frame.
	 */
	public VentanaUsuarioOpciones(UsuarioDto usuario) {
		this.usuario = usuario;
		setTitle("Panel del Usuario - Opciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 374, 239);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 15));
		contentPane.add(getLbTitulo(), BorderLayout.NORTH);
		contentPane.add(getPnCentral(), BorderLayout.CENTER);

	}

	private JLabel getLbTitulo() {
		if (lbTitulo == null) {
			lbTitulo = new JLabel("Bienvenido, " + usuario.getNombre());
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
		}
		return lbTitulo;
	}
	private JPanel getPnCentral() {
		if (pnCentral == null) {
			pnCentral = new JPanel();
			pnCentral.setLayout(new GridLayout(0, 1, 10, 10));
			pnCentral.add(getBtRegistrarEnvio());
			pnCentral.add(getBtConsultarEnvio());
		}
		return pnCentral;
	}
	private JButton getBtRegistrarEnvio() {
		if (btRegistrarEnvio == null) {
			btRegistrarEnvio = new JButton("Registrar un envio");
			btRegistrarEnvio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new VentanaUsuarioRegistro(usuario).setVisible(true);
				}
			});
			btRegistrarEnvio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btRegistrarEnvio;
	}
	private JButton getBtConsultarEnvio() {
		if (btConsultarEnvio == null) {
			btConsultarEnvio = new JButton("New button");
		}
		return btConsultarEnvio;
	}
}
