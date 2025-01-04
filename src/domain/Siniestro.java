package domain;

public class Siniestro {
	
	private int id;
	private String dni_cliente;
	private String resumen;
	private TipoSeguro tipoSeguro;
	private double precio;
	private EstadoSiniestro estado;
	
	
	public Siniestro(int id, String dni_cliente, String resumen, TipoSeguro tipo, double precio ,  EstadoSiniestro estado) {
		super();
		this.id = id;
		this.dni_cliente = dni_cliente;
		this.resumen = resumen;
		this.tipoSeguro = tipo;
		this.precio = precio;
		this.estado = estado;
	}


	public Double getPrecio() {
		return precio;
	}


	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDni_cliente() {
		return dni_cliente;
	}


	public void setDni_cliente(String dni_cliente) {
		this.dni_cliente = dni_cliente;
	}


	public String getResumen() {
		return resumen;
	}


	public void setResumen(String resumen) {
		this.resumen = resumen;
	}


	public TipoSeguro getTipoSeguro() {
		return tipoSeguro;
	}


	public void setTipoSeguro(TipoSeguro tipoSeguro) {
		this.tipoSeguro = tipoSeguro;
	}


	public EstadoSiniestro getEstado() {
		return estado;
	}


	public void setEstado(EstadoSiniestro estado) {
		this.estado = estado;
	}


	@Override
	public String toString() {
		return "Siniestro [id=" + id + ", dni_cliente=" + dni_cliente + ", resumen=" + resumen + ", tipoSeguro="
				+ tipoSeguro + ", estado=" + estado + "]";
	}
	
	
	
	

}
