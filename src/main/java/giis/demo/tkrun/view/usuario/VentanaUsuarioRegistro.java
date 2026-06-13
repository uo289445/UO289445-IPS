package giis.demo.tkrun.view.usuario;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import giis.demo.tkrun.model.EnvioDto;
import giis.demo.tkrun.model.UsuarioDto;
import giis.demo.tkrun.service.EnvioDao;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class VentanaUsuarioRegistro extends JFrame {

	private static final long serialVersionUID = 1L;
	private UsuarioDto usuario;
	private JPanel contentPane;
	private JLabel lbTitulo;
	private JLabel lbDirOrigen;
	private JTextField txOrigen;
	private JLabel lbDirDestino;
	private JLabel lbPeso;
	private JLabel lbTarifa;
	private JTextField txDestino;
	private JTextField txPeso;
	private JTextField txTarifa;
	private JButton btRetornar;
	private JButton btConfirmar;
	EnvioDao eDao = new EnvioDao();

	/**
	 * Create the frame.
	 */
	public VentanaUsuarioRegistro(UsuarioDto usuario) {
		setTitle("Panel del Usuario - Registro de envios");
		this.usuario = usuario;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 526, 348);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbTitulo());
		contentPane.add(getLbDirOrigen());
		contentPane.add(getTxOrigen());
		contentPane.add(getLbDirDestino());
		contentPane.add(getLbPeso());
		contentPane.add(getLbTarifa());
		contentPane.add(getTxDestino());
		contentPane.add(getTxPeso());
		contentPane.add(getTxTarifa());
		contentPane.add(getBtRetornar());
		contentPane.add(getBtConfirmar());
	}

	private JLabel getLbTitulo() {
		if (lbTitulo == null) {
			lbTitulo = new JLabel("Registrar un envio");
			lbTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
			lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lbTitulo.setBounds(10, 11, 492, 61);
		}
		return lbTitulo;
	}
	private JLabel getLbDirOrigen() {
		if (lbDirOrigen == null) {
			lbDirOrigen = new JLabel("Dirección de origen:");
			lbDirOrigen.setBounds(87, 95, 117, 20);
		}
		return lbDirOrigen;
	}
	private JTextField getTxOrigen() {
		if (txOrigen == null) {
			txOrigen = new JTextField();
			txOrigen.setBounds(214, 95, 192, 20);
			txOrigen.setColumns(10);
		}
		return txOrigen;
	}
	private JLabel getLbDirDestino() {
		if (lbDirDestino == null) {
			lbDirDestino = new JLabel("Dirección de destino:");
			lbDirDestino.setBounds(87, 126, 117, 20);
		}
		return lbDirDestino;
	}
	private JLabel getLbPeso() {
		if (lbPeso == null) {
			lbPeso = new JLabel("Peso (Kg):");
			lbPeso.setBounds(137, 174, 67, 20);
		}
		return lbPeso;
	}
	private JLabel getLbTarifa() {
		if (lbTarifa == null) {
			lbTarifa = new JLabel("Tarifa del servicio calculada:");
			lbTarifa.setBounds(49, 224, 155, 20);
		}
		return lbTarifa;
	}
	private JTextField getTxDestino() {
		if (txDestino == null) {
			txDestino = new JTextField();
			txDestino.setColumns(10);
			txDestino.setBounds(214, 126, 192, 20);
		}
		return txDestino;
	}
	private JTextField getTxPeso() {
		if (txPeso == null) {
			txPeso = new JTextField();
			txPeso.setColumns(10);
			txPeso.setBounds(214, 174, 192, 20);
		}
		return txPeso;
	}
	private JTextField getTxTarifa() {
		if (txTarifa == null) {
			txTarifa = new JTextField();
			txTarifa.setEditable(false);
			txTarifa.setColumns(10);
			txTarifa.setBounds(214, 224, 192, 20);
		}
		return txTarifa;
	}
	private JButton getBtRetornar() {
		if (btRetornar == null) {
			btRetornar = new JButton("Retornar");
			btRetornar.setBounds(121, 278, 129, 22);
		}
		return btRetornar;
	}
	private JButton getBtConfirmar() {
		if (btConfirmar == null) {
			btConfirmar = new JButton("Confirmar Envio");
			btConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					confirmarEnvio();
				}
			});
			btConfirmar.setBounds(260, 278, 129, 22);
		}
		return btConfirmar;
	}
	
	private void confirmarEnvio() {
		String origen = getTxOrigen().getText();
		String destino = getTxDestino().getText();
		String pesoStr = getTxPeso().getText();
		
		if (origen.isBlank() || destino.isBlank() || pesoStr.isBlank()) {
	        JOptionPane.showMessageDialog(this, "Por favor, rellene todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    double peso = 0.0;
	    try {
	        peso = Double.parseDouble(pesoStr);
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "El peso debe ser un valor numérico.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    
	    double tarifa = eDao.calcularTarifa(peso);
	    
	    if (tarifa < 0) {
	        JOptionPane.showMessageDialog(this, "No se encontró una tarifa para el peso indicado.", "Error de tarifa", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    
	    getTxTarifa().setText(String.format("%.2f €", tarifa));
	    
	    int confirmacion = JOptionPane.showConfirmDialog(this,
	            "La tarifa del servicio es de " + String.format("%.2f", tarifa) + " €.\n¿Desea confirmar el envío?",
	            "Confirmar Envío",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.QUESTION_MESSAGE);
	    
	    if (confirmacion == JOptionPane.YES_OPTION) {
	        EnvioDto nuevoEnvio = new EnvioDto();
	        
	        nuevoEnvio.setIdUsuario(this.usuario.getIdUsuario());
	        nuevoEnvio.setOrigen(origen);
	        nuevoEnvio.setDestino(destino);
	        nuevoEnvio.setPesoInicial(peso);
	        nuevoEnvio.setEstado("Solicitado");
	        nuevoEnvio.setNumIntentosEntrega(0);
	        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        nuevoEnvio.setFecha(LocalDateTime.now().format(formato));
	        nuevoEnvio.setUbicacionActual(origen); 

	        int idGenerado = eDao.registrarEnvio(nuevoEnvio);

	        if (idGenerado != -1) {
	            JOptionPane.showMessageDialog(this, 
	                    "Envío registrado correctamente.\nSu ID de seguimiento es: " + idGenerado, 
	                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
	            this.dispose();
	        } else {
	            JOptionPane.showMessageDialog(this, "Hubo un error al registrar el envío en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
}
