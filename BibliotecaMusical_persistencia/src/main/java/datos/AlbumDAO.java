/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IAlbumDAO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import org.bson.types.ObjectId;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

/**
 *
 * @author pauli
 */
public class AlbumDAO implements IAlbumDAO {

    private final MongoCollection<Albumes> coleccionAlbumes;

    public AlbumDAO() {
        this.coleccionAlbumes = ConexionBD.crearConexion().getCollection("albumes", Albumes.class);
    }

    /**
     *
     * @param album
     * @throws PersistenciaException
     */
    @Override
    public void insertarAlbum(Albumes album) throws PersistenciaException {
        try {
            if (album == null || album.getNombre() == null || album.getFechaLanzamiento() == null) {
                throw new PersistenciaException("El álbum o sus campos obligatorios no pueden ser nulos.");
            }
            this.coleccionAlbumes.insertOne(album);
        } catch (MongoException e) {
            throw new PersistenciaException("Error al insertar el álbum: " + e.getMessage());
        }
    }

    /**
     *
     * @param nombre
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorNombre(String nombre) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.regex("nombre", Pattern.compile(nombre, Pattern.CASE_INSENSITIVE)))
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por nombre: " + e.getMessage());
        }
    }

    /**
     *
     * @param genero
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorGenero(String genero) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.in("genero", genero)).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por género: " + e.getMessage());
        }
    }

    /**
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorFechaLanzamiento(Date fechaInicio, Date fechaFin) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.and(
                    Filters.gte("fechaLanzamiento", fechaInicio),
                    Filters.lte("fechaLanzamiento", fechaFin)
            )).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por rango de fechas: " + e.getMessage());
        }
    }

    /**
     *
     * @param artistaId
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<Albumes> buscarPorArtista(ObjectId artistaId) throws PersistenciaException {
        try {
            return coleccionAlbumes.find(Filters.eq("artistaId", artistaId)).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new PersistenciaException("Error al buscar álbumes por artista: " + e.getMessage());
        }
    }

    /**
     *
     * @param albumId
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<DetallesCancion> obtenerCancionesDeAlbum(ObjectId albumId) throws PersistenciaException {
        try {
            Albumes album = coleccionAlbumes.find(Filters.eq("_id", albumId)).first();
            if (album == null) {
                throw new PersistenciaException("No se encontró el álbum con el ID proporcionado.");
            }
            return album.getDetallesCanciones();
        } catch (MongoException e) {
            throw new PersistenciaException("Error al obtener canciones del álbum: " + e.getMessage());
        }
    }

    @Override
    public List<Albumes> obtenerCincoAlbumes() throws PersistenciaException {
        try {
            // Consulta para obtener 6 artistas
            return this.coleccionAlbumes.find()
                    .limit(5) // Limita el resultado a 6 documentos
                    .into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener 6 artistas: " + e.getMessage());
        }
    }

    @Override
    public List<Albumes> obtenerAlbumes() throws PersistenciaException {
        try {
            // Consulta para obtener 6 artistas
            return this.coleccionAlbumes.find()
                    .into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener 6 artistas: " + e.getMessage());
        }
    }

    @Override
    public List<DetallesCancion> obtenerCancionesDeAlbumes() throws PersistenciaException {
        try {
            // Consulta para obtener los álbumes
            List<Albumes> albumes = this.coleccionAlbumes.find()
                    .into(new ArrayList<>());

            // Lista para almacenar las canciones
            List<DetallesCancion> canciones = new ArrayList<>();

            // Itera sobre cada álbum y agrega las canciones a la lista
            for (Albumes album : albumes) {
                List<DetallesCancion> detallesCanciones = album.getDetallesCanciones();
                if (detallesCanciones != null) {
                    canciones.addAll(detallesCanciones);
                }
            }

            // Devuelve la lista de todas las canciones
            return canciones;

        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener las canciones de los álbumes: " + e.getMessage());
        }
    }

    public List<DetallesCancion> crearCanciones(List<String[]> listaCanciones, ObjectId idAlbum, Imagen imagenPortada) {
        List<DetallesCancion> canciones = new ArrayList<>();
        for (String[] cancion : listaCanciones) {
            DetallesCancion detalleCancion = new DetallesCancion();
            detalleCancion.setIdReferenciaAlbum(idAlbum);
            detalleCancion.setTitulo(cancion[0]);
            detalleCancion.setFotoAlbum(imagenPortada);

            // Convertir duración de string a entero (segundos)
            String[] duracion = cancion[1].split(":");
            int duracionSegundos = Integer.parseInt(duracion[0]) * 60 + Integer.parseInt(duracion[1]);
            detalleCancion.setDuracion(duracionSegundos);

            canciones.add(detalleCancion);
        }
        return canciones;
    }

    public List<DetallesCancion> buscarCancionesPorNombre(String nombre) throws PersistenciaException {
        try {
            // Buscar todos los álbumes
            List<Albumes> albumes = coleccionAlbumes.find().into(new ArrayList<>());

            // Lista para almacenar las canciones que coincidan con el nombre
            List<DetallesCancion> cancionesEncontradas = new ArrayList<>();

            for (Albumes album : albumes) {
                for (DetallesCancion cancion : album.getDetallesCanciones()) {
                    if (cancion.getTitulo().toLowerCase().contains(nombre.toLowerCase())) {
                        cancionesEncontradas.add(cancion);
                    }
                }
            }

            return cancionesEncontradas;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar canciones por nombre: " + e.getMessage());
        }
    }

    @Override
    public List<Albumes> crearAlbumesParaArtista(Artistas artista) {
        List<Albumes> albumes = new ArrayList<>();

        // Creación de álbumes para diferentes artistas
        switch (artista.getNombre()) {
            case "Coldplay":
                albumes.add(crearAlbumColdplay1(artista));
                albumes.add(crearAlbumColdplay2(artista));
                albumes.add(crearAlbumColdplay3(artista));
                albumes.add(crearAlbumColdplay4(artista));
                break;
            case "Arctic Monkeys":
                albumes.add(crearAlbumArcticMonkeys1(artista));
                albumes.add(crearAlbumArcticMonkeys2(artista));
                albumes.add(crearAlbumArcticMonkeys3(artista));
                break;
            case "The Beatles":
                albumes.add(crearAlbumTheBeatles1(artista));
                albumes.add(crearAlbumTheBeatles2(artista));
                albumes.add(crearAlbumTheBeatles3(artista));
                break;
            case "Green Day":
                albumes.add(crearAlbumGreenDay1(artista));
                albumes.add(crearAlbumGreenDay2(artista));
                albumes.add(crearAlbumGreenDay3(artista));
                break;
            case "Twenty One Pilots":
                albumes.add(crearAlbumTwentyOnePilots1(artista));
                albumes.add(crearAlbumTwentyOnePilots2(artista));
                albumes.add(crearAlbumTwentyOnePilots3(artista));
                break;

            case "Queen":
                albumes.add(crearAlbumQueen1(artista));
                albumes.add(crearAlbumQueen2(artista));
                albumes.add(crearAlbumQueen3(artista));
                break;

            case "The Rolling Stones":
                albumes.add(crearAlbumRollingStones1(artista));
                albumes.add(crearAlbumRollingStones2(artista));
                albumes.add(crearAlbumRollingStones3(artista));
                break;

            case "Nirvana":
                albumes.add(crearAlbumNirvana1(artista));
                albumes.add(crearAlbumNirvana2(artista));
                albumes.add(crearAlbumNirvana3(artista));
                break;

            case "Red Hot Chili Peppers":
                albumes.add(crearAlbumRedHotChiliPeppers1(artista));
                albumes.add(crearAlbumRedHotChiliPeppers2(artista));
                albumes.add(crearAlbumRedHotChiliPeppers3(artista));
                break;

            case "Imagine Dragons":
                albumes.add(crearAlbumImagineDragons1(artista));
                albumes.add(crearAlbumImagineDragons2(artista));
                albumes.add(crearAlbumImagineDragons3(artista));
                break;

            case "One Republic":
                albumes.add(crearAlbumOneRepublic1(artista));
                albumes.add(crearAlbumOneRepublic2(artista));
                albumes.add(crearAlbumOneRepublic3(artista));
                break;

            case "Maroon5":
                albumes.add(crearAlbumMaroon5_1(artista));
                albumes.add(crearAlbumMaroon5_2(artista));
                albumes.add(crearAlbumMaroon5_3(artista));
                break;

            case "Morat":
                albumes.add(crearAlbumMorat1(artista));
                albumes.add(crearAlbumMorat2(artista));
                albumes.add(crearAlbumMorat3(artista));
                break;

            case "Mana":
                albumes.add(crearAlbumMana1(artista));
                albumes.add(crearAlbumMana2(artista));
                albumes.add(crearAlbumMana3(artista));
                break;

            case "Aventura":
                albumes.add(crearAlbumAventura1(artista));
                albumes.add(crearAlbumAventura2(artista));
                albumes.add(crearAlbumAventura3(artista));
                break;

            case "Los Angeles Azules":
                albumes.add(crearAlbumLosAngelesAzules1(artista));
                albumes.add(crearAlbumLosAngelesAzules2(artista));
                albumes.add(crearAlbumLosAngelesAzules3(artista));
                break;

            case "Enanitos Verdes":
                albumes.add(crearAlbumEnanitosVerdes1(artista));
                albumes.add(crearAlbumEnanitosVerdes2(artista));
                albumes.add(crearAlbumEnanitosVerdes3(artista));
                break;

            case "Caifanes":
                albumes.add(crearAlbumCaifanes1(artista));
                albumes.add(crearAlbumCaifanes2(artista));
                albumes.add(crearAlbumCaifanes3(artista));
                break;

            case "Panda":
                albumes.add(crearAlbumPanda1(artista));
                albumes.add(crearAlbumPanda2(artista));
                albumes.add(crearAlbumPanda3(artista));
                break;

            case "Allison":
                albumes.add(crearAlbumAllison1(artista));
                albumes.add(crearAlbumAllison2(artista));
                albumes.add(crearAlbumAllison3(artista));
                break;

            /////////////////////////////////////////////SOLISTAS**********
            case "Bad Bunny":
                albumes.add(crearAlbumBadBunny1(artista));
                albumes.add(crearAlbumBadBunny2(artista));
                albumes.add(crearAlbumBadBunny3(artista));
                break;

            case "DuaLipa":
                albumes.add(crearAlbumDuaLipa1(artista));
                albumes.add(crearAlbumDuaLipa2(artista));
                albumes.add(crearAlbumDuaLipa3(artista));
                break;

            case "Taylor Swift":
                albumes.add(crearAlbumTaylorSwift1(artista));
                albumes.add(crearAlbumTaylorSwift2(artista));
                albumes.add(crearAlbumTaylorSwift3(artista));
                break;

            case "The Weeknd":
                albumes.add(crearAlbumTheWeeknd1(artista));
                albumes.add(crearAlbumTheWeeknd2(artista));
                albumes.add(crearAlbumTheWeeknd3(artista));
                break;

            case "Billie Eilish":
                albumes.add(crearAlbumBillieEilish1(artista));
                albumes.add(crearAlbumBillieEilish2(artista));
                albumes.add(crearAlbumBillieEilish3(artista));
                break;

            case "Harry Styles":
                albumes.add(crearAlbumHarryStyles1(artista));
                albumes.add(crearAlbumHarryStyles2(artista));
                albumes.add(crearAlbumHarryStyles3(artista));
                break;

            case "Ed Sheeran":
                albumes.add(crearAlbumEdSheeran1(artista));
                albumes.add(crearAlbumEdSheeran2(artista));
                albumes.add(crearAlbumEdSheeran3(artista));
                break;

            case "Adele":
                albumes.add(crearAlbumAdele1(artista));
                albumes.add(crearAlbumAdele2(artista));
                albumes.add(crearAlbumAdele3(artista));
                break;

            case "Rosalia":
                albumes.add(crearAlbumRosalia1(artista));
                albumes.add(crearAlbumRosalia2(artista));
                albumes.add(crearAlbumRosalia3(artista));
                break;

            case "Shakira":
                albumes.add(crearAlbumShakira1(artista));
                albumes.add(crearAlbumShakira2(artista));
                albumes.add(crearAlbumShakira3(artista));
                break;

            case "Karol G":
                albumes.add(crearAlbumKarolG1(artista));
                albumes.add(crearAlbumKarolG2(artista));
                albumes.add(crearAlbumKarolG3(artista));
                break;

            case "Olivia Rodrigo":
                albumes.add(crearAlbumOliviaRodrigo1(artista));
                albumes.add(crearAlbumOliviaRodrigo2(artista));
                albumes.add(crearAlbumOliviaRodrigo3(artista));
                break;

            case "Bruno Mars":
                albumes.add(crearAlbumBrunoMars1(artista));
                albumes.add(crearAlbumBrunoMars2(artista));
                albumes.add(crearAlbumBrunoMars3(artista));
                break;

            case "Ariana Grande":
                albumes.add(crearAlbumArianaGrande1(artista));
                albumes.add(crearAlbumArianaGrande2(artista));
                albumes.add(crearAlbumArianaGrande3(artista));
                break;

            case "J Balvin":
                albumes.add(crearAlbumJBalvin1(artista));
                albumes.add(crearAlbumJBalvin2(artista));
                albumes.add(crearAlbumJBalvin3(artista));
                break;

            case "Elvis Presley":
                albumes.add(crearAlbumElvis1(artista));
                albumes.add(crearAlbumElvis2(artista));
                albumes.add(crearAlbumElvis3(artista));
                break;

            case "Bob Dylan":
                albumes.add(crearAlbumBobDylan1(artista));
                albumes.add(crearAlbumBobDylan2(artista));
                albumes.add(crearAlbumBobDylan3(artista));
                break;

            case "Janis Joplin":
                albumes.add(crearAlbumJanisJoplin1(artista));
                albumes.add(crearAlbumJanisJoplin2(artista));
                albumes.add(crearAlbumJanisJoplin3(artista));
                break;

            case "Jimi Hendrix":
                albumes.add(crearAlbumJimiHendrix1(artista));
                albumes.add(crearAlbumJimiHendrix2(artista));
                albumes.add(crearAlbumJimiHendrix3(artista));
                break;

            case "David Bowie":
                albumes.add(crearAlbumDavidBowie1(artista));
                albumes.add(crearAlbumDavidBowie2(artista));
                albumes.add(crearAlbumDavidBowie3(artista));
                break;

            case "Prince":
                albumes.add(crearAlbumPrince1(artista));
                albumes.add(crearAlbumPrince2(artista));
                albumes.add(crearAlbumPrince3(artista));
                break;

            case "Freddie Mercury":
                albumes.add(crearAlbumFreddieMercury1(artista));
                albumes.add(crearAlbumFreddieMercury2(artista));
                albumes.add(crearAlbumFreddieMercury3(artista));
                break;

            case "Michael Jackson":
                albumes.add(crearAlbumMichaelJackson1(artista));
                albumes.add(crearAlbumMichaelJackson2(artista));
                albumes.add(crearAlbumMichaelJackson3(artista));
                break;

            case "Amy Winehouse":
                albumes.add(crearAlbumAmyWinehouse1(artista));
                albumes.add(crearAlbumAmyWinehouse2(artista));
                albumes.add(crearAlbumAmyWinehouse3(artista));
                break;

            case "Chris Stapleton":
                albumes.add(crearAlbumChrisStapleton1(artista));
                albumes.add(crearAlbumChrisStapleton2(artista));
                albumes.add(crearAlbumChrisStapleton3(artista));
                break;

            case "Sam Smith":
                albumes.add(crearAlbumSamSmith1(artista));
                albumes.add(crearAlbumSamSmith2(artista));
                albumes.add(crearAlbumSamSmith3(artista));
                break;

            case "Hozier":
                albumes.add(crearAlbumHozier1(artista));
                albumes.add(crearAlbumHozier2(artista));
                albumes.add(crearAlbumHozier3(artista));
                break;

            case "Lana Del Rey":
                albumes.add(crearAlbumLanaDelRey1(artista));
                albumes.add(crearAlbumLanaDelRey2(artista));
                albumes.add(crearAlbumLanaDelRey3(artista));
                break;

            case "Post Malone":
                albumes.add(crearAlbumPostMalone1(artista));
                albumes.add(crearAlbumPostMalone2(artista));
                albumes.add(crearAlbumPostMalone3(artista));
                break;

            case "Shawn Mendes":
                albumes.add(crearAlbumShawnMendes1(artista));
                albumes.add(crearAlbumShawnMendes2(artista));
                albumes.add(crearAlbumShawnMendes3(artista));
                break;

            case "Miley Cyrus":
                albumes.add(crearAlbumMileyCyrus1(artista));
                albumes.add(crearAlbumMileyCyrus2(artista));
                albumes.add(crearAlbumMileyCyrus3(artista));
                break;

            case "Daddy Yankee":
                albumes.add(crearAlbumDaddyYankee1(artista));
                albumes.add(crearAlbumDaddyYankee2(artista));
                albumes.add(crearAlbumDaddyYankee3(artista));
                break;

            case "Ozuna":
                albumes.add(crearAlbumOzuna1(artista));
                albumes.add(crearAlbumOzuna2(artista));
                albumes.add(crearAlbumOzuna3(artista));
                break;

            case "Anuel AA":
                albumes.add(crearAlbumAnuelAA1(artista));
                albumes.add(crearAlbumAnuelAA2(artista));
                albumes.add(crearAlbumAnuelAA3(artista));
                break;

            case "Maluma":
                albumes.add(crearAlbumMaluma1(artista));
                albumes.add(crearAlbumMaluma2(artista));
                albumes.add(crearAlbumMaluma3(artista));
                break;

            case "Natti Natasha":
                albumes.add(crearAlbumNattiNatasha1(artista));
                albumes.add(crearAlbumNattiNatasha2(artista));
                albumes.add(crearAlbumNattiNatasha3(artista));
                break;

            case "Becky G":
                albumes.add(crearAlbumBeckyG1(artista));
                albumes.add(crearAlbumBeckyG2(artista));
                break;

            case "Farruko":
                albumes.add(crearAlbumFarruko1(artista));
                albumes.add(crearAlbumFarruko2(artista));
                albumes.add(crearAlbumFarruko3(artista));
                break;

            case "Sech":
                albumes.add(crearAlbumSech1(artista));
                albumes.add(crearAlbumSech2(artista));
                albumes.add(crearAlbumSech3(artista));
                break;

            case "Danny Ocean":
                albumes.add(crearAlbumDannyOcean1(artista));
                albumes.add(crearAlbumDannyOcean2(artista));
                break;

        }

        return albumes;
    }

    private Albumes crearAlbumColdplay1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/vivaLaVida.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("vivaLaVida", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Viva La Vida or Death and All His Friends");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2008-06-12"));
            album.setGenero(Arrays.asList("Rock Alternativo", "Indie Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Life in Technicolor", "3:28"},
                            new String[]{"Cemeteries of London", "3:21"},
                            new String[]{"Lost!", "3:55"},
                            new String[]{"Viva La Vida", "4:01"},
                            new String[]{"Violet Hill", "3:42"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Coldplay", ex);
            return null;
        }
    }

    private Albumes crearAlbumColdplay2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/parachutes.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("parachutes_coldplay", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Parachutes");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2000-07-10"));
            album.setGenero(Arrays.asList("Rock Alternativo"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Yellow", "4:29"},
                            new String[]{"Shiver", "5:03"},
                            new String[]{"Trouble", "4:30"},
                            new String[]{"Don't Panic", "2:17"},
                            new String[]{"High Speed", "4:14"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Coldplay", ex);
            return null;
        }
    }

    private Albumes crearAlbumColdplay3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/arushofbloodtothehead.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("arushofblood_coldplay", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("A Rush of Blood to the Head");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2002-08-26"));
            album.setGenero(Arrays.asList("Rock Alternativo"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Clocks", "5:07"},
                            new String[]{"The Scientist", "5:09"},
                            new String[]{"In My Place", "3:48"},
                            new String[]{"God Put a Smile Upon Your Face", "4:57"},
                            new String[]{"Green Eyes", "3:42"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Coldplay", ex);
            return null;
        }
    }

    private Albumes crearAlbumColdplay4(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/xandY.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("xandY_coldplay", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("X&Y");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2005-06-06"));
            album.setGenero(Arrays.asList("Rock Alternativo"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Fix You", "4:55"},
                            new String[]{"Speed of Sound", "4:48"},
                            new String[]{"Talk", "5:11"},
                            new String[]{"The Hardest Part", "4:25"},
                            new String[]{"Swallowed in the Sea", "3:58"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Coldplay", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumArcticMonkeys1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/am.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("am_artic", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("AM");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2013-09-09"));
            album.setGenero(Arrays.asList("Rock Indie"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Do I Wanna Know?", "4:32"},
                            new String[]{"R U Mine?", "3:21"},
                            new String[]{"Arabella", "3:27"},
                            new String[]{"I Want It All", "3:05"},
                            new String[]{"No. 1 Party Anthem", "3:58"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Arctic Monkeys", ex);
            return null;
        }
    }

    private Albumes crearAlbumArcticMonkeys2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/whateverpeople.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("whateverpeople_arcticmonkeys", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Whatever People Say I Am, That's What I'm Not");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2006-01-23"));
            album.setGenero(Arrays.asList("Rock Indie"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"The View from the Afternoon", "3:38"},
                            new String[]{"I Bet You Look Good on the Dancefloor", "2:53"},
                            new String[]{"Fake Tales of San Francisco", "2:57"},
                            new String[]{"Dancing Shoes", "2:21"},
                            new String[]{"Mardy Bum", "2:55"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Arctic Monkeys", ex);
            return null;
        }
    }

    private Albumes crearAlbumArcticMonkeys3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/favouritworstnightmare.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("favouriteworst_arcticmonkeys", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Favourite Worst Nightmare");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2007-04-23"));
            album.setGenero(Arrays.asList("Rock Indie"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Brianstorm", "2:50"},
                            new String[]{"Teddy Picker", "2:43"},
                            new String[]{"Fluorescent Adolescent", "2:57"},
                            new String[]{"Do Me a Favour", "3:27"},
                            new String[]{"505", "4:13"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Arctic Monkeys", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumTheBeatles1(Artistas artista) {
        try {
            File imagenFile = new File("./images/abbey_road.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("abbey_road", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Abbey Road");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1969-09-26"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Come Together", "4:19"},
                            new String[]{"Something", "3:02"},
                            new String[]{"Maxwell's Silver Hammer", "3:27"},
                            new String[]{"Oh! Darling", "3:26"},
                            new String[]{"Octopus's Garden", "2:51"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Beatles", ex);
            return null;
        }
    }

    private Albumes crearAlbumTheBeatles2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/sgtpepper.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("sgtpepper_thebeatles", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Sgt. Pepper's Lonely Hearts Club Band");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1967-06-01"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Sgt. Pepper's Lonely Hearts Club Band", "2:02"},
                            new String[]{"With a Little Help from My Friends", "2:44"},
                            new String[]{"Lucy in the Sky with Diamonds", "3:28"},
                            new String[]{"Getting Better", "2:48"},
                            new String[]{"A Day in the Life", "5:38"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Beatles", ex);
            return null;
        }
    }

    private Albumes crearAlbumTheBeatles3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/letitbe.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("letitbe_thebeatles", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Let It Be");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1970-05-08"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Two of Us", "3:36"},
                            new String[]{"Let It Be", "4:03"},
                            new String[]{"Across the Universe", "3:48"},
                            new String[]{"The Long and Winding Road", "3:38"},
                            new String[]{"Get Back", "3:09"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Beatles", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumGreenDay1(Artistas artista) {
        // Álbum "American Idiot"
        try {
            File imagenFile = new File("./images/american_idiot.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("american_idiot", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("American Idiot");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2004-09-21"));
            album.setGenero(Arrays.asList("Punk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"American Idiot", "2:54"},
                            new String[]{"Jesus of Suburbia", "9:08"},
                            new String[]{"Holiday", "3:51"},
                            new String[]{"Boulevard of Broken Dreams", "4:20"},
                            new String[]{"Are We the Waiting", "2:42"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Green Day", ex);
            return null;
        }
    }

    private Albumes crearAlbumGreenDay2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dookie.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("dookie_greenday", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Dookie");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1994-02-01"));
            album.setGenero(Arrays.asList("Punk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Burnout", "2:07"},
                            new String[]{"Basket Case", "3:03"},
                            new String[]{"When I Come Around", "2:58"},
                            new String[]{"Longview", "3:59"},
                            new String[]{"She", "2:14"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Green Day", ex);
            return null;
        }
    }

    private Albumes crearAlbumGreenDay3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/revolutionradio.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("revolutionradio_greenday", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Revolution Radio");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2016-10-07"));
            album.setGenero(Arrays.asList("Punk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Somewhere Now", "4:08"},
                            new String[]{"Bang Bang", "3:25"},
                            new String[]{"Revolution Radio", "3:00"},
                            new String[]{"Still Breathing", "3:45"},
                            new String[]{"Youngblood", "2:32"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Green Day", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumTwentyOnePilots1(Artistas artista) {
        // Álbum "Blurryface"
        try {
            File imagenFile = new File("./images/blurryface.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("blurryface", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Blurryface");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-19"));
            album.setGenero(Arrays.asList("Alternative Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Heavydirtysoul", "3:55"},
                            new String[]{"Stressed Out", "3:22"},
                            new String[]{"Ride", "3:34"},
                            new String[]{"Fairly Local", "3:28"},
                            new String[]{"Tear in My Heart", "3:09"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Twenty One Pilots", ex);
            return null;
        }
    }

    private Albumes crearAlbumTwentyOnePilots2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/trench.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("trench_top", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Trench");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-05"));
            album.setGenero(Arrays.asList("Alternative Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Jumpsuit", "3:58"},
                            new String[]{"Levitate", "2:25"},
                            new String[]{"Chlorine", "5:24"},
                            new String[]{"My Blood", "3:49"},
                            new String[]{"Bandito", "5:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Twenty One Pilots", ex);
            return null;
        }
    }

    private Albumes crearAlbumTwentyOnePilots3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/scaledandicy.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("scaledandicy_top", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Scaled and Icy");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-21"));
            album.setGenero(Arrays.asList("Alternative Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Shy Away", "2:55"},
                            new String[]{"Choker", "3:43"},
                            new String[]{"Saturday", "2:52"},
                            new String[]{"Formidable", "2:57"},
                            new String[]{"Good Day", "3:24"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Twenty One Pilots", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumQueen1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/anightattheopera.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("anightattheopera_queen", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("A Night at the Opera");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1975-11-21"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Bohemian Rhapsody", "5:55"},
                            new String[]{"Love of My Life", "3:39"},
                            new String[]{"You're My Best Friend", "2:50"},
                            new String[]{"Death on Two Legs", "3:43"},
                            new String[]{"Seaside Rendezvous", "2:13"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Queen", ex);
            return null;
        }
    }

    private Albumes crearAlbumQueen2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/newsoftheworld.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("news_of_the_world_queen", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("News of the World");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1977-10-28"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"We Will Rock You", "2:02"},
                            new String[]{"We Are the Champions", "2:59"},
                            new String[]{"Spread Your Wings", "4:34"},
                            new String[]{"Sheer Heart Attack", "3:25"},
                            new String[]{"It's Late", "6:26"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Queen", ex);
            return null;
        }
    }

    private Albumes crearAlbumQueen3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/innuendo.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("innuendo_queen", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Innuendo");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1991-02-04"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Innuendo", "6:31"},
                            new String[]{"These Are the Days of Our Lives", "4:15"},
                            new String[]{"The Show Must Go On", "4:37"},
                            new String[]{"I'm Going Slightly Mad", "4:22"},
                            new String[]{"Headlong", "4:38"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Queen", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumRollingStones1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/letitbleed.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("let_it_bleed_rollingstones", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Let It Bleed");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1969-12-05"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Gimme Shelter", "4:32"},
                            new String[]{"Love in Vain", "4:19"},
                            new String[]{"Country Honk", "3:07"},
                            new String[]{"Live with Me", "3:33"},
                            new String[]{"You Can't Always Get What You Want", "7:28"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Rolling Stones", ex);
            return null;
        }
    }

    private Albumes crearAlbumRollingStones2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/stickyfingers.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("sticky_fingers_rollingstones", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Sticky Fingers");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1971-04-23"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Brown Sugar", "3:49"},
                            new String[]{"Wild Horses", "5:42"},
                            new String[]{"Can't You Hear Me Knocking", "7:15"},
                            new String[]{"Sway", "3:51"},
                            new String[]{"Bitch", "3:36"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Rolling Stones", ex);
            return null;
        }
    }

    private Albumes crearAlbumRollingStones3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/exileonmainst.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("exile_on_main_st_rollingstones", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Exile on Main St.");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1972-05-12"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Rocks Off", "4:31"},
                            new String[]{"Rip This Joint", "2:22"},
                            new String[]{"Tumbling Dice", "3:45"},
                            new String[]{"Sweet Virginia", "4:25"},
                            new String[]{"Loving Cup", "4:23"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Rolling Stones", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumNirvana1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/nevermind.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("nevermind_nirvana", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Nevermind");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1991-09-24"));
            album.setGenero(Arrays.asList("Grunge"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Smells Like Teen Spirit", "5:01"},
                            new String[]{"In Bloom", "4:14"},
                            new String[]{"Come as You Are", "3:39"},
                            new String[]{"Breed", "3:39"},
                            new String[]{"Lithium", "4:17"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Nirvana", ex);
            return null;
        }
    }

    private Albumes crearAlbumNirvana2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/inutero.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("in_utero_nirvana", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("In Utero");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1993-09-21"));
            album.setGenero(Arrays.asList("Grunge"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Serve the Servants", "3:36"},
                            new String[]{"Heart-Shaped Box", "4:41"},
                            new String[]{"Rape Me", "2:49"},
                            new String[]{"Frances Farmer Will Have Her Revenge on Seattle", "4:10"},
                            new String[]{"Dumb", "2:34"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Nirvana", ex);
            return null;
        }
    }

    private Albumes crearAlbumNirvana3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bleach.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("bleach_nirvana", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Bleach");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1989-06-15"));
            album.setGenero(Arrays.asList("Grunge"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Blew", "2:57"},
                            new String[]{"Floyd the Barber", "2:18"},
                            new String[]{"About a Girl", "2:48"},
                            new String[]{"School", "2:42"},
                            new String[]{"Love Buzz", "3:35"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Nirvana", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumRedHotChiliPeppers1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bloodsugarsexmagik.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("blood_sugar_sex_magik", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Blood Sugar Sex Magik");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1991-09-24"));
            album.setGenero(Arrays.asList("Funk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Give It Away", "4:43"},
                            new String[]{"Under the Bridge", "4:24"},
                            new String[]{"Suck My Kiss", "3:37"},
                            new String[]{"Breaking the Girl", "4:55"},
                            new String[]{"If You Have to Ask", "3:37"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Red Hot Chili Peppers", ex);
            return null;
        }
    }

    private Albumes crearAlbumRedHotChiliPeppers2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/californication.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("californication", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Californication");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1999-06-08"));
            album.setGenero(Arrays.asList("Funk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Around the World", "3:58"},
                            new String[]{"Scar Tissue", "3:37"},
                            new String[]{"Otherside", "4:15"},
                            new String[]{"Californication", "5:21"},
                            new String[]{"Parallel Universe", "4:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Red Hot Chili Peppers", ex);
            return null;
        }
    }

    private Albumes crearAlbumRedHotChiliPeppers3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bytheway.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("by_the_way", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("By the Way");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2002-07-09"));
            album.setGenero(Arrays.asList("Funk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"By the Way", "3:37"},
                            new String[]{"Can't Stop", "4:29"},
                            new String[]{"Universally Speaking", "4:19"},
                            new String[]{"The Zephyr Song", "3:52"},
                            new String[]{"Throw Away Your Television", "3:45"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Red Hot Chili Peppers", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumImagineDragons1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/nightvisions.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("night_visions", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Night Visions");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2012-09-04"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Radioactive", "3:06"},
                            new String[]{"Demons", "2:57"},
                            new String[]{"It's Time", "3:59"},
                            new String[]{"On Top of the World", "3:12"},
                            new String[]{"Bleeding Out", "3:43"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Imagine Dragons", ex);
            return null;
        }
    }

    private Albumes crearAlbumImagineDragons2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/smokeandmirrors.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("smoke_and_mirrors", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Smoke + Mirrors");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-17"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Shots", "3:52"},
                            new String[]{"Gold", "3:36"},
                            new String[]{"I Bet My Life", "3:14"},
                            new String[]{"Polaroid", "3:51"},
                            new String[]{"Friction", "3:21"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Imagine Dragons", ex);
            return null;
        }
    }

    private Albumes crearAlbumImagineDragons3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/evolve.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("evolve", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Evolve");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-06-23"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Believer", "3:24"},
                            new String[]{"Thunder", "3:07"},
                            new String[]{"Whatever It Takes", "3:21"},
                            new String[]{"Walking the Wire", "3:53"},
                            new String[]{"Rise Up", "3:51"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Imagine Dragons", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumOneRepublic1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dreamingoutloud.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("dreaming_out_loud", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Dreaming Out Loud");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2007-11-20"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Apologize", "3:28"},
                            new String[]{"Stop and Stare", "3:43"},
                            new String[]{"Say (All I Need)", "3:50"},
                            new String[]{"Mercy", "4:01"},
                            new String[]{"Come Home", "4:23"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de OneRepublic", ex);
            return null;
        }
    }

    private Albumes crearAlbumOneRepublic2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/nativ.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("native", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Native");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2013-03-26"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Counting Stars", "4:17"},
                            new String[]{"If I Lose Myself", "4:01"},
                            new String[]{"Feel Again", "3:05"},
                            new String[]{"Something I Need", "4:01"},
                            new String[]{"I Lived", "3:54"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de OneRepublic", ex);
            return null;
        }
    }

    private Albumes crearAlbumOneRepublic3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/human.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("human", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Human");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-27"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Run", "2:49"},
                            new String[]{"Someday", "3:22"},
                            new String[]{"Rescue Me", "2:39"},
                            new String[]{"Better Days", "2:24"},
                            new String[]{"Take Care of You", "3:36"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de OneRepublic", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumMaroon5_1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/songsaboutjane.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("songs_about_jane", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Songs About Jane");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2002-06-25"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Harder to Breathe", "2:55"},
                            new String[]{"This Love", "3:26"},
                            new String[]{"She Will Be Loved", "4:17"},
                            new String[]{"Sunday Morning", "4:06"},
                            new String[]{"Sweetest Goodbye", "4:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maroon 5", ex);
            return null;
        }
    }

    private Albumes crearAlbumMaroon5_2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/itwontbesoonbeforelong.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("it_wont_be_soon", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("It Won't Be Soon Before Long");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2007-05-16"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Makes Me Wonder", "3:31"},
                            new String[]{"Wake Up Call", "3:21"},
                            new String[]{"Won't Go Home Without You", "3:51"},
                            new String[]{"If I Never See Your Face Again", "3:21"},
                            new String[]{"Goodnight Goodnight", "4:03"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maroon 5", ex);
            return null;
        }
    }

    private Albumes crearAlbumMaroon5_3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/v.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("v", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("V");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2014-08-29"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Maps", "3:10"},
                            new String[]{"Animals", "3:51"},
                            new String[]{"Sugar", "3:55"},
                            new String[]{"Unkiss Me", "3:58"},
                            new String[]{"Feelings", "3:14"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maroon 5", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumMorat1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/como_te_atreves.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("como_te_atreves", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Sobre El Amor Y Sus Efectos Secundarios");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-07-17"));
            album.setGenero(Arrays.asList("Pop Latino/Folk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Cómo Te Atreves", "3:29"},
                            new String[]{"Di Que No Te Vas", "3:41"},
                            new String[]{"Cuánto Me Duele", "4:05"},
                            new String[]{"Yo Contigo, Tú Conmigo", "3:15"},
                            new String[]{"Amor Con Hielo", "3:20"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Morat", ex);
            return null;
        }
    }

    private Albumes crearAlbumMorat2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/balascuerdas.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("balas_perdidas", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Balas Perdidas");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-26"));
            album.setGenero(Arrays.asList("Pop Latino/Folk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Besos En Guerra", "3:37"},
                            new String[]{"Yo Contigo, Tú Conmigo", "3:16"},
                            new String[]{"Punto y Aparte", "3:48"},
                            new String[]{"Cuando Nadie Ve", "3:09"},
                            new String[]{"Cien Años", "3:23"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Morat", ex);
            return null;
        }
    }

    private Albumes crearAlbumMorat3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/ahorayoentiendo.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("ahora_y_si_entendemos", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("¿A Dónde Vamos?");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-07-15"));
            album.setGenero(Arrays.asList("Pop Latino/Folk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Al Aire", "3:43"},
                            new String[]{"Amor Con Hielo", "3:19"},
                            new String[]{"Primeras Veces", "3:18"},
                            new String[]{"Simplemente Pasan", "4:06"},
                            new String[]{"¿A Dónde Vamos?", "3:32"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Morat", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumMana1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dondejugaranlosninos.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("donde_jugaran_los_ninos", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("¿Dónde Jugarán los Niños?");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1992-10-27"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Vivir Sin Aire", "4:53"},
                            new String[]{"Oye Mi Amor", "4:37"},
                            new String[]{"Cómo Te Deseo", "4:18"},
                            new String[]{"De Pies a Cabeza", "4:07"},
                            new String[]{"Te Llore un Río", "5:21"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maná", ex);
            return null;
        }
    }

    private Albumes crearAlbumMana2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/suenosliquidos.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("suenos_liquidos", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Sueños Líquidos");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1997-10-14"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Hechicera", "4:39"},
                            new String[]{"En El Muelle De San Blas", "5:07"},
                            new String[]{"Clavado En Un Bar", "5:09"},
                            new String[]{"Como Dueles En Los Labios", "4:08"},
                            new String[]{"Amor Clandestino", "5:04"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maná", ex);
            return null;
        }
    }

    private Albumes crearAlbumMana3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/amardueleryamar.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("amar_dueler_y_amar", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Amar es Combatir");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2006-08-22"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Labios Compartidos", "5:02"},
                            new String[]{"Bendita Tu Luz", "4:38"},
                            new String[]{"Manda Una Señal", "4:55"},
                            new String[]{"El Rey Tiburón", "4:16"},
                            new String[]{"Volaré", "4:51"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maná", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumAventura1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/weareaventura.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("we_are_aventura", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("We Broke the Rules");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2002-07-30"));
            album.setGenero(Arrays.asList("Bachata"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Obsesión", "4:12"},
                            new String[]{"Cuando Volverás", "3:45"},
                            new String[]{"Todavía Me Amas", "4:38"},
                            new String[]{"Perdí El Control", "3:52"},
                            new String[]{"Mi Puerto Rico", "4:18"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Aventura", ex);
            return null;
        }
    }

    private Albumes crearAlbumAventura2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/lovepainandbachata.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("love_and_pain_bachata", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Love & Hate");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2003-11-18"));
            album.setGenero(Arrays.asList("Bachata"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Hermanita", "4:10"},
                            new String[]{"La Guerra", "3:57"},
                            new String[]{"Amor De Madre", "5:19"},
                            new String[]{"No Lo Perdona Dios", "4:36"},
                            new String[]{"Canta Conmigo", "3:46"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Aventura", ex);
            return null;
        }
    }

    private Albumes crearAlbumAventura3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/thelast.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("the_last_aventura", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("The Last");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2009-06-09"));
            album.setGenero(Arrays.asList("Bachata"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Dile Al Amor", "4:44"},
                            new String[]{"Por Un Segundo", "4:02"},
                            new String[]{"El Perdedor", "3:35"},
                            new String[]{"La Curita", "4:17"},
                            new String[]{"Su Veneno", "3:58"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Aventura", ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumLosAngelesAzules1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/inolvidables.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("inolvidables_angeles", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Inolvidables");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1996-07-01"));
            album.setGenero(Arrays.asList("Cumbia"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Cómo Te Voy A Olvidar", "4:07"},
                            new String[]{"El Listón De Tu Pelo", "3:32"},
                            new String[]{"Entrega De Amor", "4:18"},
                            new String[]{"Mi Niña Mujer", "3:50"},
                            new String[]{"La Cumbia Del Acordeón", "3:40"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Los Ángeles Azules", ex);
            return null;
        }
    }

    private Albumes crearAlbumLosAngelesAzules2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/comoamaneci.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("como_amaneci_angeles", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Cómo Amanecí");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2002-08-15"));
            album.setGenero(Arrays.asList("Cumbia"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Amigos Nada Más", "4:12"},
                            new String[]{"20 Rosas", "4:03"},
                            new String[]{"Te Necesito", "3:47"},
                            new String[]{"Cómo Amanecí", "4:22"},
                            new String[]{"Juventud", "3:34"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Los Ángeles Azules", ex);
            return null;
        }
    }

    private Albumes crearAlbumLosAngelesAzules3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/deplazasalasalma.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("de_plaza_angeles", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("De Plaza en Plaza");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2016-09-30"));
            album.setGenero(Arrays.asList("Cumbia"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Las Maravillas de la Vida", "3:58"},
                            new String[]{"La Cumbia del Infinito", "4:03"},
                            new String[]{"Mis Sentimientos", "4:20"},
                            new String[]{"Nunca Es Suficiente", "3:42"},
                            new String[]{"A La Cumbre", "3:54"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Los Ángeles Azules", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumEnanitosVerdes1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/suenos.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("suenos_enanitos", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Sueños");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1984-06-15"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Tus Viejas Cartas", "4:12"},
                            new String[]{"El Extraño del Pelo Largo", "3:30"},
                            new String[]{"Cada Vez Que Digo Adiós", "3:40"},
                            new String[]{"Aún Sigo Cantando", "3:50"},
                            new String[]{"Tus Sueños", "4:00"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Enanitos Verdes", ex);
            return null;
        }
    }

    private Albumes crearAlbumEnanitosVerdes2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/habitacionesextrañas.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("habitaciones_extrañas_enanitos", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Habitaciones Extrañas");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1987-11-01"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"La Muralla Verde", "3:52"},
                            new String[]{"Te Ví En Un Tren", "3:20"},
                            new String[]{"Por el Resto", "3:45"},
                            new String[]{"El Aguacero", "4:10"},
                            new String[]{"Solo Dame Otra Oportunidad", "4:22"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Enanitos Verdes", ex);
            return null;
        }
    }

    private Albumes crearAlbumEnanitosVerdes3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/iglu.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("iglu_enanitos", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Igloo");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1995-09-01"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Mi Primer Día Sin Ti", "3:48"},
                            new String[]{"Dale Pascual", "3:35"},
                            new String[]{"Eterna Soledad", "3:45"},
                            new String[]{"Amores Lejanos", "4:00"},
                            new String[]{"Camino al Sol", "4:15"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Enanitos Verdes", ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumCaifanes1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/volumenuno.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("volumenuno_caifanes", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Caifanes Volumen 1");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1988-08-28"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Matenme Porque Me Muero", "4:35"},
                            new String[]{"La Negra Tomasa", "3:32"},
                            new String[]{"Viento", "3:30"},
                            new String[]{"Amanece", "3:50"},
                            new String[]{"Cuentame Tu Vida", "4:00"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Caifanes Volumen 1", ex);
            return null;
        }
    }

    private Albumes crearAlbumCaifanes2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/volumendos.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("volumendos_caifanes", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Caifanes Volumen 2: El Diablito");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1990-06-01"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Los Dioses Ocultos", "4:35"},
                            new String[]{"Antes De Que Nos Olviden", "3:20"},
                            new String[]{"La Celula Que Explota", "4:15"},
                            new String[]{"El Negro Cósmico", "3:55"},
                            new String[]{"De Noche Todos Los Gatos Son Pardos", "4:10"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Caifanes Volumen 2", ex);
            return null;
        }
    }

    private Albumes crearAlbumCaifanes3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/el_silencio.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("elsilencio_caifanes", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("El Silencio");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1992-05-01"));
            album.setGenero(Arrays.asList("Rock en Español"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Nubes", "4:35"},
                            new String[]{"Piedra", "3:45"},
                            new String[]{"Hasta Morir", "4:20"},
                            new String[]{"No Dejes Que...", "4:15"},
                            new String[]{"Ayer Me Dijo Un Ave", "4:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Caifanes El Silencio", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumPanda1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/arrozconleche.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("arrozconleche_panda", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Arroz Con Leche");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2000-09-02"));
            album.setGenero(Arrays.asList("Rock Alternativo"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Buen Día", "3:31"},
                            new String[]{"En El Autobús", "4:20"},
                            new String[]{"Si Supieras", "4:00"},
                            new String[]{"El Monstruo Come Asteroides", "3:45"},
                            new String[]{"Muñeca", "3:50"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Panda: Arroz Con Leche", ex);
            return null;
        }
    }

    private Albumes crearAlbumPanda2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/laresistencian.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("laresistencian_panda", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("La Revancha Del Príncipe Charro");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2002-02-18"));
            album.setGenero(Arrays.asList("Rock Alternativo"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Hola", "3:20"},
                            new String[]{"Maracas", "3:45"},
                            new String[]{"Mi Huracán Llevaba Tu Nombre", "4:00"},
                            new String[]{"Cita En El Quirofano", "4:15"},
                            new String[]{"Si Esto Fuera Real", "4:10"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Panda: La Revancha Del Príncipe Charro", ex);
            return null;
        }
    }

    private Albumes crearAlbumPanda3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/paraustedes.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("paraustedes_panda", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Para Ti Con Desprecio");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2005-06-11"));
            album.setGenero(Arrays.asList("Rock Alternativo"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Cita En El Quirófano", "3:20"},
                            new String[]{"Cuando No Es Como Debiera Ser", "3:40"},
                            new String[]{"Miércoles", "4:15"},
                            new String[]{"Disculpa Los Malos Pensamientos", "3:55"},
                            new String[]{"Promesas/Decepciones", "4:00"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Panda: Para Ti Con Desprecio", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumAllison1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/allisonalbum.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("allison_selftitled", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Allison");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2006-03-01"));
            album.setGenero(Arrays.asList("Pop Punk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Frágil", "3:44"},
                            new String[]{"Aquí", "3:12"},
                            new String[]{"Me Cambió", "3:49"},
                            new String[]{"Llamado de Atención", "3:30"},
                            new String[]{"Baby Please", "3:55"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Allison: Allison", ex);
            return null;
        }
    }

    private Albumes crearAlbumAllison2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/mememories.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("mememories_allison", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Memorama");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2008-05-01"));
            album.setGenero(Arrays.asList("Pop Punk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Memorama", "3:45"},
                            new String[]{"80's", "3:18"},
                            new String[]{"Tú", "4:02"},
                            new String[]{"Amor Eterno", "3:35"},
                            new String[]{"Baby Please 2", "3:50"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Allison: Memorama", ex);
            return null;
        }
    }

    private Albumes crearAlbumAllison3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/todobien.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("todobien_allison", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Todo Está Encendido");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2012-11-01"));
            album.setGenero(Arrays.asList("Pop Punk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Rómpase el Vidrio en Caso de Emergencia", "3:40"},
                            new String[]{"Dualidad", "3:28"},
                            new String[]{"Matar o Morir", "3:33"},
                            new String[]{"La Última Canción", "4:00"},
                            new String[]{"Algo Que Decir", "3:22"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Allison: Todo Está Encendido", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumBadBunny1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/x100pre.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("x100pre_badbunny", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("X 100PRE");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-12-24"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Ni Bien Ni Mal", "3:54"},
                            new String[]{"200 MPH", "3:09"},
                            new String[]{"¿Quién Tú Eres?", "2:57"},
                            new String[]{"Caro", "3:49"},
                            new String[]{"Estamos Bien", "3:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bad Bunny: X 100PRE", ex);
            return null;
        }
    }

    private Albumes crearAlbumBadBunny2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/yhlqmdlg.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("yhlqmdlg_badbunny", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("YHLQMDLG");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-29"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Si Veo a Tu Mamá", "2:51"},
                            new String[]{"La Difícil", "2:43"},
                            new String[]{"La Santa", "3:26"},
                            new String[]{"Yo Perreo Sola", "3:13"},
                            new String[]{"Safaera", "4:56"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bad Bunny: YHLQMDLG", ex);
            return null;
        }
    }

    private Albumes crearAlbumBadBunny3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/unverano.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("unverano_badbunny", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Un Verano Sin Ti");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-06"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Moscow Mule", "4:05"},
                            new String[]{"Después de la Playa", "3:50"},
                            new String[]{"Ojitos Lindos", "4:18"},
                            new String[]{"Me Porto Bonito", "2:58"},
                            new String[]{"Tití Me Preguntó", "4:03"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bad Bunny: Un Verano Sin Ti", ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumDuaLipa1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dualipaalbum.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("dualipa_selftitled", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Dua Lipa");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-06-02"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Genesis", "3:26"},
                            new String[]{"Hotter Than Hell", "3:08"},
                            new String[]{"Be the One", "3:23"},
                            new String[]{"New Rules", "3:29"},
                            new String[]{"IDGAF", "3:38"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Dua Lipa: Dua Lipa", ex);
            return null;
        }
    }

    private Albumes crearAlbumDuaLipa2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/future_nostalgia.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("future_nostalgia", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Future Nostalgia");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-27"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Don't Start Now", "3:03"},
                            new String[]{"Physical", "3:13"},
                            new String[]{"Levitating", "3:23"},
                            new String[]{"Break My Heart", "3:41"},
                            new String[]{"Love Again", "4:18"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Dua Lipa: Future Nostalgia", ex);
            return null;
        }
    }

    private Albumes crearAlbumDuaLipa3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/club_future_nostalgia.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("club_future_nostalgia", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Club Future Nostalgia");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-08-28"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Levitating (feat. Madonna and Missy Elliott)", "4:10"},
                            new String[]{"Physical (Mark Ronson Remix)", "4:24"},
                            new String[]{"Love Is Religion (The Blessed Madonna Remix)", "4:20"},
                            new String[]{"Hallucinate (Paul Woolford Remix)", "5:06"},
                            new String[]{"Don't Start Now (Yaeji Remix)", "3:29"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Dua Lipa: Club Future Nostalgia", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumTaylorSwift1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/taylorswift_fearless.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("fearless", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Fearless");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2008-11-11"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Love Story", "3:55"},
                            new String[]{"You Belong With Me", "3:52"},
                            new String[]{"Fearless", "4:01"},
                            new String[]{"Fifteen", "4:54"},
                            new String[]{"White Horse", "3:55"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Taylor Swift: Fearless", ex);
            return null;
        }
    }

    private Albumes crearAlbumTaylorSwift2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/taylorswift_1989.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("1989", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("1989");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-27"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Blank Space", "3:51"},
                            new String[]{"Shake It Off", "3:39"},
                            new String[]{"Style", "3:51"},
                            new String[]{"Wildest Dreams", "3:40"},
                            new String[]{"Bad Blood", "3:31"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Taylor Swift: 1989", ex);
            return null;
        }
    }

    private Albumes crearAlbumTaylorSwift3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/taylorswift_folklore.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("folklore", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Folklore");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-07-24"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Cardigan", "3:59"},
                            new String[]{"The Last Great American Dynasty", "3:50"},
                            new String[]{"Exile (feat. Bon Iver)", "4:45"},
                            new String[]{"August", "4:21"},
                            new String[]{"Betty", "4:54"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Taylor Swift: Folklore", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumTheWeeknd1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/theweeknd_beautybehindmadness.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("beauty_behind_madness", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Beauty Behind the Madness");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-28"));
            album.setGenero(Arrays.asList("R&B"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Can't Feel My Face", "3:35"},
                            new String[]{"The Hills", "4:02"},
                            new String[]{"Often", "4:10"},
                            new String[]{"Earned It", "4:37"},
                            new String[]{"Shameless", "4:13"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Weeknd: Beauty Behind the Madness", ex);
            return null;
        }
    }

    private Albumes crearAlbumTheWeeknd2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/theweeknd_afterhours.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("after_hours", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("After Hours");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-20"));
            album.setGenero(Arrays.asList("R&B"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Blinding Lights", "3:22"},
                            new String[]{"Save Your Tears", "3:35"},
                            new String[]{"Heartless", "3:18"},
                            new String[]{"In Your Eyes", "3:57"},
                            new String[]{"Too Late", "3:59"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Weeknd: After Hours", ex);
            return null;
        }
    }

    private Albumes crearAlbumTheWeeknd3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/theweeknd_starboy.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("starboy", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Starboy");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-25"));
            album.setGenero(Arrays.asList("R&B"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Starboy", "3:50"},
                            new String[]{"I Feel It Coming", "4:29"},
                            new String[]{"Party Monster", "4:09"},
                            new String[]{"Reminder", "3:38"},
                            new String[]{"Secrets", "4:25"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de The Weeknd: Starboy", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumBillieEilish1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/whenweallfallasleep.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("whenweallfallasleep", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("When We All Fall Asleep, Where Do We Go?");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-29"));
            album.setGenero(Arrays.asList("Alternative Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Bad Guy", "3:14"},
                            new String[]{"Bury a Friend", "3:13"},
                            new String[]{"When the Party's Over", "3:16"},
                            new String[]{"All the Good Girls Go to Hell", "2:49"},
                            new String[]{"You Should See Me in a Crown", "3:01"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Billie Eilish: When We All Fall Asleep, Where Do We Go?", ex);
            return null;
        }
    }

    private Albumes crearAlbumBillieEilish2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/happierthanever.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("happierthanever", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Happier Than Ever");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-07-30"));
            album.setGenero(Arrays.asList("Alternative Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Happier Than Ever", "4:58"},
                            new String[]{"Lost Cause", "3:32"},
                            new String[]{"My Future", "3:29"},
                            new String[]{"Oxytocin", "3:30"},
                            new String[]{"Your Power", "4:05"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Billie Eilish: Happier Than Ever", ex);
            return null;
        }
    }

    private Albumes crearAlbumBillieEilish3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dontsmileatme.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("dontsmileatme", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Don't Smile at Me");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-08-11"));
            album.setGenero(Arrays.asList("Alternative Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Ocean Eyes", "3:20"},
                            new String[]{"Bellyache", "2:59"},
                            new String[]{"Watch", "2:57"},
                            new String[]{"Copycat", "3:14"},
                            new String[]{"Idontwannabeyouanymore", "3:23"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Billie Eilish: Don't Smile at Me", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumHarryStyles1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/harrystylesalbum.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("harrystyles", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Harry Styles");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-12"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Sign of the Times", "5:41"},
                            new String[]{"Two Ghosts", "3:49"},
                            new String[]{"Kiwi", "2:56"},
                            new String[]{"Sweet Creature", "3:44"},
                            new String[]{"Ever Since New York", "4:13"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Harry Styles: Harry Styles", ex);
            return null;
        }
    }

    private Albumes crearAlbumHarryStyles2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/fine_line.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("fine_line", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Fine Line");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-13"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Watermelon Sugar", "2:54"},
                            new String[]{"Adore You", "3:27"},
                            new String[]{"Golden", "3:29"},
                            new String[]{"Falling", "4:00"},
                            new String[]{"Sunflower, Vol. 6", "3:41"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Harry Styles: Fine Line", ex);
            return null;
        }
    }

    private Albumes crearAlbumHarryStyles3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/harrys_house.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("harrys_house", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Harry's House");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-20"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"As It Was", "2:47"},
                            new String[]{"Late Night Talking", "2:57"},
                            new String[]{"Music for a Sushi Restaurant", "3:14"},
                            new String[]{"Matilda", "4:05"},
                            new String[]{"Daydreaming", "3:36"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Harry Styles: Harry's House", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumEdSheeran1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/plus.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("plus", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("+ (Plus)");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2011-09-09"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"The A Team", "4:18"},
                            new String[]{"Lego House", "3:05"},
                            new String[]{"Drunk", "3:20"},
                            new String[]{"Small Bump", "4:19"},
                            new String[]{"Give Me Love", "5:26"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ed Sheeran: + (Plus)", ex);
            return null;
        }
    }

    private Albumes crearAlbumEdSheeran2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/multiply.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("multiply", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("× (Multiply)");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2014-06-20"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Sing", "3:55"},
                            new String[]{"Don't", "3:39"},
                            new String[]{"Thinking Out Loud", "4:41"},
                            new String[]{"Photograph", "4:19"},
                            new String[]{"Tenerife Sea", "4:01"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ed Sheeran: × (Multiply)", ex);
            return null;
        }
    }

    private Albumes crearAlbumEdSheeran3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/divide.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("divide", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("÷ (Divide)");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-03"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Shape of You", "3:53"},
                            new String[]{"Castle on the Hill", "4:21"},
                            new String[]{"Galway Girl", "2:50"},
                            new String[]{"Perfect", "4:23"},
                            new String[]{"Happier", "3:27"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ed Sheeran: ÷ (Divide)", ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumAdele1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/19.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("19", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("19");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2008-01-28"));
            album.setGenero(Arrays.asList("Soul"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Chasing Pavements", "3:30"},
                            new String[]{"Hometown Glory", "4:31"},
                            new String[]{"Make You Feel My Love", "3:32"},
                            new String[]{"Daydreamer", "3:40"},
                            new String[]{"Cold Shoulder", "3:11"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Adele: 19", ex);
            return null;
        }
    }

    private Albumes crearAlbumAdele2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/21.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("21", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("21");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2011-01-24"));
            album.setGenero(Arrays.asList("Soul"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Rolling in the Deep", "3:48"},
                            new String[]{"Someone Like You", "4:45"},
                            new String[]{"Set Fire to the Rain", "4:02"},
                            new String[]{"Rumour Has It", "3:43"},
                            new String[]{"Turning Tables", "4:10"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Adele: 21", ex);
            return null;
        }
    }

    private Albumes crearAlbumAdele3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/25.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("25", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("25");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-11-20"));
            album.setGenero(Arrays.asList("Soul"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Hello", "4:55"},
                            new String[]{"When We Were Young", "4:51"},
                            new String[]{"Send My Love (To Your New Lover)", "3:43"},
                            new String[]{"Water Under the Bridge", "4:00"},
                            new String[]{"All I Ask", "4:32"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Adele: 25", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumRosalia1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/losangeles.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Los Ángeles", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Los Ángeles");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-02-10"));
            album.setGenero(Arrays.asList("Flamenco Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Catalina", "3:24"},
                            new String[]{"De Plata", "3:59"},
                            new String[]{"Si Tú Supieras Compañero", "3:16"},
                            new String[]{"Por Castigarme Tan Fuerte", "3:22"},
                            new String[]{"Los Ángeles", "4:06"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Rosalía: Los Ángeles", ex);
            return null;
        }
    }

    private Albumes crearAlbumRosalia2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/elmalquerer.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("El Mal Querer", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("El Mal Querer");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-02"));
            album.setGenero(Arrays.asList("Flamenco Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Malamente", "2:30"},
                            new String[]{"Pienso en Tu Mirá", "3:13"},
                            new String[]{"Di Mi Nombre", "2:43"},
                            new String[]{"Bagdad", "3:40"},
                            new String[]{"A Ningún Hombre", "3:06"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Rosalía: El Mal Querer", ex);
            return null;
        }
    }

    private Albumes crearAlbumRosalia3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/motomami.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Motomami", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Motomami");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2022-03-18"));
            album.setGenero(Arrays.asList("Flamenco Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Saoko", "2:17"},
                            new String[]{"Chicken Teriyaki", "2:02"},
                            new String[]{"Hentai", "2:42"},
                            new String[]{"Motomami", "2:00"},
                            new String[]{"Despechá", "2:37"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Rosalía: Motomami", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumShakira1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/piesdescalzos.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Pies Descalzos", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Pies Descalzos");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1995-10-06"));
            album.setGenero(Arrays.asList("Pop Latino"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Estoy Aquí", "3:54"},
                            new String[]{"Antología", "4:14"},
                            new String[]{"Dónde Estás Corazón", "3:51"},
                            new String[]{"Un Poco de Amor", "4:00"},
                            new String[]{"Se Quiere, Se Mata", "3:40"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Shakira: Pies Descalzos", ex);
            return null;
        }
    }

    private Albumes crearAlbumShakira2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dondeestanlosladrones.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("¿Dónde Están los Ladrones?", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("¿Dónde Están los Ladrones?");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1998-09-29"));
            album.setGenero(Arrays.asList("Pop Latino"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Ciega, Sordomuda", "4:26"},
                            new String[]{"Si Te Vas", "3:32"},
                            new String[]{"Ojos Así", "3:58"},
                            new String[]{"Inevitable", "3:13"},
                            new String[]{"Tú", "3:34"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Shakira: ¿Dónde Están los Ladrones?", ex);
            return null;
        }
    }

    private Albumes crearAlbumShakira3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/loba.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Loba", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Loba");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2009-10-09"));
            album.setGenero(Arrays.asList("Pop Latino"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Loba", "3:07"},
                            new String[]{"Lo Hecho Está Hecho", "3:13"},
                            new String[]{"Gypsy", "3:19"},
                            new String[]{"Mon Amour", "4:05"},
                            new String[]{"Men in This Town", "3:34"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Shakira: Loba", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumKarolG1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/unstoppable.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Unstoppable", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Unstoppable");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-27"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Ahora Me Llama", "3:55"},
                            new String[]{"A Ella", "3:47"},
                            new String[]{"Hello", "3:49"},
                            new String[]{"Eres Mi Todo", "3:50"},
                            new String[]{"Casi Nada", "3:35"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Karol G: Unstoppable", ex);
            return null;
        }
    }

    private Albumes crearAlbumKarolG2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/ocean.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Ocean", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Ocean");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-03"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Ocean", "3:38"},
                            new String[]{"Punto G", "3:13"},
                            new String[]{"Culpables", "3:12"},
                            new String[]{"Mi Cama", "2:55"},
                            new String[]{"Créeme", "3:29"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Karol G: Ocean", ex);
            return null;
        }
    }

    private Albumes crearAlbumKarolG3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/kg0516.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("KG0516", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("KG0516");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-03-25"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Tusa", "3:20"},
                            new String[]{"Bichota", "3:29"},
                            new String[]{"El Makinon", "3:24"},
                            new String[]{"200 Copas", "3:21"},
                            new String[]{"Ay, Dios Mío!", "2:54"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Karol G: KG0516", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumOliviaRodrigo1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/sour.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("SOUR", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("SOUR");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-21"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"brutal", "2:23"},
                            new String[]{"traitor", "3:49"},
                            new String[]{"drivers license", "4:02"},
                            new String[]{"1 step forward, 3 steps back", "2:44"},
                            new String[]{"deja vu", "3:35"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Olivia Rodrigo: SOUR", ex);
            return null;
        }
    }

    private Albumes crearAlbumOliviaRodrigo2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/guts.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("GUTS", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("GUTS");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2023-09-08"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"all-american bitch", "2:37"},
                            new String[]{"bad idea right?", "3:04"},
                            new String[]{"vampire", "3:39"},
                            new String[]{"lacy", "2:54"},
                            new String[]{"get him back!", "3:22"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Olivia Rodrigo: GUTS", ex);
            return null;
        }
    }

    private Albumes crearAlbumOliviaRodrigo3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/livefrom.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Live from SOUR Tour", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Live from SOUR Tour");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2022-11-15"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"favorite crime (Live)", "3:20"},
                            new String[]{"good 4 u (Live)", "3:27"},
                            new String[]{"happier (Live)", "2:56"},
                            new String[]{"hope ur ok (Live)", "3:50"},
                            new String[]{"deja vu (Live)", "3:44"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Olivia Rodrigo: Live from SOUR Tour", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumBrunoMars1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/doowopsandhooligans.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Doo-Wops & Hooligans", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Doo-Wops & Hooligans");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2010-10-05"));
            album.setGenero(Arrays.asList("Funk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Just the Way You Are", "3:41"},
                            new String[]{"Grenade", "3:42"},
                            new String[]{"The Lazy Song", "3:10"},
                            new String[]{"Marry You", "3:50"},
                            new String[]{"Count On Me", "3:17"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bruno Mars: Doo-Wops & Hooligans", ex);
            return null;
        }
    }

    private Albumes crearAlbumBrunoMars2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/unorthodoxjukebox.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Unorthodox Jukebox", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Unorthodox Jukebox");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2012-12-07"));
            album.setGenero(Arrays.asList("Funk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Locked Out of Heaven", "3:53"},
                            new String[]{"When I Was Your Man", "3:33"},
                            new String[]{"Treasure", "2:58"},
                            new String[]{"Gorilla", "4:04"},
                            new String[]{"If I Knew", "2:12"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bruno Mars: Unorthodox Jukebox", ex);
            return null;
        }
    }

    private Albumes crearAlbumBrunoMars3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/24kmagic.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("24K Magic", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("24K Magic");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-18"));
            album.setGenero(Arrays.asList("Funk"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"24K Magic", "3:46"},
                            new String[]{"That's What I Like", "3:26"},
                            new String[]{"Versace on the Floor", "4:21"},
                            new String[]{"Chunky", "3:07"},
                            new String[]{"Finesse", "3:11"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bruno Mars: 24K Magic", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumArianaGrande1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/yours_truly.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Yours Truly", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Yours Truly");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2013-09-03"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Honeymoon Avenue", "5:40"},
                            new String[]{"The Way (feat. Mac Miller)", "3:46"},
                            new String[]{"Baby I", "3:17"},
                            new String[]{"Right There (feat. Big Sean)", "4:07"},
                            new String[]{"Almost Is Never Enough (feat. Nathan Sykes)", "5:28"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ariana Grande: Yours Truly", ex);
            return null;
        }
    }

    private Albumes crearAlbumArianaGrande2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/my_everything.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("My Everything", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("My Everything");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2014-08-25"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Problem (feat. Iggy Azalea)", "3:14"},
                            new String[]{"Break Free (feat. Zedd)", "3:35"},
                            new String[]{"Love Me Harder (with The Weeknd)", "3:56"},
                            new String[]{"One Last Time", "3:17"},
                            new String[]{"Bang Bang (with Jessie J & Nicki Minaj)", "3:19"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ariana Grande: My Everything", ex);
            return null;
        }
    }

    private Albumes crearAlbumArianaGrande3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/thank_u_next.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Thank U, Next", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Thank U, Next");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-08"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Imagine", "3:32"},
                            new String[]{"Needy", "2:51"},
                            new String[]{"NASA", "3:02"},
                            new String[]{"7 Rings", "2:58"},
                            new String[]{"Thank U, Next", "3:27"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ariana Grande: Thank U, Next", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumJBalvin1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/energia.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Energia", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Energía");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2016-06-24"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Ginza", "2:58"},
                            new String[]{"Safari (feat. Pharrell Williams, BIA & Sky)", "3:25"},
                            new String[]{"Bobo", "2:57"},
                            new String[]{"Sigo Extrañándote", "3:23"},
                            new String[]{"Snapchat", "3:25"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de J Balvin: Energía", ex);
            return null;
        }
    }

    private Albumes crearAlbumJBalvin2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/vibras.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Vibras", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Vibras");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-25"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Machika (feat. Jeon & Anitta)", "3:10"},
                            new String[]{"Mi Gente (feat. Willy William)", "3:06"},
                            new String[]{"Ambiente", "3:12"},
                            new String[]{"Cuando Tú Quieras", "3:35"},
                            new String[]{"No Es Justo (feat. Zion & Lennox)", "3:43"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de J Balvin: Vibras", ex);
            return null;
        }
    }

    private Albumes crearAlbumJBalvin3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/colores.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Colores", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Colores");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-19"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Blanco", "2:25"},
                            new String[]{"Morado", "3:18"},
                            new String[]{"Rojo", "2:34"},
                            new String[]{"Amarillo", "2:45"},
                            new String[]{"Azul", "2:47"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de J Balvin: Colores", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumElvis1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/elvis.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Elvis Presley Album", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Elvis Presley");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1956-03-23"));
            album.setGenero(Arrays.asList("Rock & Roll"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Blue Suede Shoes", "2:00"},
                            new String[]{"I Got a Woman", "2:23"},
                            new String[]{"Heartbreak Hotel", "2:08"},
                            new String[]{"Tutti Frutti", "2:00"},
                            new String[]{"Money Honey", "2:36"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Elvis Presley: Elvis Presley", ex);
            return null;
        }
    }

    private Albumes crearAlbumElvis2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/elvisgold.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("50,000,000 Elvis Fans Can't Be Wrong", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("50,000,000 Elvis Fans Can't Be Wrong");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1959-11-13"));
            album.setGenero(Arrays.asList("Rock & Roll"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"I Need Your Love Tonight", "2:05"},
                            new String[]{"A Big Hunk O' Love", "2:12"},
                            new String[]{"A Fool Such as I", "2:41"},
                            new String[]{"I Got Stung", "1:51"},
                            new String[]{"Wear My Ring Around Your Neck", "2:15"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Elvis Presley: 50,000,000 Elvis Fans Can't Be Wrong", ex);
            return null;
        }
    }

    private Albumes crearAlbumElvis3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/elvismemphis.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("From Elvis in Memphis", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("From Elvis in Memphis");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1969-06-17"));
            album.setGenero(Arrays.asList("Rock & Roll"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Suspicious Minds", "4:22"},
                            new String[]{"In the Ghetto", "2:57"},
                            new String[]{"Kentucky Rain", "3:20"},
                            new String[]{"Any Day Now", "3:02"},
                            new String[]{"Only the Strong Survive", "2:45"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Elvis Presley: From Elvis in Memphis", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumBobDylan1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bobdylanfreetwheelin.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("The Freewheelin' Bob Dylan", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("The Freewheelin' Bob Dylan");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1963-05-27"));
            album.setGenero(Arrays.asList("Folk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Blowin' in the Wind", "2:48"},
                            new String[]{"Girl from the North Country", "3:22"},
                            new String[]{"Masters of War", "4:34"},
                            new String[]{"A Hard Rain's a-Gonna Fall", "6:55"},
                            new String[]{"Don't Think Twice, It's All Right", "3:40"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bob Dylan: The Freewheelin' Bob Dylan", ex);
            return null;
        }
    }

    private Albumes crearAlbumBobDylan2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bobdylanhighway61.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Highway 61 Revisited", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Highway 61 Revisited");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1965-08-30"));
            album.setGenero(Arrays.asList("Folk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Like a Rolling Stone", "6:13"},
                            new String[]{"Tombstone Blues", "5:58"},
                            new String[]{"Ballad of a Thin Man", "5:58"},
                            new String[]{"Highway 61 Revisited", "3:30"},
                            new String[]{"Desolation Row", "11:21"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bob Dylan: Highway 61 Revisited", ex);
            return null;
        }
    }

    private Albumes crearAlbumBobDylan3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bobdylanbloodontracks.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Blood on the Tracks", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Blood on the Tracks");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1975-01-20"));
            album.setGenero(Arrays.asList("Folk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Tangled Up in Blue", "5:42"},
                            new String[]{"Simple Twist of Fate", "4:18"},
                            new String[]{"You're Gonna Make Me Lonesome When You Go", "2:55"},
                            new String[]{"Shelter from the Storm", "5:02"},
                            new String[]{"Buckets of Rain", "3:22"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Bob Dylan: Blood on the Tracks", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumJanisJoplin1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/janispearl.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Pearl", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Pearl");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1971-01-11"));
            album.setGenero(Arrays.asList("Blues Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Move Over", "3:40"},
                            new String[]{"Cry Baby", "3:57"},
                            new String[]{"A Woman Left Lonely", "3:29"},
                            new String[]{"Me and Bobby McGee", "4:31"},
                            new String[]{"Mercedes Benz", "1:48"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Janis Joplin: Pearl", ex);
            return null;
        }
    }

    private Albumes crearAlbumJanisJoplin2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/janischeapthrills.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Cheap Thrills", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Cheap Thrills");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1968-08-12"));
            album.setGenero(Arrays.asList("Blues Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Combination of the Two", "5:47"},
                            new String[]{"Summertime", "4:00"},
                            new String[]{"Piece of My Heart", "4:14"},
                            new String[]{"Ball and Chain", "9:37"},
                            new String[]{"I Need a Man to Love", "4:53"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Janis Joplin: Cheap Thrills", ex);
            return null;
        }
    }

    private Albumes crearAlbumJanisJoplin3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/janiskozmicblues.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("I Got Dem Ol' Kozmic Blues Again Mama!", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("I Got Dem Ol' Kozmic Blues Again Mama!");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1969-09-11"));
            album.setGenero(Arrays.asList("Blues Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Try (Just a Little Bit Harder)", "3:57"},
                            new String[]{"Maybe", "3:40"},
                            new String[]{"Kozmic Blues", "4:22"},
                            new String[]{"Little Girl Blue", "3:50"},
                            new String[]{"Work Me, Lord", "6:43"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Janis Joplin: I Got Dem Ol' Kozmic Blues Again Mama!", ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumJimiHendrix1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/areyouexperienced.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Are You Experienced", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Are You Experienced");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1967-05-12"));
            album.setGenero(Arrays.asList("Psychedelic Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Purple Haze", "2:50"},
                            new String[]{"Manic Depression", "3:30"},
                            new String[]{"Hey Joe", "3:26"},
                            new String[]{"The Wind Cries Mary", "3:21"},
                            new String[]{"Fire", "2:43"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Jimi Hendrix: Are You Experienced", ex);
            return null;
        }
    }

    private Albumes crearAlbumJimiHendrix2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/axiseboldaslove.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Axis: Bold as Love", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Axis: Bold as Love");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1967-12-01"));
            album.setGenero(Arrays.asList("Psychedelic Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Spanish Castle Magic", "3:00"},
                            new String[]{"Little Wing", "2:26"},
                            new String[]{"If 6 Was 9", "5:33"},
                            new String[]{"Castles Made of Sand", "2:47"},
                            new String[]{"Bold as Love", "4:09"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Jimi Hendrix: Axis: Bold as Love", ex);
            return null;
        }
    }

    private Albumes crearAlbumJimiHendrix3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/electricladyland.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Electric Ladyland", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Electric Ladyland");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1968-10-16"));
            album.setGenero(Arrays.asList("Psychedelic Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Voodoo Child (Slight Return)", "5:12"},
                            new String[]{"All Along the Watchtower", "4:01"},
                            new String[]{"Crosstown Traffic", "2:25"},
                            new String[]{"Gypsy Eyes", "3:43"},
                            new String[]{"Burning of the Midnight Lamp", "3:39"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Jimi Hendrix: Electric Ladyland", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumDavidBowie1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/the-rise-and-fall-of-ziggy-stardust.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("The Rise and Fall of Ziggy Stardust and the Spiders from Mars", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("The Rise and Fall of Ziggy Stardust and the Spiders from Mars");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1972-06-16"));
            album.setGenero(Arrays.asList("Glam Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Five Years", "4:42"},
                            new String[]{"Soul Love", "3:34"},
                            new String[]{"Moonage Daydream", "4:39"},
                            new String[]{"Starman", "4:13"},
                            new String[]{"Suffragette City", "3:25"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de David Bowie: The Rise and Fall of Ziggy Stardust", ex);
            return null;
        }
    }

    private Albumes crearAlbumDavidBowie2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/low.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Low", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Low");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1977-01-14"));
            album.setGenero(Arrays.asList("Glam Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Speed of Life", "2:46"},
                            new String[]{"Breaking Glass", "1:52"},
                            new String[]{"Sound and Vision", "3:05"},
                            new String[]{"Be My Wife", "2:58"},
                            new String[]{"A New Career in a New Town", "2:53"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de David Bowie: Low", ex);
            return null;
        }
    }

    private Albumes crearAlbumDavidBowie3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/lets-dance.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Let's Dance", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Let's Dance");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1983-04-14"));
            album.setGenero(Arrays.asList("Glam Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Modern Love", "4:46"},
                            new String[]{"China Girl", "5:32"},
                            new String[]{"Let's Dance", "7:38"},
                            new String[]{"Without You", "3:08"},
                            new String[]{"Cat People (Putting Out Fire)", "5:10"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de David Bowie: Let's Dance", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumPrince1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/purple-rain.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Purple Rain", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Purple Rain");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1984-06-25"));
            album.setGenero(Arrays.asList("Funk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Let's Go Crazy", "4:39"},
                            new String[]{"Take Me with U", "3:54"},
                            new String[]{"When Doves Cry", "5:54"},
                            new String[]{"I Would Die 4 U", "2:49"},
                            new String[]{"Purple Rain", "8:41"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Prince: Purple Rain", ex);
            return null;
        }
    }

    private Albumes crearAlbumPrince2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/sign-o-the-times.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Sign o' the Times", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Sign o' the Times");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1987-03-30"));
            album.setGenero(Arrays.asList("Funk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Sign o' the Times", "4:57"},
                            new String[]{"If I Was Your Girlfriend", "5:01"},
                            new String[]{"I Could Never Take the Place of Your Man", "6:31"},
                            new String[]{"U Got the Look", "3:46"},
                            new String[]{"The Cross", "4:46"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Prince: Sign o' the Times", ex);
            return null;
        }
    }

    private Albumes crearAlbumPrince3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/1999.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("1999", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("1999");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1982-10-27"));
            album.setGenero(Arrays.asList("Funk Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"1999", "6:15"},
                            new String[]{"Little Red Corvette", "4:58"},
                            new String[]{"Delirious", "3:56"},
                            new String[]{"D.M.S.R.", "8:05"},
                            new String[]{"Automatic", "9:28"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Prince: 1999", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumFreddieMercury1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/mr-bad-guy.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Mr. Bad Guy", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Mr. Bad Guy");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1985-04-29"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Let's Turn It On", "3:43"},
                            new String[]{"Made in Heaven", "4:05"},
                            new String[]{"I Was Born to Love You", "3:39"},
                            new String[]{"Living on My Own", "3:23"},
                            new String[]{"Love Me Like There's No Tomorrow", "3:44"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Freddie Mercury: Mr. Bad Guy", ex);
            return null;
        }
    }

    private Albumes crearAlbumFreddieMercury2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/barcelona.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Barcelona", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Barcelona");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1988-10-10"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Barcelona", "5:39"},
                            new String[]{"La Japonaise", "4:49"},
                            new String[]{"The Fallen Priest", "5:47"},
                            new String[]{"Ensueño", "4:21"},
                            new String[]{"How Can I Go On", "3:49"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Freddie Mercury: Barcelona", ex);
            return null;
        }
    }

    private Albumes crearAlbumFreddieMercury3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/the-great-pretender.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("The Great Pretender", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("The Great Pretender");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1992-11-23"));
            album.setGenero(Arrays.asList("Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"The Great Pretender", "3:27"},
                            new String[]{"In My Defence", "3:52"},
                            new String[]{"Time", "4:02"},
                            new String[]{"Living on My Own (Remix)", "3:38"},
                            new String[]{"Love Kills", "4:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Freddie Mercury: The Great Pretender", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumMichaelJackson1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/thriller.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Thriller", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Thriller");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1982-11-30"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Wanna Be Startin' Somethin'", "6:03"},
                            new String[]{"Thriller", "5:57"},
                            new String[]{"Beat It", "4:18"},
                            new String[]{"Billie Jean", "4:54"},
                            new String[]{"Human Nature", "4:06"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Michael Jackson: Thriller", ex);
            return null;
        }
    }

    private Albumes crearAlbumMichaelJackson2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bad.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Bad", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Bad");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1987-08-31"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Bad", "4:07"},
                            new String[]{"The Way You Make Me Feel", "4:58"},
                            new String[]{"Smooth Criminal", "4:17"},
                            new String[]{"Man in the Mirror", "5:19"},
                            new String[]{"Dirty Diana", "4:52"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Michael Jackson: Bad", ex);
            return null;
        }
    }

    private Albumes crearAlbumMichaelJackson3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dangerous.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Dangerous", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Dangerous");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1991-11-13"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Jam", "5:39"},
                            new String[]{"Black or White", "4:16"},
                            new String[]{"Remember the Time", "4:00"},
                            new String[]{"In the Closet", "6:31"},
                            new String[]{"Dangerous", "7:00"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Michael Jackson: Dangerous", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumAmyWinehouse1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/frank.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Frank", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Frank");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2003-10-20"));
            album.setGenero(Arrays.asList("Jazz Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Stronger Than Me", "3:54"},
                            new String[]{"Take the Box", "3:20"},
                            new String[]{"In My Bed", "5:17"},
                            new String[]{"You Sent Me Flying", "5:18"},
                            new String[]{"Fuck Me Pumps", "3:20"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Amy Winehouse: Frank", ex);
            return null;
        }
    }

    private Albumes crearAlbumAmyWinehouse2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/backtoblack.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Back to Black", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Back to Black");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2006-10-27"));
            album.setGenero(Arrays.asList("Jazz Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Rehab", "3:35"},
                            new String[]{"You Know I'm No Good", "4:17"},
                            new String[]{"Back to Black", "4:01"},
                            new String[]{"Love Is a Losing Game", "2:35"},
                            new String[]{"Tears Dry on Their Own", "3:06"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Amy Winehouse: Back to Black", ex);
            return null;
        }
    }

    private Albumes crearAlbumAmyWinehouse3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/lioness.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Lioness: Hidden Treasures", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Lioness: Hidden Treasures");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2011-12-02"));
            album.setGenero(Arrays.asList("Jazz Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Our Day Will Come", "2:49"},
                            new String[]{"Between the Cheats", "3:33"},
                            new String[]{"Valerie ('68 Version)", "3:59"},
                            new String[]{"A Song for You", "4:29"},
                            new String[]{"Tears Dry (Original Version)", "4:08"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Amy Winehouse: Lioness: Hidden Treasures", ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumChrisStapleton1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/traveler.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Traveler", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Traveller");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-05"));
            album.setGenero(Arrays.asList("Country Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Traveler", "3:42"},
                            new String[]{"Fire Away", "4:04"},
                            new String[]{"Tennessee Whiskey", "4:53"},
                            new String[]{"Parachute", "4:13"},
                            new String[]{"Whiskey and You", "3:56"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Chris Stapleton: Traveler", ex);
            return null;
        }
    }

    private Albumes crearAlbumChrisStapleton2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/fromaroomvolume1.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("From A Room: Volume 1", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("From A Room: Volume 1");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-05"));
            album.setGenero(Arrays.asList("Country Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Broken Halos", "3:00"},
                            new String[]{"Last Thing I Needed, First Thing This Morning", "4:16"},
                            new String[]{"Second One to Know", "2:56"},
                            new String[]{"Either Way", "4:08"},
                            new String[]{"Up to No Good Livin'", "4:05"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Chris Stapleton: From A Room: Volume 1", ex);
            return null;
        }
    }

    private Albumes crearAlbumChrisStapleton3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/fromaroomvolume2.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("From A Room: Volume 2", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("From A Room: Volume 2");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-12-01"));
            album.setGenero(Arrays.asList("Country Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Millionaire", "3:30"},
                            new String[]{"Hard Livin'", "2:59"},
                            new String[]{"Scarecrow in the Garden", "3:21"},
                            new String[]{"Nobody's Lonely Tonight", "3:28"},
                            new String[]{"Tryin' to Untangle My Mind", "3:54"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Chris Stapleton: From A Room: Volume 2", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumSamSmith1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/inthelonelyhour.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("In The Lonely Hour", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("In The Lonely Hour");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2014-05-26"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Stay With Me", "2:52"},
                            new String[]{"I'm Not The Only One", "3:59"},
                            new String[]{"Like I Can", "2:47"},
                            new String[]{"Lay Me Down", "4:13"},
                            new String[]{"Money On My Mind", "3:14"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Sam Smith: In The Lonely Hour", ex);
            return null;
        }
    }

    private Albumes crearAlbumSamSmith2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/thethrilloftitall.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("The Thrill Of It All", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("The Thrill Of It All");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-11-03"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Too Good At Goodbyes", "3:21"},
                            new String[]{"Say It First", "4:07"},
                            new String[]{"One Last Song", "3:12"},
                            new String[]{"Pray", "3:58"},
                            new String[]{"Burning", "3:23"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Sam Smith: The Thrill Of It All", ex);
            return null;
        }
    }

    private Albumes crearAlbumSamSmith3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/gloria.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Gloria", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Gloria");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-27"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Unholy (feat. Kim Petras)", "2:36"},
                            new String[]{"I'm Not Here To Make Friends", "3:49"},
                            new String[]{"Love Me More", "3:10"},
                            new String[]{"Gloria", "1:53"},
                            new String[]{"Perfect", "4:14"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Sam Smith: Gloria", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumHozier1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/hozier_debut.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Hozier", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Hozier");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2014-09-19"));
            album.setGenero(Arrays.asList("Indie Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Take Me to Church", "4:02"},
                            new String[]{"Angel of Small Death & the Codeine Scene", "3:39"},
                            new String[]{"Jackie and Wilson", "3:43"},
                            new String[]{"Cherry Wine (Live)", "4:00"},
                            new String[]{"Someone New", "3:43"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Hozier: Hozier", ex);
            return null;
        }
    }

    private Albumes crearAlbumHozier2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/hozier_wasteland.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Wasteland, Baby!", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Wasteland, Baby!");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-01"));
            album.setGenero(Arrays.asList("Indie Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Movement", "3:57"},
                            new String[]{"Almost (Sweet Music)", "3:37"},
                            new String[]{"Dinner & Diatribes", "3:45"},
                            new String[]{"Wasteland, Baby!", "4:27"},
                            new String[]{"Would That I", "4:27"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Hozier: Wasteland, Baby!", ex);
            return null;
        }
    }

    private Albumes crearAlbumHozier3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/hozier_unreal.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Unreal Unearth", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Unreal Unearth");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2023-08-18"));
            album.setGenero(Arrays.asList("Indie Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"De Selby (Part 1)", "5:45"},
                            new String[]{"Francesca", "3:58"},
                            new String[]{"Unknown / Nth", "5:45"},
                            new String[]{"All Things End", "3:35"},
                            new String[]{"Anything But", "4:15"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Hozier: Unreal Unearth", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumLanaDelRey1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/borntodie.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Born to Die", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Born to Die");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2012-01-27"));
            album.setGenero(Arrays.asList("Indie Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Born to Die", "4:46"},
                            new String[]{"Blue Jeans", "3:29"},
                            new String[]{"Video Games", "4:41"},
                            new String[]{"Summertime Sadness", "4:25"},
                            new String[]{"National Anthem", "3:51"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Lana Del Rey: Born to Die", ex);
            return null;
        }
    }

    private Albumes crearAlbumLanaDelRey2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/normanrockwell.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Norman Fucking Rockwell!", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Norman Fucking Rockwell!");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-08-30"));
            album.setGenero(Arrays.asList("Indie Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Norman Fucking Rockwell", "4:08"},
                            new String[]{"Mariners Apartment Complex", "4:07"},
                            new String[]{"Venice Bitch", "9:37"},
                            new String[]{"Doin' Time", "3:22"},
                            new String[]{"The Greatest", "5:00"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Lana Del Rey: Norman Fucking Rockwell!", ex);
            return null;
        }
    }

    private Albumes crearAlbumLanaDelRey3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/chemtrails.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Chemtrails Over the Country Club", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Chemtrails Over the Country Club");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-03-19"));
            album.setGenero(Arrays.asList("Indie Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"White Dress", "5:33"},
                            new String[]{"Chemtrails Over the Country Club", "4:31"},
                            new String[]{"Tulsa Jesus Freak", "3:35"},
                            new String[]{"Wild at Heart", "4:06"},
                            new String[]{"Dark But Just a Game", "3:55"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Lana Del Rey: Chemtrails Over the Country Club", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumPostMalone1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/stoney.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Stoney", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Stoney");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-09"));
            album.setGenero(Arrays.asList("Hip Hop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"White Iverson", "4:17"},
                            new String[]{"Congratulations", "3:40"},
                            new String[]{"Go Flex", "3:00"},
                            new String[]{"I Fall Apart", "3:43"},
                            new String[]{"Deja Vu", "3:54"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Post Malone: Stoney", ex);
            return null;
        }
    }

    private Albumes crearAlbumPostMalone2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/beerbongs.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Beerbongs & Bentleys", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Beerbongs & Bentleys");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-27"));
            album.setGenero(Arrays.asList("Hip Hop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Rockstar", "3:38"},
                            new String[]{"Psycho", "3:41"},
                            new String[]{"Better Now", "3:51"},
                            new String[]{"Candy Paint", "3:49"},
                            new String[]{"Ball for Me", "3:26"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Post Malone: Beerbongs & Bentleys", ex);
            return null;
        }
    }

    private Albumes crearAlbumPostMalone3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/hollywoodsbleeding.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Hollywood's Bleeding", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Hollywood's Bleeding");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-09-06"));
            album.setGenero(Arrays.asList("Hip Hop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Circles", "3:35"},
                            new String[]{"Wow.", "2:29"},
                            new String[]{"Take What You Want", "3:49"},
                            new String[]{"Goodbyes", "2:55"},
                            new String[]{"Enemies", "3:16"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Post Malone: Hollywood's Bleeding", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumShawnMendes1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/handwritten.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Handwritten", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Handwritten");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-04-14"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Life of the Party", "3:34"},
                            new String[]{"Stitches", "3:59"},
                            new String[]{"Something Big", "2:41"},
                            new String[]{"Never Be Alone", "3:36"},
                            new String[]{"A Little Too Much", "3:06"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Shawn Mendes: Handwritten", ex);
            return null;
        }
    }

    private Albumes crearAlbumShawnMendes2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/illuminate.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Illuminate", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Illuminate");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2016-09-23"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Treat You Better", "3:08"},
                            new String[]{"Mercy", "3:28"},
                            new String[]{"Ruin", "4:01"},
                            new String[]{"Three Empty Words", "3:19"},
                            new String[]{"Don't Be a Fool", "3:35"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Shawn Mendes: Illuminate", ex);
            return null;
        }
    }

    private Albumes crearAlbumShawnMendes3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/shawnmendesalbum.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Shawn Mendes (2018)", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Shawn Mendes");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-25"));
            album.setGenero(Arrays.asList("Pop"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"In My Blood", "3:31"},
                            new String[]{"Lost in Japan", "3:21"},
                            new String[]{"Youth", "3:10"},
                            new String[]{"Where Were You in the Morning?", "3:20"},
                            new String[]{"Why", "3:59"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Shawn Mendes: Shawn Mendes", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumMileyCyrus1(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/bangerz.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Bangerz", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Bangerz");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2013-10-04"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"We Can't Stop", "3:52"},
                            new String[]{"Wrecking Ball", "3:41"},
                            new String[]{"Adore You", "4:37"},
                            new String[]{"4x4", "3:11"},
                            new String[]{"Drive", "4:15"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Miley Cyrus: Bangerz", ex);
            return null;
        }
    }

    private Albumes crearAlbumMileyCyrus2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/plastichearts.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Plastic Hearts", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Plastic Hearts");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-11-27"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Midnight Sky", "3:43"},
                            new String[]{"Prisoner (feat. Dua Lipa)", "2:49"},
                            new String[]{"Angels Like You", "3:16"},
                            new String[]{"Edge of Midnight (feat. Stevie Nicks)", "3:41"},
                            new String[]{"Gimme What I Want", "2:32"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Miley Cyrus: Plastic Hearts", ex);
            return null;
        }
    }

    private Albumes crearAlbumMileyCyrus3(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/endlesssummervacation.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Endless Summer Vacation", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("Endless Summer Vacation");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2023-03-10"));
            album.setGenero(Arrays.asList("Pop Rock"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Flowers", "3:21"},
                            new String[]{"River", "2:43"},
                            new String[]{"Jaded", "3:05"},
                            new String[]{"Violet Chemistry", "4:06"},
                            new String[]{"Rose Colored Lenses", "3:44"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Miley Cyrus: Endless Summer Vacation", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumDaddyYankee1(Artistas artista) {
        try {
            File imagenFile = new File("./images/barriofino.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Barrio Fino", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Barrio Fino");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2004-07-13"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Gasolina", "3:12"},
                            new String[]{"Lo Que Pasó, Pasó", "3:31"},
                            new String[]{"King Daddy", "3:44"},
                            new String[]{"Salud y Vida", "4:01"},
                            new String[]{"Dale Caliente", "3:28"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Daddy Yankee: Barrio Fino", ex);
            return null;
        }
    }

    private Albumes crearAlbumDaddyYankee2(Artistas artista) {
        try {
            File imagenFile = new File("./images/thebigboss.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("El Cartel: The Big Boss", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("El Cartel: The Big Boss");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2007-06-05"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Impacto", "3:40"},
                            new String[]{"Ella Me Levantó", "3:39"},
                            new String[]{"Mensaje de Estado", "3:58"},
                            new String[]{"Papi Lover", "3:13"},
                            new String[]{"Cambio", "3:44"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Daddy Yankee: El Cartel: The Big Boss", ex);
            return null;
        }
    }

    private Albumes crearAlbumDaddyYankee3(Artistas artista) {
        try {
            File imagenFile = new File("./images/legendaddy.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Legendaddy", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Legendaddy");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2022-03-24"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Rumbatón", "3:20"},
                            new String[]{"La Ola", "2:46"},
                            new String[]{"Campeón", "3:15"},
                            new String[]{"Agua", "3:35"},
                            new String[]{"Zona del Perreo", "4:01"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Daddy Yankee: Legendaddy", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumOzuna1(Artistas artista) {
        try {
            File imagenFile = new File("./images/odisea.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Odisea", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Odisea");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2017-08-25"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Dile Que Tú Me Quieres", "4:02"},
                            new String[]{"Se Preparó", "3:38"},
                            new String[]{"Tu Foto", "3:48"},
                            new String[]{"El Farsante", "4:07"},
                            new String[]{"Odisea", "4:00"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ozuna: Odisea", ex);
            return null;
        }
    }

    private Albumes crearAlbumOzuna2(Artistas artista) {
        try {
            File imagenFile = new File("./images/aura.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Aura", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Aura");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-08-24"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Vaina Loca", "4:07"},
                            new String[]{"Única", "3:24"},
                            new String[]{"Ibiza", "3:48"},
                            new String[]{"Me Dijeron", "4:01"},
                            new String[]{"Taki Taki", "3:52"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ozuna: Aura", ex);
            return null;
        }
    }

    private Albumes crearAlbumOzuna3(Artistas artista) {
        try {
            File imagenFile = new File("./images/enoc.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("ENOC", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("ENOC");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-04"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Caramelo", "3:33"},
                            new String[]{"Despeinada", "3:23"},
                            new String[]{"Del Mar", "3:36"},
                            new String[]{"No Se Da Cuenta", "3:49"},
                            new String[]{"Gistro Amarillo", "3:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Ozuna: ENOC", ex);
            return null;
        }
    }
///////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumAnuelAA1(Artistas artista) {
        try {
            File imagenFile = new File("./images/realhastalamuerte.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Real Hasta La Muerte", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Real Hasta La Muerte");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-07-17"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Hipócrita", "3:49"},
                            new String[]{"Ella Quiere Beber", "3:39"},
                            new String[]{"Brindemos", "3:44"},
                            new String[]{"Yeezy", "3:40"},
                            new String[]{"Na' Nuevo", "3:56"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Anuel AA: Real Hasta La Muerte", ex);
            return null;
        }
    }

    private Albumes crearAlbumAnuelAA2(Artistas artista) {
        try {
            File imagenFile = new File("./images/manuel.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Emmanuel", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Emmanuel");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-29"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Reggaetonera", "3:32"},
                            new String[]{"Fútbol y Rumba", "4:02"},
                            new String[]{"Keii", "4:08"},
                            new String[]{"Hasta Que Dios Diga", "3:54"},
                            new String[]{"Narcos", "3:31"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Anuel AA: Emmanuel", ex);
            return null;
        }
    }

    private Albumes crearAlbumAnuelAA3(Artistas artista) {
        try {
            File imagenFile = new File("./images/lasleyendasnuncanmueren.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Las Leyendas Nunca Mueren", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Las Leyendas Nunca Mueren");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-11-26"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Dictadura", "4:01"},
                            new String[]{"Subelo", "3:42"},
                            new String[]{"Leyenda", "3:40"},
                            new String[]{"Pin", "3:33"},
                            new String[]{"Llorando en un Ferrari", "3:30"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Anuel AA: Las Leyendas Nunca Mueren", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumMaluma1(Artistas artista) {
        try {
            File imagenFile = new File("./images/fame.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("F.A.M.E.", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("F.A.M.E.");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-18"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"El Préstamo", "3:39"},
                            new String[]{"Corazón", "3:05"},
                            new String[]{"Felices los 4", "3:49"},
                            new String[]{"Marinero", "4:10"},
                            new String[]{"Hangover", "4:01"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maluma: F.A.M.E.", ex);
            return null;
        }
    }

    private Albumes crearAlbumMaluma2(Artistas artista) {
        try {
            File imagenFile = new File("./images/1111.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("11:11", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("11:11");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-17"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"HP", "3:04"},
                            new String[]{"11 PM", "3:42"},
                            new String[]{"Dispuesto", "3:49"},
                            new String[]{"No Se Me Quita", "3:39"},
                            new String[]{"Instinto Natural", "3:52"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maluma: 11:11", ex);
            return null;
        }
    }

    private Albumes crearAlbumMaluma3(Artistas artista) {
        try {
            File imagenFile = new File("./images/papijuancho.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Papi Juancho", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Papi Juancho");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-08-21"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Hawái", "3:20"},
                            new String[]{"Parce", "3:43"},
                            new String[]{"ADMV", "3:04"},
                            new String[]{"Vete Vete", "3:33"},
                            new String[]{"Madrid", "3:38"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Maluma: Papi Juancho", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumNattiNatasha1(Artistas artista) {
        try {
            File imagenFile = new File("./images/iluminatti.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Iluminatti", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Iluminatti");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-15"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Pa' Mala Yo", "3:32"},
                            new String[]{"Me Gusta", "3:12"},
                            new String[]{"Obsesión", "3:33"},
                            new String[]{"La Mejor Versión de Mí", "3:33"},
                            new String[]{"Toca Toca", "3:35"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Natti Natasha: Iluminatti", ex);
            return null;
        }
    }

    private Albumes crearAlbumNattiNatasha2(Artistas artista) {
        try {
            File imagenFile = new File("./images/nattivida.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Nattividad", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Nattividad");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-24"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Noches en Miami", "3:15"},
                            new String[]{"Philliecito", "3:38"},
                            new String[]{"Imposible Amor", "3:36"},
                            new String[]{"Las Nenas", "3:48"},
                            new String[]{"Wow BB", "3:21"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Natti Natasha: Nattividad", ex);
            return null;
        }
    }

    private Albumes crearAlbumNattiNatasha3(Artistas artista) {
        try {
            File imagenFile = new File("./images/allaboutme.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("All About Me", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("All About Me");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-20"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Diosa", "3:10"},
                            new String[]{"Qué Mal Te Fue", "2:55"},
                            new String[]{"Despacio", "3:40"},
                            new String[]{"Te Lo Dije", "3:50"},
                            new String[]{"La Mejor Versión de Mí (Remix)", "3:55"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Natti Natasha: All About Me", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumBeckyG1(Artistas artista) {
        try {
            File imagenFile = new File("./images/malibu.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Mala Santa", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Mala Santa");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-17"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Mala Santa", "3:05"},
                            new String[]{"Sin Pijama", "3:09"},
                            new String[]{"Dollar", "3:12"},
                            new String[]{"Mayores", "3:22"},
                            new String[]{"Cuando Te Besé", "3:26"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Becky G: Mala Santa", ex);
            return null;
        }
    }

    private Albumes crearAlbumBeckyG2(Artistas artista) {
        try {
            File imagenFile = new File("./images/esquemas.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Esquemas", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Esquemas");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-13"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Mamiii", "3:25"},
                            new String[]{"No Mienten", "3:18"},
                            new String[]{"Bailé Con Mi Ex", "3:10"},
                            new String[]{"Dolores", "3:00"},
                            new String[]{"Flashback", "3:35"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Becky G: Esquemas", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumFarruko1(Artistas artista) {
        try {
            File imagenFile = new File("./images/visionary.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Visionary", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Visionary");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-23"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Obsesionado", "3:57"},
                            new String[]{"Sunset", "3:42"},
                            new String[]{"Chillax", "3:58"},
                            new String[]{"Te Va a Doler", "3:20"},
                            new String[]{"Después Que Te Perdí", "3:45"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Farruko: Visionary", ex);
            return null;
        }
    }

    private Albumes crearAlbumFarruko2(Artistas artista) {
        try {
            File imagenFile = new File("./images/gangalee.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Gangalee", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Gangalee");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-26"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Nadie", "3:20"},
                            new String[]{"La Cartera", "3:25"},
                            new String[]{"Titerito", "3:10"},
                            new String[]{"Inolvidable", "4:12"},
                            new String[]{"Quédate", "3:05"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Farruko: Gangalee", ex);
            return null;
        }
    }

    private Albumes crearAlbumFarruko3(Artistas artista) {
        try {
            File imagenFile = new File("./images/la167.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("La 167", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("La 167");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-01"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Pepas", "4:47"},
                            new String[]{"El Incomprendido", "3:52"},
                            new String[]{"La Tóxica", "3:25"},
                            new String[]{"Jíbaro", "3:55"},
                            new String[]{"My Lova", "4:10"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Farruko: La 167", ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private Albumes crearAlbumSech1(Artistas artista) {
        try {
            File imagenFile = new File("./images/suenossech.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("Sueños", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Sueños");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-19"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Otro Trago", "3:47"},
                            new String[]{"Solita", "3:46"},
                            new String[]{"Si Te Vas", "3:30"},
                            new String[]{"Que Más Pues", "3:28"},
                            new String[]{"Me Gustas", "3:52"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Sech: Sueños", ex);
            return null;
        }
    }

    private Albumes crearAlbumSech2(Artistas artista) {
        try {
            File imagenFile = new File("./images/1of1.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("1 of 1", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("1 of 1");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-21"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Relación", "3:31"},
                            new String[]{"Goteras", "3:54"},
                            new String[]{"Pantera", "2:50"},
                            new String[]{"911", "3:20"},
                            new String[]{"Confía", "3:42"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Sech: 1 of 1", ex);
            return null;
        }
    }

    private Albumes crearAlbumSech3(Artistas artista) {
        try {
            File imagenFile = new File("./images/42.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("42", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("42");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2021-04-15"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"911 Remix", "3:56"},
                            new String[]{"Pata' Abajo", "3:20"},
                            new String[]{"Sal y Perrea", "3:11"},
                            new String[]{"Playa", "3:38"},
                            new String[]{"Wao", "3:25"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Sech: 42", ex);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Albumes crearAlbumDannyOcean1(Artistas artista) {
        try {
            File imagenFile = new File("./images/54plus1.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("54+1", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("54+1");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-22"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Dembow", "4:11"},
                            new String[]{"Epa Wei", "3:35"},
                            new String[]{"Swing", "3:10"},
                            new String[]{"Veneno", "3:25"},
                            new String[]{"Tú", "3:40"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Danny Ocean: 54+1", ex);
            return null;
        }
    }

    private Albumes crearAlbumDannyOcean2(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/dannyoceanalbum.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("@dannocean", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("@dannocean");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2022-03-25"));
            album.setGenero(Arrays.asList("Reggaeton"));
            album.setImagenPortada(portada);
            album.setArtistaId(artista.getId());

            // Crear canciones
            List<DetallesCancion> canciones = this.crearCanciones(
                    Arrays.asList(
                            new String[]{"Fuera del Mercado", "3:25"},
                            new String[]{"Tú No Me Conoces", "3:14"},
                            new String[]{"Pronto", "2:59"},
                            new String[]{"Apartamento", "3:48"},
                            new String[]{"Rubia Sol Morena Luna", "3:33"},
                            new String[]{"Veneno", "3:16"},
                            new String[]{"3 de Febrero", "2:50"},
                            new String[]{"La Bella y la Bestia", "3:20"},
                            new String[]{"Cuántas Veces", "3:31"},
                            new String[]{"Me Rehúso", "3:24"},
                            new String[]{"Volvamos a Caer", "2:46"},
                            new String[]{"Tú", "3:30"},
                            new String[]{"Call Me", "3:35"},
                            new String[]{"Que Lo Que", "2:57"},
                            new String[]{"Agua", "2:45"},
                            new String[]{"Todo Va a Estar Bien", "3:27"},
                            new String[]{"Dembow", "3:15"},
                            new String[]{"Flow", "2:50"},
                            new String[]{"Sirena", "3:18"},
                            new String[]{"Relax", "3:20"},
                            new String[]{"Romance", "2:56"},
                            new String[]{"Cositas", "3:14"},
                            new String[]{"Mensaje de Voz", "3:12"},
                            new String[]{"Amor Prohibido", "3:30"},
                            new String[]{"Adiós", "3:25"}
                    ),
                    album.getId(),
                    portada
            );
            album.setDetallesCanciones(canciones);

            return album;
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error al crear álbum de Danny Ocean: @dannocean", ex);
            return null;
        }
    }

}
