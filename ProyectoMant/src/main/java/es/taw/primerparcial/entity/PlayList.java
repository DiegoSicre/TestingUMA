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
@Table(name = "Playlist")
public class PlayList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Playlistid")
    private Integer playListId;
    @Basic(optional = false)
    @Column(name = "Playlistname")
    private String playListName;
    @Basic(optional = false)
    @Column(name = "Datecreation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @JoinColumn(name = "Usuarioid", referencedColumnName = "UsuarioId")
    @ManyToOne(optional = false)
    private Usuario usuarioId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "playListId")
    private List<PlayListCancion> playListCancionList;

    public PlayList() {
    }

    public PlayList(Integer playListId) {
        this.playListId = playListId;
    }

    public PlayList(Integer playListId, String playListName, Date dateCreation) {
        this.playListId = playListId;
        this.playListName = playListName;
        this.dateCreation = dateCreation;
    }

    public Integer getPlayListId() {
        return playListId;
    }

    public void setPlayListId(Integer playListId) {
        this.playListId = playListId;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
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
        hash += (playListId != null ? playListId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlayList)) {
            return false;
        }
        PlayList other = (PlayList) object;
        if ((this.playListId == null && other.playListId != null) || (this.playListId != null && !this.playListId.equals(other.playListId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.taw.primerparcial.entity.PlayList[ playListId=" + playListId + " ]";
    }
    
}
