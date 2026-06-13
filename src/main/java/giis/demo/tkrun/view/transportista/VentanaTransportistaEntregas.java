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
import giis.demo.tkrun.service.EnvioDao;

public class VentanaTransportistaEntregas extends JFrame {

	private static final long serialVersionUID = 1L;
	private TransportistaDto t;
	private JPanel contentPane;
	private JLabel lbTitulo;
    private JScrollPane scrollPane;
    private JTable tableEntregas;
    private JButton btnAusencia;
    private DefaultTableModel modeloTabla;

	/**
	 * Create the frame.
	 */
	public VentanaTransportistaEntregas(TransportistaDto t) {
		this.t = t;
		setTitle("Terminal Portátil - Ruta de Entregas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.add(getLbTitulo());
        contentPane.add(getScrollPane());
        contentPane.add(getBtnAusencia());
        
        cargarEntregas();
	}

    private JLabel getLbTitulo() {
        if (lbTitulo == null) {
            lbTitulo = new JLabel("Entregas Pendientes - " + t.getNombre());
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
            scrollPane.setViewportView(getTableEntregas());
        }
        return scrollPane;
    }

    private JTable getTableEntregas() {
        if (tableEntregas == null) {
            tableEntregas = new JTable();
            modeloTabla = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID Envío", "Dirección de Destino", "Intentos Realizados" }
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };
            tableEntregas.setModel(modeloTabla);
            
            // Ajustamos el ancho de las columnas
            tableEntregas.getColumnModel().getColumn(0).setPreferredWidth(60);
            tableEntregas.getColumnModel().getColumn(1).setPreferredWidth(250);
            tableEntregas.getColumnModel().getColumn(2).setPreferredWidth(100);
        }
        return tableEntregas;
    }

    private JButton getBtnAusencia() {
        if (btnAusencia == null) {
            btnAusencia = new JButton("Marcar Ausencia");
            btnAusencia.setBounds(180, 300, 180, 40);
            btnAusencia.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    marcarAusencia();
                }
            });
        }
        return btnAusencia;
    }

    private void cargarEntregas() {
        modeloTabla.setRowCount(0); 
        EnvioDao dao = new EnvioDao();
        List<EnvioDto> lista = dao.obtenerEntregasPendientes(t.getIdTransportista());
        
        for (EnvioDto envio : lista) {
            modeloTabla.addRow(new Object[] {
                envio.getIdEnvio(),
                envio.getDestino(),
                envio.getNumIntentosEntrega()
            });
        }
    }
    
    private void marcarAusencia() {
        int filaSeleccionada = getTableEntregas().getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idEnvio = (int) getTableEntregas().getValueAt(filaSeleccionada, 0);
        
        EnvioDao dao = new EnvioDao();
        EnvioDto envioActual = dao.obtenerEnvioPorId(idEnvio);
        
        if (envioActual != null) {
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Confirmar que el destinatario del paquete " + idEnvio + " está ausente?", 
                "Confirmar Ausencia", JOptionPane.YES_NO_OPTION);
                
            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = dao.registrarAusencia(envioActual);
                
                if (exito) {
                    int nuevosIntentos = envioActual.getNumIntentosEntrega() + 1;
                    JOptionPane.showMessageDialog(this, 
                        "Ausencia registrada con éxito.\nIntentos fallidos de este paquete: " + nuevosIntentos, 
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                        
                    cargarEntregas();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
