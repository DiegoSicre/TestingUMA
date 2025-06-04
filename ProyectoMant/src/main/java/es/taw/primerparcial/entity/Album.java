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
import jakarta.persistence.JoinTable;
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
@Table(name = "Album")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Albumid")
    private Integer albumId;
    @Basic(optional = false)
    @Column(name = "Albumname")
    private String albumName;
    @Basic(optional = false)
    @Column(name = "Datereleased")
    @Temporal(TemporalType.DATE)
    private Date dateReleased;
    @JoinTable(name = "Albumgenero", joinColumns = {
        @JoinColumn(name = "Albumid", referencedColumnName = "AlbumId")}, inverseJoinColumns = {
        @JoinColumn(name = "Generoid", referencedColumnName = "GeneroId")})
    @ManyToMany
    private List<Genero> generoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "albumId")
    private List<Cancion> cancionList;
    @JoinColumn(name = "Artistaid", referencedColumnName = "ArtistaId")
    @ManyToOne(optional = false)
    private Artista artistaId;

    public Album() {
    }

    public Album(Integer albumId) {
        this.albumId = albumId;
    }

    public Album(Integer albumId, String albumName, Date dateReleased) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.dateReleased = dateReleased;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Date getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(Date dateReleased) {
        this.dateReleased = dateReleased;
    }

    public List<Genero> getGeneroList() {
        return generoList;
    }

    public void setGeneroList(List<Genero> generoList) {
        this.generoList = generoList;
    }

    public List<Cancion> getCancionList() {
        return cancionList;
    }

    public void setCancionList(List<Cancion> cancionList) {
        this.cancionList = cancionList;
    }

    public Artista getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(Artista artistaId) {
        this.artistaId = artistaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (albumId != null ? albumId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Album)) {
            return false;
        }
        Album other = (Album) object;
        if ((this.albumId == null && other.albumId != null) || (this.albumId != null && !this.albumId.equals(other.albumId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.taw.primerparcial.entity.Album[ albumId=" + albumId + " ]";
    }
    
}
