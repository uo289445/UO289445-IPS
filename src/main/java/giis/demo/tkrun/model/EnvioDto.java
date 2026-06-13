package giis.demo.tkrun.model;

public class EnvioDto {
	private int idEnvio;
    private int idUsuario;
    private int idTransportista;
    private String origen;
    private String destino;
    private double pesoInicial;
    private double pesoReal;
    private int danado;
    private String observaciones;
    private String estado;
    private int numIntentosEntrega;
    private String fecha;
    private String ubicacionActual;

    public EnvioDto() {
    	
    }

    public int getIdEnvio() { return idEnvio; }
    public void setIdEnvio(int idEnvio) { this.idEnvio = idEnvio; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    
    public int getIdTransportista() { return idTransportista; }
    public void setIdTransportista(int idTransportista) { this.idTransportista = idTransportista; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public double getPesoInicial() { return pesoInicial; }
    public void setPesoInicial(double pesoInicial) { this.pesoInicial = pesoInicial; }
    
    public double getPesoReal() { return pesoReal; }
    public void setPesoReal(double pesoReal) { this.pesoReal = pesoReal; }

    public int getDanado() { return danado; }
    public void setDanado(int danado) { this.danado = danado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getNumIntentosEntrega() { return numIntentosEntrega; }
    public void setNumIntentosEntrega(int numIntentosEntrega) { this.numIntentosEntrega = numIntentosEntrega; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(String ubicacionActual) { this.ubicacionActual = ubicacionActual; }
}
