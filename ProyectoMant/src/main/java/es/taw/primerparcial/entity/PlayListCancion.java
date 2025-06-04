/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.taw.primerparcial.entity;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author guzman
 */
@Entity
@Table(name = "Playlistcancion")
public class PlayListCancion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Playlistcancionid")
    private Integer playListCancionId;
    @Basic(optional = false)
    @Column(name = "Orden")
    private int orden;
    @JoinColumn(name = "Cancionid", referencedColumnName = "CancionId")
    @ManyToOne(optional = false)
    private Cancion cancionId;
    @JoinColumn(name = "Playlistid", referencedColumnName = "PlayListId")
    @ManyToOne(optional = false)
    private PlayList playListId;

    public PlayListCancion() {
    }

    public PlayListCancion(Integer playListCancionId) {
        this.playListCancionId = playListCancionId;
    }

    public PlayListCancion(Integer playListCancionId, int orden) {
        this.playListCancionId = playListCancionId;
        this.orden = orden;
    }

    public Integer getPlayListCancionId() {
        return playListCancionId;
    }

    public void setPlayListCancionId(Integer playListCancionId) {
        this.playListCancionId = playListCancionId;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Cancion getCancionId() {
        return cancionId;
    }

    public void setCancionId(Cancion cancionId) {
        this.cancionId = cancionId;
    }

    public PlayList getPlayListId() {
        return playListId;
    }

    public void setPlayListId(PlayList playListId) {
        this.playListId = playListId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (playListCancionId != null ? playListCancionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlayListCancion)) {
            return false;
        }
        PlayListCancion other = (PlayListCancion) object;
        if ((this.playListCancionId == null && other.playListCancionId != null) || (this.playListCancionId != null && !this.playListCancionId.equals(other.playListCancionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.taw.primerparcial.entity.PlayListCancion[ playListCancionId=" + playListCancionId + " ]";
    }
    
}
