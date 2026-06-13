package giis.demo.tkrun.view.transportista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.TransportistaDto;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaTransportistaOpciones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
	private JPanel pnCentral;
	private JButton btRecogidas;
	private JButton btEntregas;
	
	TransportistaDto t;

	/**
	 * Create the frame.
	 */
	public VentanaTransportistaOpciones(TransportistaDto t) {
		this.t = t;
		setTitle("Panel del Transportista - Opciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 374, 239);
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
			String nombre = t.getNombre().split(" - ")[1];
			lbTitulo = new JLabel("Bienvenido, " + nombre);
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
		}
		return lbTitulo;
	}
	private JPanel getPnCentral() {
		if (pnCentral == null) {
			pnCentral = new JPanel();
			pnCentral.setLayout(new GridLayout(0, 1, 10, 10));
			pnCentral.add(getBtRecogidas());
			pnCentral.add(getBtEntregas());
		}
		return pnCentral;
	}
	private JButton getBtRecogidas() {
		if (btRecogidas == null) {
			btRecogidas = new JButton("Recogidas");
			btRecogidas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new VentanaTransportistaRuta(t).setVisible(true);
				}
			});
			btRecogidas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btRecogidas;
	}
	private JButton getBtEntregas() {
		if (btEntregas == null) {
			btEntregas = new JButton("Entregas");
			btEntregas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new VentanaTransportistaEntregas(t).setVisible(true);
				}
			});
			btEntregas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btEntregas;
	}
}
