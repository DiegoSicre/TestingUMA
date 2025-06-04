package es.taw.primerparcial.dto;

import java.util.ArrayList;
import java.util.List;

public class RecopilatorioDTO {
    private String nombreRecopilatorio;
    private String nombreAlbum;
    private List<Integer> idCancionesRecopilatorio;

    public RecopilatorioDTO() {
        this.nombreRecopilatorio = "";
        this.nombreAlbum = "";
        this.idCancionesRecopilatorio = new ArrayList<Integer>();
    }

    public String getNombreRecopilatorio() {
        return nombreRecopilatorio;
    }

    public void setNombreRecopilatorio(String nombreRecopilatorio) {
        this.nombreRecopilatorio = nombreRecopilatorio;
    }

    public String getNombreAlbum() {
        return nombreAlbum;
    }

    public void setNombreAlbum(String nombreAlbum) {
        this.nombreAlbum = nombreAlbum;
    }

    public List<Integer> getIdCancionesRecopilatorio() {
        return idCancionesRecopilatorio;
    }

    public void setIdCancionesRecopilatorio(List<Integer> idCancionesRecopilatorio) {
        this.idCancionesRecopilatorio = idCancionesRecopilatorio;
    }
}
