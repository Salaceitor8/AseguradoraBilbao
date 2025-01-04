package domain;

public class Siniestros {
	
	private int id;
	private String dni;
	private String resumen;
	private TipoSeguro tipo;
	
	
	public Siniestros(int id, String dni, String resumen, TipoSeguro tipo) {
		super();
		this.id = id;
		this.dni = dni;
		this.resumen = resumen;
		this.tipo = tipo;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDni() {
		return dni;
	}


	public void setDni(String dni) {
		this.dni = dni;
	}


	public String getResumen() {
		return resumen;
	}


	public void setResumen(String resumen) {
		this.resumen = resumen;
	}


	public TipoSeguro getTipo() {
		return tipo;
	}


	public void setTipo(TipoSeguro tipo) {
		this.tipo = tipo;
	}


	@Override
	public String toString() {
		return "Siniestros [id=" + id + ", dni=" + dni + ", resumen=" + resumen + ", tipo=" + tipo + "]";
	}
	
	
	
	

}