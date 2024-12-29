package domain;

import java.time.LocalDateTime;

public class Mensaje {
	private String remitente;
    private String contenido;
    private LocalDateTime fechaHora;

    public Mensaje(String remitente, String contenido, LocalDateTime fechaHora) {
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

}
