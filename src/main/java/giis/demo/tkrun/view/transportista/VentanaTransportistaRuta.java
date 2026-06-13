package giis.demo.tkrun.view.transportista;

import java.awt.Font;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
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
		setBounds(100, 100, 543, 356);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.add(getLbTitulo());
        contentPane.add(getScrollPane());
        
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
}
