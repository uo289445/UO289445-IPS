package giis.demo.tkrun.view.admin;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.view.usuario.VentanaUsuarioOpciones;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
	private JPanel pnCentral;
	private JButton btAsignación;
	private JButton btnNewButton_1;

	/**
	 * Create the frame.
	 */
	public VentanaAdmin() {
		setTitle("Panel del Administrador");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 240);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getLbTitulo(), BorderLayout.NORTH);
		contentPane.add(getPnCentral(), BorderLayout.CENTER);

	}

	private JLabel getLbTitulo() {
		if (lbTitulo == null) {
			lbTitulo = new JLabel("Menú del Aministrador");
			lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lbTitulo;
	}
	private JPanel getPnCentral() {
		if (pnCentral == null) {
			pnCentral = new JPanel();
			pnCentral.setLayout(new GridLayout(2, 1, 10, 10));
			pnCentral.add(getBtAsignación());
			pnCentral.add(getBtnNewButton_1());
		}
		return pnCentral;
	}
	private JButton getBtAsignación() {
		if (btAsignación == null) {
			btAsignación = new JButton("Asignar paquetes");
			btAsignación.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new VentanaAdminAsignacion().setVisible(true);
				}
			});
			btAsignación.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btAsignación;
	}
	private JButton getBtnNewButton_1() {
		if (btnNewButton_1 == null) {
			btnNewButton_1 = new JButton("New button");
			btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btnNewButton_1;
	}
}
