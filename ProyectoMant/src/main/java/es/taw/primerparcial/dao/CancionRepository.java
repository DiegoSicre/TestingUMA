package es.taw.primerparcial.dao;

import es.taw.primerparcial.entity.Cancion;
import es.taw.primerparcial.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CancionRepository extends JpaRepository<Cancion, Integer> {
    @Query("Select c from Cancion c JOIN c.albumId a JOIN a.generoList g WHERE g.generoId=:genero and c.dateReleased BETWEEN :fechaInicio and :fechaFin" )
    List<Cancion> filtrarPorGeneroYFechas(@Param("genero") Integer genero, @Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    @Query("SELECT c FROM Cancion c JOIN c.albumId a JOIN a.generoList g WHERE g.generoId=:genero")
    List<Cancion> filtrarPorGenero(@Param("genero") Integer genero);
}
