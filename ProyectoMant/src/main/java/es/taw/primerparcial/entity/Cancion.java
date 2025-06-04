/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.taw.primerparcial.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author guzman
 */
@Entity
@Table(name = "Cancion")
public class Cancion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Cancionid")
    private Integer cancionId;
    @Basic(optional = false)
    @Column(name = "Cancionname")
    private String cancionName;
    @Basic(optional = false)
    @Column(name = "Datereleased")
    @Temporal(TemporalType.DATE)
    private Date dateReleased;
    @ManyToMany(mappedBy = "cancionList")
    private List<Artista> artistaList;
    @JoinColumn(name = "Albumid", referencedColumnName = "AlbumId")
    @ManyToOne(optional = false)
    private Album albumId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cancionId")
    private List<PlayListCancion> playListCancionList;

    public Cancion() {
    }

    public Cancion(Integer cancionId) {
        this.cancionId = cancionId;
    }

    public Cancion(Integer cancionId, String cancionName, Date dateReleased) {
        this.cancionId = cancionId;
        this.cancionName = cancionName;
        this.dateReleased = dateReleased;
    }

    public Integer getCancionId() {
        return cancionId;
    }

    public void setCancionId(Integer cancionId) {
        this.cancionId = cancionId;
    }

    public String getCancionName() {
        return cancionName;
    }

    public void setCancionName(String cancionName) {
        this.cancionName = cancionName;
    }

    public Date getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(Date dateReleased) {
        this.dateReleased = dateReleased;
    }

    public List<Artista> getArtistaList() {
        return artistaList;
    }

    public void setArtistaList(List<Artista> artistaList) {
        this.artistaList = artistaList;
    }

    public Album getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Album albumId) {
        this.albumId = albumId;
    }

    public List<PlayListCancion> getPlayListCancionList() {
        return playListCancionList;
    }

    public void setPlayListCancionList(List<PlayListCancion> playListCancionList) {
        this.playListCancionList = playListCancionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cancionId != null ? cancionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cancion)) {
            return false;
        }
        Cancion other = (Cancion) object;
        if ((this.cancionId == null && other.cancionId != null) || (this.cancionId != null && !this.cancionId.equals(other.cancionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.taw.primerparcial.entity.Cancion[ cancionId=" + cancionId + " ]";
    }
    
}
