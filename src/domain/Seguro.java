package domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Seguro {
	private int contador = 0;
    private int idSeguro; // ID único para identificar el seguro
    private TipoSeguro tipo;
    private LocalDate fechaContratacion;
    private double costoMensual;
    private String estado;

    // Formato de fecha estándar (puede cambiar según las necesidades)
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor
    public Seguro(TipoSeguro tipo, LocalDate fechaContratacion, double costoMensual, String estado) {
        this.idSeguro = contador;
        contador++;
        this.tipo = tipo;
        this.fechaContratacion = fechaContratacion;
        this.costoMensual = costoMensual;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdSeguro() {
        return idSeguro;
    }

    public TipoSeguro getTipo() {
        return tipo;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public double getCostoMensual() {
        return costoMensual;
    }

    public void setCostoMensual(double costoMensual) {
        this.costoMensual = costoMensual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    // Método para obtener la fecha de contratación en formato String
    public String getFechaContratacionFormato() {
        return this.fechaContratacion.format(FORMATO_FECHA);
    }

    // Método para establecer la fecha a partir de un String
    public void setFechaContratacionDesdeString(String fechaStr) {
        this.fechaContratacion = LocalDate.parse(fechaStr, FORMATO_FECHA);
    }
    
 // Método para convertir a CSV los atributos comunes del seguro
    public String toCSV() {
        return tipo + ";" + fechaContratacion.format(FORMATO_FECHA) + ";" + costoMensual + ";" + estado;
    }

    @Override
    public String toString() {
        return "Seguro [idSeguro=" + idSeguro + ", tipo=" + tipo + ", fechaContratacion=" + fechaContratacion 
                + ", costoMensual=" + costoMensual + ", estado=" + estado + "]";
    }
}
