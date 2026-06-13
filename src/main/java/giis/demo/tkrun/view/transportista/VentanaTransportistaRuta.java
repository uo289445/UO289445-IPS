package giis.demo.tkrun.view.transportista;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.model.EnvioDto;
import giis.demo.tkrun.model.TransportistaDto;
import giis.demo.tkrun.model.UsuarioDto;
import giis.demo.tkrun.service.EnvioDao;
import giis.demo.tkrun.service.UsuarioDao;

public class VentanaTransportistaRuta extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
    private JScrollPane scrollPane;
    private JTable tableRecogidas;
    private JButton btnLeerCodigo;
    
    private TransportistaDto transportista;
    private DefaultTableModel modeloTabla;
    private UsuarioDao uDao = new UsuarioDao();

	/**
	 * Create the frame.
	 */
	public VentanaTransportistaRuta(TransportistaDto transportista) {
		setTitle("Portal del transportista - Recogidas");
		setResizable(false);
		this.transportista = transportista;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.add(getLbTitulo());
        contentPane.add(getScrollPane());
        contentPane.add(getBtnLeerCodigo());
        
        cargarRuta();
	}
	
	private JLabel getLbTitulo() {
        if (lbTitulo == null) {
            lbTitulo = new JLabel("Recogidas Pendientes - " + transportista.getNombre());
            lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
            lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            lbTitulo.setBounds(10, 11, 514, 30);
        }
        return lbTitulo;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(20, 60, 494, 220);
            scrollPane.setViewportView(getTableRecogidas());
        }
        return scrollPane;
    }

    private JTable getTableRecogidas() {
        if (tableRecogidas == null) {
            tableRecogidas = new JTable();
            modeloTabla = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID Envío", "Cliente", "Dirección de Recogida" }
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };
            tableRecogidas.setModel(modeloTabla);
            
            tableRecogidas.getColumnModel().getColumn(0).setPreferredWidth(60);
            tableRecogidas.getColumnModel().getColumn(1).setPreferredWidth(120);
            tableRecogidas.getColumnModel().getColumn(2).setPreferredWidth(250);
        }
        return tableRecogidas;
    }

    private void cargarRuta() {
        modeloTabla.setRowCount(0); 
        EnvioDao dao = new EnvioDao();
        List<EnvioDto> lista = dao.obtenerRecogidasPendientes(transportista.getIdTransportista());
        
        for (EnvioDto envio : lista) {
        	UsuarioDto usuario = uDao.findById(envio.getIdUsuario());
            modeloTabla.addRow(new Object[] {
                envio.getIdEnvio(),
                usuario.getNombre(),
                envio.getOrigen()
            });
        }
    }
    
    private JButton getBtnLeerCodigo() {
        if (btnLeerCodigo == null) {
            btnLeerCodigo = new JButton("Leer código");
            btnLeerCodigo.setBounds(150, 300, 250, 40);
            btnLeerCodigo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    confirmarPaqueteSeleccionado();
                }
            });
        }
        return btnLeerCodigo;
    }
    
    private void confirmarPaqueteSeleccionado() {
        int filaSeleccionada = getTableRecogidas().getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un paquete de la tabla simulando la lectura de su código.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idEnvio = (int) getTableRecogidas().getValueAt(filaSeleccionada, 0);
        
        EnvioDao dao = new EnvioDao();
        boolean exito = dao.confirmarRecogida(idEnvio, transportista.getNombre());
        
        if (exito) {
            JOptionPane.showMessageDialog(this, 
                "El paquete " + idEnvio + " ha sido leído correctamente.\nEstado actualizado a 'Recogido/En tránsito'.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            cargarRuta();
        } else {
            JOptionPane.showMessageDialog(this, "Error al confirmar la recogida en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
