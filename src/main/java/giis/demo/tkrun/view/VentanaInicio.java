package giis.demo.tkrun.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.view.usuario.VentanaUsuario;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lbTitulo;
	private JButton btnUsuario;
	private JButton btnTransportista;

	/**
	 * Create the frame.
	 */
	public VentanaInicio() {
		setTitle("Sistema de Transporte de Paquetes - Pantalla Inicial");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanel(), BorderLayout.CENTER);
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new GridLayout(5, 1, 10, 10));
			panel.add(getLbTitulo());
			panel.add(getBtnUsuario());
			panel.add(getBtnTransportista());
		}
		return panel;
	}
	private JLabel getLbTitulo() {
		if (lbTitulo == null) {
			lbTitulo = new JLabel("Seleccione cómo desea entrar");
			lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lbTitulo;
	}
	private JButton getBtnUsuario() {
		if (btnUsuario == null) {
			btnUsuario = new JButton("Entrar como usuario");
			btnUsuario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					VentanaUsuario vU = new VentanaUsuario();
					vU.setVisible(true);
				}
			});
			btnUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return btnUsuario;
	}
	private JButton getBtnTransportista() {
		if (btnTransportista == null) {
			btnTransportista = new JButton("Entrar como transportista");
			btnTransportista.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}
			});
			btnTransportista.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return btnTransportista;
	}
}
