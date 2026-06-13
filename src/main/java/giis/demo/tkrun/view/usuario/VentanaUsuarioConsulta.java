package giis.demo.tkrun.view.usuario;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.EnvioDto;
import giis.demo.tkrun.model.UsuarioDto;
import giis.demo.tkrun.service.EnvioDao;

public class VentanaUsuarioConsulta extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
    private JLabel lbInstruccion;
    private JTextField txIdEnvioBusqueda;
    private JButton btnConsultar;
    private JSeparator separator;
    private JLabel lbEstado;
    private JTextField txEstado;
    private JLabel lbUbicacion;
    private JTextArea txUbicacion;
    
    private UsuarioDto usuario;

	/**
	 * Create the frame.
	 */
	public VentanaUsuarioConsulta(UsuarioDto usuario) {
		setResizable(false);
		this.usuario = usuario;
		setTitle("Panel del Usuario - Consulta de Estado");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 468, 248);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbTitulo());
        contentPane.add(getLbInstruccion());
        contentPane.add(getTxIdEnvioBusqueda());
        contentPane.add(getBtnConsultar());
        contentPane.add(getSeparator());
        contentPane.add(getLbEstado());
        contentPane.add(getTxEstado());
        contentPane.add(getLbUbicacion());
        contentPane.add(getTxUbicacion());
	}

	private JLabel getLbTitulo() {
        if (lbTitulo == null) {
            lbTitulo = new JLabel("Consulta de Paquetes");
            lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
            lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            lbTitulo.setBounds(10, 11, 434, 30);
        }
        return lbTitulo;
    }

    private JLabel getLbInstruccion() {
        if (lbInstruccion == null) {
            lbInstruccion = new JLabel("Introduzca el ID de su envío:");
            lbInstruccion.setBounds(30, 60, 180, 20);
        }
        return lbInstruccion;
    }

    private JTextField getTxIdEnvioBusqueda() {
        if (txIdEnvioBusqueda == null) {
            txIdEnvioBusqueda = new JTextField();
            txIdEnvioBusqueda.setBounds(210, 60, 90, 20);
            txIdEnvioBusqueda.setColumns(10);
            txIdEnvioBusqueda.addActionListener(e -> consultarPaquete());
        }
        return txIdEnvioBusqueda;
    }

    private JButton getBtnConsultar() {
        if (btnConsultar == null) {
            btnConsultar = new JButton("Consultar");
            btnConsultar.setBounds(310, 59, 100, 23);
            btnConsultar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    consultarPaquete();
                }
            });
        }
        return btnConsultar;
    }

    private JSeparator getSeparator() {
        if (separator == null) {
            separator = new JSeparator();
            separator.setBounds(10, 100, 434, 2);
        }
        return separator;
    }

    // --- SECCIÓN DE RESULTADOS ---

    private JLabel getLbEstado() {
        if (lbEstado == null) {
            lbEstado = new JLabel("Estado actual:");
            lbEstado.setBounds(30, 129, 120, 20);
        }
        return lbEstado;
    }

    private JTextField getTxEstado() {
        if (txEstado == null) {
            txEstado = new JTextField();
            txEstado.setEditable(false);
            txEstado.setBounds(160, 129, 284, 20);
            txEstado.setFont(new Font("Tahoma", Font.BOLD, 11));
        }
        return txEstado;
    }

    private JLabel getLbUbicacion() {
        if (lbUbicacion == null) {
            lbUbicacion = new JLabel("Última ubicación:");
            lbUbicacion.setBounds(30, 160, 120, 20);
        }
        return lbUbicacion;
    }

    private JTextArea getTxUbicacion() {
        if (txUbicacion == null) {
        	txUbicacion = new JTextArea();
            txUbicacion.setEditable(false);
            txUbicacion.setBounds(160, 160, 284, 40);
            txUbicacion.setBorder(new JTextField().getBorder()); 
            txUbicacion.setBackground(new Color(240, 240, 240)); 
            txUbicacion.setFont(new Font("Tahoma", Font.PLAIN, 11));
        }
        return txUbicacion;
    }

    private void consultarPaquete() {
        String idStr = getTxIdEnvioBusqueda().getText();
        
        if (idStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe introducir el ID de un envío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idEnvio = Integer.parseInt(idStr);
            EnvioDao dao = new EnvioDao();
            EnvioDto envio = dao.obtenerEnvioPorId(idEnvio);
            
            if (envio == null) {
                JOptionPane.showMessageDialog(this, "No se ha encontrado el paquete con ID: " + idEnvio, "Error", JOptionPane.ERROR_MESSAGE);
                limpiarResultados();
                return;
            }
            
            if (envio.getIdUsuario() != this.usuario.getIdUsuario()) {
                JOptionPane.showMessageDialog(this, "Este paquete no está asignado a usted", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                limpiarResultados();
                return;
            }
            
            getTxEstado().setText(envio.getEstado());
            String fechaUltimaModificacion = dao.obtenerUltimaFechaSeguimiento(idEnvio);
            if(fechaUltimaModificacion.equals("Fecha no disponible")) {
            	fechaUltimaModificacion = envio.getFecha();
            }
            getTxUbicacion().setText(envio.getUbicacionActual() + "\nFecha: " + fechaUltimaModificacion);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del envío debe ser un valor numérico.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
            limpiarResultados();
        }
    }
    
    private void limpiarResultados() {
        getTxEstado().setText("");
        getTxUbicacion().setText("");
    }
}
