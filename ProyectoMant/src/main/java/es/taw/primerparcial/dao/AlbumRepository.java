package es.taw.primerparcial.dao;

import es.taw.primerparcial.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
}
