package es.taw.primerparcial.dao;

import es.taw.primerparcial.entity.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistaRepository extends JpaRepository<Artista, Integer> {
    @Query("Select a FROM Artista a WHERE a.artistaName LIKE CONCAT('%', :cadenaContenida, '%')")
    List<Artista> findByContieneNombre(@Param("cadenaContenida") String cadenaContenida);


}
