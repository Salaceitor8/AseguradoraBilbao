package domain;

public class Encuesta {
    private int id;
    private String dniCliente;
    private String fecha;
    private String satisfecho;
    private String aspectoFavorito;
    private int valoracion;
    private String comentario;

    // Constructor
    public Encuesta(int id, String dniCliente, String fecha, String satisfecho, String aspectoFavorito, int valoracion, String comentario) {
        this.id = id;
        this.dniCliente = dniCliente;
        this.fecha = fecha;
        this.satisfecho = satisfecho;
        this.aspectoFavorito = aspectoFavorito;
        this.valoracion = valoracion;
        this.comentario = comentario;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDniCliente() {
		return dniCliente;
	}

	public void setDniCliente(String dniCliente) {
		this.dniCliente = dniCliente;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getSatisfecho() {
		return satisfecho;
	}

	public void setSatisfecho(String satisfecho) {
		this.satisfecho = satisfecho;
	}

	public String getAspectoFavorito() {
		return aspectoFavorito;
	}

	public void setAspectoFavorito(String aspectoFavorito) {
		this.aspectoFavorito = aspectoFavorito;
	}

	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Override
	public String toString() {
		return "Encuesta [id=" + id + ", dniCliente=" + dniCliente + ", fecha=" + fecha + ", satisfecho=" + satisfecho
				+ ", aspectoFavorito=" + aspectoFavorito + ", valoracion=" + valoracion + ", comentario=" + comentario
				+ "]";
	}

    
}

