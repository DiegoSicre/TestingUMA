/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.taw.primerparcial.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author guzman
 */
@Entity
@Table(name = "Artista")
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Artistaid")
    private Integer artistaId;
    @Basic(optional = false)
    @Column(name = "Artistaname")
    private String artistaName;
    @JoinTable(name = "Colaboracionartista", joinColumns = {
        @JoinColumn(name = "Artistaid", referencedColumnName = "ArtistaId")}, inverseJoinColumns = {
        @JoinColumn(name = "Cancionid", referencedColumnName = "CancionId")})
    @ManyToMany
    private List<Cancion> cancionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artistaId")
    private List<Album> albumList;

    public Artista() {
    }

    public Artista(Integer artistaId) {
        this.artistaId = artistaId;
    }

    public Artista(Integer artistaId, String artistaName) {
        this.artistaId = artistaId;
        this.artistaName = artistaName;
    }
    //Cómo el total de canciones propias no es directamente accesible vamos a sacrlo de aquí
    public List<Cancion> getCancionesPropias(){
        List<Cancion> resultado = new ArrayList<>();
        for(Album aux : albumList){
            for(Cancion auxCancion : aux.getCancionList()){
                resultado.add(auxCancion);
            }
        }
        return resultado;

    }
    public Integer getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(Integer artistaId) {
        this.artistaId = artistaId;
    }

    public String getArtistaName() {
        return artistaName;
    }

    public void setArtistaName(String artistaName) {
        this.artistaName = artistaName;
    }

    public List<Cancion> getCancionList() {
        return cancionList;
    }

    public void setCancionList(List<Cancion> cancionList) {
        this.cancionList = cancionList;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (artistaId != null ? artistaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artista)) {
            return false;
        }
        Artista other = (Artista) object;
        if ((this.artistaId == null && other.artistaId != null) || (this.artistaId != null && !this.artistaId.equals(other.artistaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.taw.primerparcial.entity.Artista[ artistaId=" + artistaId + " ]";
    }
    
}
