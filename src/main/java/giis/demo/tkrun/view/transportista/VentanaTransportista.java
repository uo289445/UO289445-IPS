package giis.demo.tkrun.view.transportista;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.TransportistaDto;
import giis.demo.tkrun.service.TransportistaDao;

public class VentanaTransportista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
    private JLabel lbId;
    private JTextField txIdTransportista;
    private JPanel panelBotones;
    private JButton btnCancelar;
    private JButton btnAcceder;

	/**
	 * Create the frame.
	 */
	public VentanaTransportista() {
		setTitle("Portal del Transportista - Acceso");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 206);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.add(getLbTitulo());
        contentPane.add(getLbId());
        contentPane.add(getTxIdTransportista());
        contentPane.add(getPanelBotones());
	}
	
	private JLabel getLbTitulo() {
        if (lbTitulo == null) {
            lbTitulo = new JLabel("Terminal Portátil");
            lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
            lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            lbTitulo.setBounds(40, 11, 364, 30);
        }
        return lbTitulo;
    }

    private JLabel getLbId() {
        if (lbId == null) {
            lbId = new JLabel("Número de Empleado (ID):");
            lbId.setBounds(62, 60, 160, 20);
        }
        return lbId;
    }

    private JTextField getTxIdTransportista() {
        if (txIdTransportista == null) {
            txIdTransportista = new JTextField();
            txIdTransportista.setBounds(253, 60, 100, 20);
            txIdTransportista.setColumns(10);
        }
        return txIdTransportista;
    }

    private JPanel getPanelBotones() {
        if (panelBotones == null) {
            panelBotones = new JPanel();
            panelBotones.setBounds(40, 118, 364, 40);
            panelBotones.setLayout(new GridLayout(1, 2, 10, 0));
            panelBotones.add(getBtnCancelar());
            panelBotones.add(getBtnAcceder());
        }
        return panelBotones;
    }

    private JButton getBtnCancelar() {
        if (btnCancelar == null) {
            btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(e -> dispose());
        }
        return btnCancelar;
    }

    private JButton getBtnAcceder() {
        if (btnAcceder == null) {
            btnAcceder = new JButton("Acceder");
            btnAcceder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    comprobarAcceso();
                }
            });
        }
        return btnAcceder;
    }

    private void comprobarAcceso() {
        String idStr = getTxIdTransportista().getText();
        if (idStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduzca su ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int idTransportista = Integer.parseInt(idStr);
            TransportistaDao dao = new TransportistaDao();
            TransportistaDto t = dao.buscarPorId(idTransportista);

            if (t != null) {
                new VentanaTransportistaRuta(t).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No se encuentra ningún transportista con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
