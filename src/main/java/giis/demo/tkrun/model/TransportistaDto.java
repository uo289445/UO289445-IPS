package giis.demo.tkrun.model;

public class TransportistaDto {
	private int idTransportista;
    private String nombre;

    public TransportistaDto() {
    	
    }

    public TransportistaDto(int idTransportista, String nombre) {
        this.idTransportista = idTransportista;
        this.nombre = nombre;
    }

    public int getIdTransportista() { return idTransportista; }
    public void setIdTransportista(int idTransportista) { this.idTransportista = idTransportista; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}
