package giis.demo.tkrun.view.operario;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.EnvioDto;
import giis.demo.tkrun.service.EnvioDao;

public class VentanaOperarioAlmacen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTitulo;
    private JLabel lbIdEnvio;
    private JTextField txIdEnvio;
    private JLabel lbPesoActual;
    private JTextField txPesoActual;
    private JCheckBox chckbxDanado;
    private JLabel lbObservaciones;
    private JTextArea txObservaciones;
    private JButton btnConfirmar;

	/**
	 * Create the frame.
	 */
	public VentanaOperarioAlmacen() {
		setTitle("Panel Operario - Inspección de Paquetes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 387);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
        contentPane.add(getLbTitulo());
        contentPane.add(getLbIdEnvio());
        contentPane.add(getTxIdEnvio());
        contentPane.add(getLbPesoActual());
        contentPane.add(getTxPesoActual());
        contentPane.add(getChckbxDanado());
        contentPane.add(getLbObservaciones());
        contentPane.add(getTxObservaciones());
        contentPane.add(getBtnConfirmar());
	}
	
	private JLabel getLbTitulo() {
        if (lbTitulo == null) {
            lbTitulo = new JLabel("Inspección en Almacén");
            lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
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
            txIdEnvio.setBounds(180, 70, 120, 20);
            txIdEnvio.setColumns(10);
        }
        return txIdEnvio;
    }

    private JLabel getLbPesoActual() {
        if (lbPesoActual == null) {
            lbPesoActual = new JLabel("Peso en Báscula (kg):");
            lbPesoActual.setBounds(50, 110, 130, 20);
        }
        return lbPesoActual;
    }

    private JTextField getTxPesoActual() {
        if (txPesoActual == null) {
            txPesoActual = new JTextField();
            txPesoActual.setBounds(180, 110, 120, 20);
            txPesoActual.setColumns(10);
        }
        return txPesoActual;
    }

    private JCheckBox getChckbxDanado() {
        if (chckbxDanado == null) {
            chckbxDanado = new JCheckBox("Marcar paquete dañado o con desperfectos");
            chckbxDanado.setBounds(50, 150, 300, 23);
        }
        return chckbxDanado;
    }

    private JLabel getLbObservaciones() {
        if (lbObservaciones == null) {
            lbObservaciones = new JLabel("Observaciones:");
            lbObservaciones.setBounds(50, 190, 100, 20);
        }
        return lbObservaciones;
    }

    private JTextArea getTxObservaciones() {
        if (txObservaciones == null) {
            txObservaciones = new JTextArea();
            txObservaciones.setBounds(50, 220, 330, 49);
            txObservaciones.setBorder(new JTextField().getBorder()); 
            txObservaciones.setLineWrap(true);
        }
        return txObservaciones;
    }

    private JButton getBtnConfirmar() {
        if (btnConfirmar == null) {
            btnConfirmar = new JButton("Confirmar Inspección");
            btnConfirmar.setBounds(120, 300, 200, 39);
            btnConfirmar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    confirmarInspeccion();
                }
            });
        }
        return btnConfirmar;
    }

    private void confirmarInspeccion() {
        String idEnvioStr = getTxIdEnvio().getText();
        String pesoStr = getTxPesoActual().getText();

        if (idEnvioStr.isBlank() || pesoStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe introducir el ID del envío y el peso en báscula.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idEnvio = Integer.parseInt(idEnvioStr);
            double pesoBascula = Double.parseDouble(pesoStr);
            if(pesoBascula<0) {
            	JOptionPane.showMessageDialog(this, "El peso no puede ser menor que 0", "Peso inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }  
            
            EnvioDao dao = new EnvioDao();
            EnvioDto envio = dao.obtenerEnvioPorId(idEnvio);
            
            if (envio == null) {
                JOptionPane.showMessageDialog(this, "No se ha encontrado el envío " + idEnvio, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(!envio.getEstado().contains("En almacén")) {
            	JOptionPane.showMessageDialog(this, "El paquete no se encuentra en el almacén.", "Paquete no disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double margenError = 0.5;
            double diferencia = Math.abs(envio.getPesoInicial() - pesoBascula);
            
            if (diferencia > margenError) {
                JOptionPane.showMessageDialog(this, 
                    "¡ALERTA DE PÉRDIDA DE CONTENIDO!\n\nPeso esperado: " + envio.getPesoInicial() + " kg\nPeso en báscula: " + pesoBascula + " kg", 
                    "Alerta de Peso", JOptionPane.WARNING_MESSAGE);
            }

            envio.setPesoReal(pesoBascula);
            
            if (getChckbxDanado().isSelected()) {
                envio.setDanado(1);
                String obs = getTxObservaciones().getText().trim();
                envio.setObservaciones(obs);
                
                envio.setEstado("En almacén" + (obs.isEmpty() ? " - Con desperfectos" : " - " + obs));
            } else {
                envio.setDanado(0);
                envio.setObservaciones("");
                envio.setEstado("En almacén");
            }

            boolean exito = dao.registrarInspeccion(envio);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Inspección registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                getTxIdEnvio().setText("");
                getTxPesoActual().setText("");
                getChckbxDanado().setSelected(false);
                getTxObservaciones().setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la inspección.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID y el peso deben ser valores numéricos.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }

}
