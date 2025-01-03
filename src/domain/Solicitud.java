package domain;

public class Solicitud {
    private int id;
    private String dniCliente;
    private String pregunta;
    private EstadoSolicitud estado;
    private String respuesta;

    // Constructor completo
    public Solicitud(int id, String dniCliente, String pregunta, EstadoSolicitud estadoSolicitud, String respuesta) {
        this.id = id;
        this.dniCliente = dniCliente;
        this.pregunta = pregunta;
        this.estado = estadoSolicitud;
        this.respuesta = respuesta;
    }

    // Constructor sin ID (para cuando se crea una nueva solicitud)
    public Solicitud(String dniCliente, String pregunta, EstadoSolicitud estado) {
        this.dniCliente = dniCliente;
        this.pregunta = pregunta;
        this.estado = estado;
    }

    // Getters y setters
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

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    // Método toString
    @Override
    public String toString() {
        return "Solicitud{" +
                "id=" + id +
                ", dniCliente='" + dniCliente + '\'' +
                ", pregunta='" + pregunta + '\'' +
                ", estado='" + estado + '\'' +
                ", respuesta='" + respuesta + '\'' +
                '}';
    }
}
