package es.taw.primerparcial.controller;

import es.taw.primerparcial.dao.AlbumRepository;
import es.taw.primerparcial.dao.ArtistaRepository;
import es.taw.primerparcial.dao.CancionRepository;

import es.taw.primerparcial.dao.GeneroRepository;
import es.taw.primerparcial.dto.FiltroGeneroDTO;
import es.taw.primerparcial.dto.FiltroNombreDTO;
import es.taw.primerparcial.dto.RecopilatorioDTO;
import es.taw.primerparcial.entity.Album;
import es.taw.primerparcial.entity.Artista;
import es.taw.primerparcial.entity.Cancion;
import es.taw.primerparcial.entity.Genero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class mainController {

    //Definimos el controlador principal
    @Autowired
    ArtistaRepository artistaRepository;
    @Autowired
    CancionRepository cancionRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    GeneroRepository generoRepository;

    @GetMapping("/")
    public String listarArtistas(Model model) {

        return this.filtrarArtistaPorNombre(model, null);
    }

    @GetMapping("/filtrarArtistaPorNombre")
    public String filtrarArtistaPorNombre(Model model, @ModelAttribute FiltroNombreDTO filtro) {
        List<Artista> artistas;
        if(filtro == null) {
            artistas = artistaRepository.findAll();
        }else{
            artistas = artistaRepository.findByContieneNombre(filtro.getCadenaFiltroNombre());
        }
        FiltroNombreDTO filtroVirgen = new FiltroNombreDTO();
        model.addAttribute("filtro", filtroVirgen);

        model.addAttribute("artistas", artistas);

        return "listarArtistas";
    }




    //Vamos a hacer una breve explicación de porque es necesario refactorizar el método del controlador cuando añadimos un filtro
    //La explicación es la siguiente, si yo tengo que poder cargar una página antes de que se aplique el filtro y luego la misma página con el filtro aplicado
    //Necesito poder dirirme a "controladores" o mejor dicho a métodos de controladores diferentes
    //Uno génerico que me permite cargar la página, que sería el del anchor del jsp y otro específico que es al que se dirije el form de filtrar
    @GetMapping("/añadirAlbumConFiltro")
    public String addAlbumConFiltro(Model model, @ModelAttribute FiltroGeneroDTO filtro) {
        List<Cancion> canciones;
        if(filtro == null || filtro.getIdGenero() == -1) {
            //Caso base, en el que se carga por primera vez
            filtro = new FiltroGeneroDTO();
            canciones = cancionRepository.findAll();
        }else if(filtro.getFechaInicio() == null || filtro.getFechaFin() == null || filtro.getFechaInicio() == "" || filtro.getFechaFin() == "") {
            canciones = cancionRepository.filtrarPorGenero(filtro.getIdGenero());
        }else{

            canciones = cancionRepository.filtrarPorGenero(filtro.getIdGenero());
        }
        //Recopilatorio DTO se vuelve a hacer, pero entiendo que filtro también, que tiene que volver a ser pasado vacío siempre
        //FiltroGeneroDTO filtroVirgen = new FiltroGeneroDTO();
        //Por lo visto no hace falta crear un filtro nuevo, lo que tiene sentido, porque voy a sobreescribir las cosas, así que no tiene por qué estar vacío
        model.addAttribute("filtro", filtro);
        RecopilatorioDTO recopilatorioDTO = new RecopilatorioDTO();
        List<Genero> listaGeneros = generoRepository.findAll();
        model.addAttribute("generos", listaGeneros);
        model.addAttribute("recopilatorioDTO", recopilatorioDTO);

        model.addAttribute("canciones", canciones);
        return "añadirAlbumRecopilatorio";
    }

    @GetMapping("/añadirAlbum")
    public String addAlbum(Model model) {


        return this.addAlbumConFiltro(model, null);
    }

    @PostMapping("/crearRecopilatorio")
    public String crearRecopilatorio(Model model, @ModelAttribute RecopilatorioDTO recopilatorioDTO){
        //Aquí es donde vamos a gestionar todas las entidades
        System.out.println(recopilatorioDTO.getIdCancionesRecopilatorio());
        System.out.println(recopilatorioDTO.getNombreRecopilatorio());
        System.out.println(recopilatorioDTO.getNombreAlbum());

        //Tengo que crear un artista con el nombre
        Artista artistaRecopilatorio = new Artista();
        artistaRecopilatorio.setArtistaName(recopilatorioDTO.getNombreRecopilatorio());

        Album albumRecopilatorio = new Album();
        albumRecopilatorio.setAlbumName(recopilatorioDTO.getNombreAlbum());
        albumRecopilatorio.setDateReleased(Date.valueOf(LocalDate.now()));
        //El album necesita un artistaID necesariamente, para obtenerlo vamos a hacer save en el repository del artista para asignarle un id
        artistaRepository.save(artistaRecopilatorio);
        //Aquí la entidad ya tiene asignado un id


        albumRecopilatorio.setArtistaId(artistaRecopilatorio);

        List<Album> albumesArtistaRecopilatorio = new ArrayList<>();
        albumesArtistaRecopilatorio.add(albumRecopilatorio);
        artistaRecopilatorio.setAlbumList(albumesArtistaRecopilatorio);
        //De esta manera ya tenemos incializados los atributos obligatorios y las relaciones entre álbum y artista
        albumRepository.save(albumRecopilatorio);
        artistaRepository.save(artistaRecopilatorio);

        //Hacemos esto para que las canciones puedan tener un albumId asociado que asigna Spring al hace save
        //ahora vamos con las canciones
        List<Cancion> cancionesDelRecopilatorio = new ArrayList<>();
        for(Cancion cancionACopiar : cancionRepository.findAllById(recopilatorioDTO.getIdCancionesRecopilatorio())){
            Cancion cancionAAñadir = new Cancion();
            cancionAAñadir.setAlbumId(albumRecopilatorio);
            cancionAAñadir.setCancionName(cancionACopiar.getCancionName() + " (" + albumRecopilatorio.getAlbumName() + ")");
            cancionAAñadir.setDateReleased(cancionACopiar.getDateReleased());

            //De la manera en la que funciona esta base de datos, los artistas de una canción son los dueños de sus álbumes

            List<Artista> artistaListCancionesRecopilatorio = new ArrayList<>(cancionACopiar.getArtistaList());
            artistaListCancionesRecopilatorio.add(artistaRecopilatorio);
            cancionAAñadir.setArtistaList(artistaListCancionesRecopilatorio);
            //En el artista original también hay que incluir esta canción a colaboraciones
            cancionRepository.save(cancionAAñadir);
            List<Cancion> artistaOriginalColabos = cancionACopiar.getAlbumId().getArtistaId().getCancionList();
            artistaOriginalColabos.add(cancionAAñadir);
            artistaRepository.save(cancionACopiar.getAlbumId().getArtistaId());
            List<Artista> asda = cancionAAñadir.getArtistaList();

            //En este caso el autor va a ser el del recopilatorio y los artistasOriginales,


            //Las colabos de la canción van a ser todas las que ya tenía, más el artista del recopilatorio
            //En álbum el género no hace falta que quede definido, porque es un recopilatorio
            //Lo que nos falta es añadir, de igual manera que añadirmos el álbum a la canción, la canción a las lista del álbum
            cancionesDelRecopilatorio.add(cancionAAñadir);

        }
        //Aquí lo único que nos falta es añadir las canciones al álbum
        albumRecopilatorio.setCancionList(cancionesDelRecopilatorio);
        //Cada canción ya tiene añadido el álbum
        List<Cancion> listaALbum = albumRecopilatorio.getCancionList();

        artistaRepository.save(artistaRecopilatorio);
        albumRepository.save(albumRecopilatorio);


        //Y aquí hacemos un redirect al controlador del listar
        return "redirect:/";


    }






}





