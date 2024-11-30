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

    @Override
    public List<Albumes> crearAlbumesParaArtista(Artistas artista) {
        List<Albumes> albumes = new ArrayList<>();

        // Ejemplos de creación de álbumes para diferentes artistas
        switch (artista.getNombre()) {
            case "Coldplay":
                albumes.add(crearAlbumColdplay(artista));
                break;
            case "Arctic Monkeys":
                albumes.add(crearAlbumArcticMonkeys(artista));
                break;
            case "The Beatles":
                albumes.add(crearAlbumTheBeatles(artista));
                break;
            case "Green Day":
                albumes.add(crearAlbumGreenDay(artista));
                break;
            case "Twenty One Pilots":
                albumes.add(crearAlbumTwentyOnePilots(artista));
                break;
            // Añade más casos para otros artistas
        }

        return albumes;
    }

    private Albumes crearAlbumColdplay(Artistas artista) {
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

    private Albumes crearAlbumArcticMonkeys(Artistas artista) {
        try {
            // Crear imagen de portada
            File imagenFile = new File("./images/am.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("am_artic", imagenFile);

            // Crear álbum
            Albumes album = new Albumes();
            album.setNombre("AM");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2013-09-09"));
            album.setGenero(Arrays.asList("Rock Indie", "Alternative Rock"));
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

    // Implementa métodos similares para los demás artistas
    private Albumes crearAlbumTheBeatles(Artistas artista) {
        // Similar a los métodos anteriores
        // Por ejemplo, para el álbum "Abbey Road"
        try {
            File imagenFile = new File("./images/abbey_road.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("abbey_road", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Abbey Road");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("1969-09-26"));
            album.setGenero(Arrays.asList("Rock", "Pop Rock"));
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

    private Albumes crearAlbumGreenDay(Artistas artista) {
        // Álbum "American Idiot"
        try {
            File imagenFile = new File("./images/american_idiot.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("american_idiot", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("American Idiot");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2004-09-21"));
            album.setGenero(Arrays.asList("Punk Rock", "Rock Alternativo"));
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

    private Albumes crearAlbumTwentyOnePilots(Artistas artista) {
        // Álbum "Blurryface"
        try {
            File imagenFile = new File("./images/blurryface.jpg");
            Imagen portada = GestorImagenesMongo.crearImagen("blurryface", imagenFile);

            Albumes album = new Albumes();
            album.setNombre("Blurryface");
            album.setFechaLanzamiento(new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-19"));
            album.setGenero(Arrays.asList("Alternative Rock", "Indie Pop"));
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
}
