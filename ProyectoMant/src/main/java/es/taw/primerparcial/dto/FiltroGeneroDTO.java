package es.taw.primerparcial.dto;

public class FiltroGeneroDTO {

    private Integer idGenero;
    private String fechaInicio;
    private String fechaFin;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public FiltroGeneroDTO() {
        this.idGenero = -1;
        this.fechaInicio = null;
        this.fechaFin = null;
    }

    public Integer getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Integer idGenero) {
        this.idGenero = idGenero;
    }
}
