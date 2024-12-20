/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import conversiones.ConvertidorGeneral;
import datos.AlbumDAO;
import dto.AlbumDTO;
import dto.ArtistaDTO;
import dto.DetallesCancionDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IAlbumBO;
import interfaces.IAlbumDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class AlbumBO implements IAlbumBO {

    private final IAlbumDAO albumDAO;

    public AlbumBO() {
        this.albumDAO = new AlbumDAO();
    }

    /**
     * Inserta un nuevo álbum en la base de datos.
     *
     * @param albumDTO El álbum a insertar en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public void insertarAlbum(AlbumDTO albumDTO) throws NegocioException {
        try {
            if (albumDTO == null || albumDTO.getNombre() == null || albumDTO.getFechaLanzamiento() == null) {
                throw new NegocioException("El álbum o sus campos obligatorios no pueden ser nulos.");
            }

            // Convertir DTO a entidad
            Albumes album = ConvertidorGeneral.convertidorEntidad(albumDTO, Albumes.class);

            // Llamar al DAO para insertar el álbum
            albumDAO.insertarAlbum(album);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al insertar el álbum: " + e.getMessage(), e);
        }
    }

    /**
     * Busca álbumes por nombre.
     *
     * @param nombre El nombre del álbum (completo o parcial).
     * @return Lista de álbumes encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<AlbumDTO> buscarPorNombre(String nombre) throws NegocioException {
        try {
            List<Albumes> albumesRelacionados = albumDAO.buscarPorNombre(nombre);
            List<AlbumDTO> albumes=new ArrayList<>();
            for (Albumes album : albumesRelacionados) {
                albumes.add(convertirAlbum(album));
            }
            return albumes;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar álbumes por nombre: " + e.getMessage(), e);
        }
    }

    /**
     * Busca álbumes por género musical.
     *
     * @param genero El género musical.
     * @return Lista de álbumes encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<AlbumDTO> buscarPorGenero(String genero) throws NegocioException {
        try {
            List<Albumes> albumesRelacionados = albumDAO.buscarPorGenero(genero);
            List<AlbumDTO> albumes=new ArrayList<>();
            for (Albumes album : albumesRelacionados) {
                albumes.add(convertirAlbum(album));
            }
            return albumes;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar álbumes por género: " + e.getMessage(), e);
        }
    }

    /**
     * Busca álbumes dentro de un rango de fechas de lanzamiento.
     *
     * @param fechaInicio Fecha inicial del rango.
     * @param fechaFin Fecha final del rango.
     * @return Lista de álbumes encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<AlbumDTO> buscarPorFechaLanzamiento(Date fechaInicio, Date fechaFin) throws NegocioException {
        try {
            if (fechaInicio == null || fechaFin == null) {
                throw new NegocioException("Las fechas de inicio y fin no pueden ser nulas.");
            }

            List<Albumes> albumesRelacionados = albumDAO.buscarPorFechaLanzamiento(fechaInicio, fechaFin);
            
            List<AlbumDTO> albumes=new ArrayList<>();
            for (Albumes album : albumesRelacionados) {
                albumes.add(convertirAlbum(album));
            }
            return albumes;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar álbumes por rango de fechas: " + e.getMessage(), e);
        }
    }

    /**
     * Busca álbumes asociados a un artista.
     *
     * @param idArtista ID del artista.
     * @return Lista de álbumes del artista en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<AlbumDTO> buscarPorArtista(ObjectId idArtista) throws NegocioException {
        try {
            List<Albumes> albumes = albumDAO.buscarPorArtista(idArtista);
            List<AlbumDTO> albumesDTO=new ArrayList<>();
            for (Albumes album : albumes) {
                albumesDTO.add(convertirAlbum(album));
            }
            return albumesDTO;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar álbumes por artista: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene las canciones de un álbum.
     *
     * @param idAlbum ID del álbum.
     * @return Lista de canciones del álbum en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<DetallesCancionDTO> obtenerCancionesDeAlbum(ObjectId idAlbum) throws NegocioException {
        try {
            List<DetallesCancion> detallesCanciones = albumDAO.obtenerCancionesDeAlbum(idAlbum);
            return ConvertidorGeneral.convertidoraListaDTO(detallesCanciones, DetallesCancionDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener canciones del álbum: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene cinco albumes de los artistas para mostrar.
     *
     * @return Lista de cinco albumes de artistas.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<AlbumDTO> obtenerCincoAlbumes() throws NegocioException {
        try {
            List<Albumes> albumes = albumDAO.obtenerCincoAlbumes();
            return ConvertidorGeneral.convertidoraListaDTO(albumes, AlbumDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener los cinco álbumes: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los albumes de los artistas para mostrar.
     *
     * @return Lista de los albumes de artistas.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<AlbumDTO> obtenerAlbumes() throws NegocioException {
        try {
            List<Albumes> albumes = albumDAO.obtenerAlbumes();
            return albumes.stream()
                    .map(this::convertirAlbum) // Usa el método convertirAlbum definido en esta clase
                    .collect(Collectors.toList());
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener los álbumes: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene las canciones de los albumes.
     *
     * @return Lista de las canciones de los albumes de diferentes artistas.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<DetallesCancionDTO> obtenerCancionesDeAlbumes() throws NegocioException {
        try {
            List<DetallesCancion> canciones = albumDAO.obtenerCancionesDeAlbumes();
            return ConvertidorGeneral.convertidoraListaDTO(canciones, DetallesCancionDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener canciones del álbum: " + e.getMessage(), e);
        }
    }

    public List<DetallesCancionDTO> obtenerCancionesDeAlbumes2() throws NegocioException {
        try {
            // Obtener todos los álbumes
            List<Albumes> albumes = albumDAO.obtenerAlbumes();

            // Lista para almacenar las canciones
            List<DetallesCancionDTO> canciones = new ArrayList<>();

            // Iterar sobre los álbumes para extraer las canciones
            for (Albumes album : albumes) {
                ObjectId artistaId = album.getArtistaId(); // Dejar como ObjectId
                for (DetallesCancion detalles : album.getDetallesCanciones()) {
                    DetallesCancionDTO cancionDTO = new DetallesCancionDTO();
                    cancionDTO.setTitulo(detalles.getTitulo());
                    cancionDTO.setDuracion(detalles.getDuracion());
                    cancionDTO.setIdArtista(artistaId); // Asociar el ObjectId del artista
                    cancionDTO.setFotoAlbum(detalles.getFotoAlbum());
                    canciones.add(cancionDTO);
                }
            }

            return canciones;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener canciones de álbumes: " + e.getMessage(), e);
        }
    }

    public List<DetallesCancionDTO> buscarCancionesPorNombre(String nombre) throws NegocioException {
        try {
            // Llamar al DAO para buscar canciones por nombre
            List<DetallesCancion> canciones = albumDAO.buscarCancionesPorNombre(nombre);

            // Convertir las canciones a DTO
            return convertirAListaDTO(canciones);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar canciones por nombre: " + e.getMessage(), e);
        }
    }

    private AlbumDTO convertirAlbum(Albumes album) {
        AlbumDTO albumDTO = new AlbumDTO();

        // Mapea los campos simples
        albumDTO.setId(album.getId());
        albumDTO.setNombre(album.getNombre());
        albumDTO.setFechaLanzamiento(album.getFechaLanzamiento());
        albumDTO.setGenero(album.getGenero());
        albumDTO.setImagenPortada(album.getImagenPortada());
        albumDTO.setArtistaId(album.getArtistaId());

         List<DetallesCancionDTO> canciones = new ArrayList<>();
        // Mapea las canciones
        if (album.getDetallesCanciones() != null) {
            for (DetallesCancion detalles : album.getDetallesCanciones()) {
                DetallesCancionDTO cancionDTO = new DetallesCancionDTO();
                cancionDTO.setTitulo(detalles.getTitulo());
                cancionDTO.setDuracion(detalles.getDuracion());
                cancionDTO.setIdArtista(album.getArtistaId()); // Asociar el ObjectId del artista
                cancionDTO.setFotoAlbum(detalles.getFotoAlbum());
                canciones.add(cancionDTO);
            }
            albumDTO.setCanciones(canciones);
        } else {
            albumDTO.setCanciones(new ArrayList<>());
        }
        

        return albumDTO;
    }

    // Método privado para convertir un objeto DetallesCancion a DetallesCancionDTO
    private DetallesCancionDTO convertirCancion(DetallesCancion cancion) {
        DetallesCancionDTO cancionDTO = new DetallesCancionDTO();

        // Mapea los campos de DetallesCancion a DetallesCancionDTO
        cancionDTO.setTitulo(cancion.getTitulo());
        cancionDTO.setDuracion(cancion.getDuracion());
        cancionDTO.setFotoAlbum(cancion.getFotoAlbum());
        // cancionDTO.setIdArtista(cancion.getIdArtista()); // Si está disponible

        return cancionDTO;
    }

    public List<DetallesCancionDTO> buscarCancionesPorArtista(ObjectId idArtista) throws NegocioException {
        try {
            List<Albumes> albumesDelArtistaa = albumDAO.buscarPorArtista(idArtista);
            List<DetallesCancionDTO> canciones = new ArrayList<>();

            // Iterar sobre los álbumes para extraer las canciones
            for (Albumes album : albumesDelArtistaa) {
                ObjectId artistaId = album.getArtistaId(); // Dejar como ObjectId
                for (DetallesCancion detalles : album.getDetallesCanciones()) {
                    DetallesCancionDTO cancionDTO = new DetallesCancionDTO();
                    cancionDTO.setTitulo(detalles.getTitulo());
                    cancionDTO.setDuracion(detalles.getDuracion());
                    cancionDTO.setIdArtista(artistaId); // Asociar el ObjectId del artista
                    cancionDTO.setFotoAlbum(detalles.getFotoAlbum());
                    canciones.add(cancionDTO);
                }
            }

            return canciones;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar canciones por artista: " + e.getMessage(), e);
        }
    }

    public List<AlbumDTO> buscarAlbumesPorNombreDeCancion(String nombreCancion) throws NegocioException {
        try {
            List<Albumes> albumesRelacionados = albumDAO.buscarAlbumesPorNombreDeCancion(nombreCancion);
            List<AlbumDTO> albumes=new ArrayList<>();
            for (Albumes album : albumesRelacionados) {
                albumes.add(convertirAlbum(album));
            }
            return albumes;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar álbumes por nombre de canción: " + e.getMessage(), e);
        }
    }

    public List<AlbumDTO> buscarPorNombreArtista(String nombreArtista) throws NegocioException {
        try {
            if (nombreArtista == null || nombreArtista.trim().isEmpty()) {
                throw new NegocioException("El nombre del artista no puede ser nulo o vacío.");
            }

            // Buscar artistas que coincidan con el nombre
            List<Artistas> artistasRelacionados = albumDAO.buscarArtistasPorNombre(nombreArtista);

            if (artistasRelacionados.isEmpty()) {
                throw new NegocioException("No se encontró ningún artista con el nombre: " + nombreArtista);
            }

            List<AlbumDTO> albumesRelacionados = new ArrayList<>();

            // Buscar álbumes de cada artista relacionado
            for (Artistas artista : artistasRelacionados) {
                if (artista.getId() != null) {
                    List<Albumes> albumesDelArtista = albumDAO.buscarPorArtista(artista.getId());
                    albumesRelacionados.addAll(ConvertidorGeneral.convertidoraListaDTO(albumesDelArtista, AlbumDTO.class));
                } else {
                    System.out.println("Artista sin ID: " + artista.getNombre());
                }
            }

            return albumesRelacionados;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar álbumes por nombre del artista: " + e.getMessage(), e);
        }
    }
    public static List<DetallesCancionDTO> convertirAListaDTO(List<DetallesCancion> canciones) {
        List<DetallesCancionDTO> cancionesDTO = new ArrayList<>();

        for (DetallesCancion cancion : canciones) {
            DetallesCancionDTO dto = new DetallesCancionDTO();
            dto.setTitulo(cancion.getTitulo());
            dto.setDuracion(cancion.getDuracion());
            dto.setIdArtista(cancion.getIdArtista());
            dto.setIdReferenciaAlbum(cancion.getIdReferenciaAlbum());
            dto.setFotoAlbum(cancion.getFotoAlbum());
            // El nombre del artista no está en DetallesCancion, deberá asignarse posteriormente si es necesario
            dto.setNombreArtista(null); 
            cancionesDTO.add(dto);
        }

        return cancionesDTO;
    }
}
