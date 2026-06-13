package giis.demo.tkrun.view.admin;

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
import giis.demo.tkrun.model.UsuarioDto;
import giis.demo.tkrun.service.EnvioDao;
import giis.demo.tkrun.service.UsuarioDao;

public class VentanaAdminEntregasFallidas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
    private JScrollPane scrollPane;
    private JTable tableFallidas;
    private JButton btnGestionar;
    
    private DefaultTableModel modeloTabla;

	/**
	 * Create the frame.
	 */
	public VentanaAdminEntregasFallidas() {
		setTitle("Panel del Administrador - Gestión de Entregas Fallidas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.add(getLbTitulo());
        contentPane.add(getScrollPane());
        contentPane.add(getBtnGestionar());
        
        cargarEntregasFallidas();
	}
	
	private JLabel getLbTitulo() {
        if (lbTitulo == null) {
            lbTitulo = new JLabel("Resolución de Entregas Fallidas (Ausencias)");
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
            scrollPane.setViewportView(getTableFallidas());
        }
        return scrollPane;
    }

    private JTable getTableFallidas() {
        if (tableFallidas == null) {
            tableFallidas = new JTable();
            modeloTabla = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID Envío", "Destino", "Intentos Fallidos" }
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };
            tableFallidas.setModel(modeloTabla);
        }
        return tableFallidas;
    }

    private JButton getBtnGestionar() {
        if (btnGestionar == null) {
            btnGestionar = new JButton("Gestionar Paquete Seleccionado");
            btnGestionar.setBounds(140, 300, 250, 40);
            btnGestionar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    procesarPaqueteFallido();
                }
            });
        }
        return btnGestionar;
    }

    private void cargarEntregasFallidas() {
        modeloTabla.setRowCount(0); 
        EnvioDao dao = new EnvioDao();
        List<EnvioDto> lista = dao.obtenerEntregasFallidas();
        
        for (EnvioDto envio : lista) {
            modeloTabla.addRow(new Object[] {
                envio.getIdEnvio(),
                envio.getDestino(),
                envio.getNumIntentosEntrega()
            });
        }
    }
    
    private void procesarPaqueteFallido() {
        int filaSeleccionada = getTableFallidas().getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idEnvio = (int) getTableFallidas().getValueAt(filaSeleccionada, 0);
        
        EnvioDao dao = new EnvioDao();
        EnvioDto envio = dao.obtenerEnvioPorId(idEnvio);
        
        if (envio == null) return;

        if (envio.getNumIntentosEntrega() < 4) {
            
            boolean exito = dao.gestionarFalloEntrega(idEnvio, false, envio.getDestino());
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Se ha reprogramado una nueva entrega.\nEl paquete vuelve a estar 'En almacén' para ser reasignado a un transportista.", 
                    "Nuevo intento programado (" + envio.getNumIntentosEntrega() + "/4)", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } else {
            
            boolean exito = dao.gestionarFalloEntrega(idEnvio, true, envio.getDestino());
            if (exito) {
                UsuarioDao usuarioDao = new UsuarioDao();
                UsuarioDto usuario = usuarioDao.findById(envio.getIdUsuario());
                
                JOptionPane.showMessageDialog(this, 
                    "Se han agotado los 4 intentos de entrega.\nEl paquete ha sido depositado en la Oficina de Destino.\n\n"
                    + ">>> SIMULACIÓN DE CORREO ENVIADO A: " + usuario.getEmail() + " <<<\n"
                    + "Asunto: Su paquete está en la oficina\n"
                    + "Mensaje: Lamentamos comunicarle que tras varios intentos fallidos, "
                    + "su paquete está listo para ser recogido físicamente en nuestras instalaciones.", 
                    "Depósito en Oficina", 
                    JOptionPane.WARNING_MESSAGE);
                System.out.println("Aviso de intentos de envio agotados enviado al correo " + usuario.getEmail() + " del cliente "+ usuario.getNombre());
            }
        }
        
        cargarEntregasFallidas();
    }
}
