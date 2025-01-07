package domain;

public class Notificacion {
	
	private int id;
	private String dni_cliente;
	private String resumen;
	
	public Notificacion(int id, String dni_cliente, String resumen) {
		super();
		this.id = id;
		this.dni_cliente = dni_cliente;
		this.resumen = resumen;
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

	@Override
	public String toString() {
		return "Notificacion [id=" + id + ", dni_cliente=" + dni_cliente + ", resumen=" + resumen + "]";
	}
	

}
