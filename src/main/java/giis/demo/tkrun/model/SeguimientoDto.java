package giis.demo.tkrun.model;

public class SeguimientoDto {
	private int idSeguimiento;
    private int idEnvio;
    private String estado;
    private String fechaHora;
    private String ubicacion;

    public SeguimientoDto() { }

    public int getIdSeguimiento() { return idSeguimiento; }
    public void setIdSeguimiento(int idSeguimiento) { this.idSeguimiento = idSeguimiento; }

    public int getIdEnvio() { return idEnvio; }
    public void setIdEnvio(int idEnvio) { this.idEnvio = idEnvio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
