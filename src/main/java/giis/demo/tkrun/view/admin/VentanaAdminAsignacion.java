package giis.demo.tkrun.view.admin;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.EnvioDto;
import giis.demo.tkrun.model.TransportistaDto;
import giis.demo.tkrun.service.EnvioDao;
import giis.demo.tkrun.service.TransportistaDao;

public class VentanaAdminAsignacion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
    private JLabel lbIdEnvio;
    private JTextField txIdEnvio;
    private JLabel lbTransportista;
    private JComboBox<TransportistaDto> cbTransportistas;
    private JButton btnAsignar;

	/**
	 * Create the frame.
	 */
	public VentanaAdminAsignacion() {
		setTitle("Panel de Administrador - Asignar Transportista");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 250);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.add(getLbTitulo());
        contentPane.add(getLbIdEnvio());
        contentPane.add(getTxIdEnvio());
        contentPane.add(getLbTransportista());
        contentPane.add(getCbTransportistas());
        contentPane.add(getBtnAsignar());
        cargarTransportistas();
	}

    private JLabel getLbTitulo() {
        if (lbTitulo == null) {
            lbTitulo = new JLabel("Asignar Envío a Transportista");
            lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
            lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            lbTitulo.setBounds(10, 11, 414, 30);
        }
        return lbTitulo;
    }

    private JLabel getLbIdEnvio() {
        if (lbIdEnvio == null) {
            lbIdEnvio = new JLabel("ID del Envío:");
            lbIdEnvio.setBounds(50, 70, 100, 20);
        }
        return lbIdEnvio;
    }

    private JTextField getTxIdEnvio() {
        if (txIdEnvio == null) {
            txIdEnvio = new JTextField();
            txIdEnvio.setBounds(160, 70, 120, 20);
            txIdEnvio.setColumns(10);
        }
        return txIdEnvio;
    }

    private JLabel getLbTransportista() {
        if (lbTransportista == null) {
            lbTransportista = new JLabel("Transportista:");
            lbTransportista.setBounds(50, 110, 100, 20);
        }
        return lbTransportista;
    }

    private JComboBox<TransportistaDto> getCbTransportistas() {
        if (cbTransportistas == null) {
            cbTransportistas = new JComboBox<TransportistaDto>();
            cbTransportistas.setBounds(160, 110, 200, 22);
        }
        return cbTransportistas;
    }

    private JButton getBtnAsignar() {
        if (btnAsignar == null) {
            btnAsignar = new JButton("Asignar Paquete");
            btnAsignar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    asignarTransportista();
                }
            });
            btnAsignar.setBounds(130, 160, 160, 30);
        }
        return btnAsignar;
    }
    
    private void cargarTransportistas() {
        TransportistaDao dao = new TransportistaDao();
        List<TransportistaDto> lista = dao.obtenerTodosLosTransportistas();
        for (TransportistaDto t : lista) {
            getCbTransportistas().addItem(t);
        }
    }
    
    private void asignarTransportista() {
        String idEnvioStr = getTxIdEnvio().getText();
        
        if (idEnvioStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe introducir el ID del envío.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        TransportistaDto transportistaSeleccionado = (TransportistaDto) getCbTransportistas().getSelectedItem();
        
        if (transportistaSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un transportista.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int idEnvio = Integer.parseInt(idEnvioStr);
            EnvioDao envioDao = new EnvioDao();
            
            EnvioDto envioActual = envioDao.obtenerEnvioPorId(idEnvio);
            
            if (envioActual == null) {
                JOptionPane.showMessageDialog(this, "No se ha encontrado ningún envío con el ID: " + idEnvio, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(!envioActual.getEstado().equals("Solicitado")) {
            	JOptionPane.showMessageDialog(this, "El paquete no puede asignarse a un transportista", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (envioActual.getIdTransportista() != 0) {
                int respuesta = JOptionPane.showConfirmDialog(this,
                        "Este envío ya tiene un transportista asignado.\n¿Desea reasignarlo a " + transportistaSeleccionado.getNombre() + "?",
                        "Aviso de reasignación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                
                if (respuesta != JOptionPane.YES_OPTION) {
                    return; 
                }
            }
            
            boolean exito = envioDao.asignarTransportista(idEnvio, transportistaSeleccionado.getIdTransportista());
            
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "El envío " + idEnvio + " ha sido asignado a: " + transportistaSeleccionado.getNombre(), 
                    "Asignación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                getTxIdEnvio().setText(""); // Limpiar para el siguiente
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar la asignación. ¿Existe el ID del envío?", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del envío debe ser un número entero válido.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }
}
