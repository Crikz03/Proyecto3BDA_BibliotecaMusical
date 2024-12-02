/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import conversiones.ConvertidorGeneral;
import datos.AlbumDAO;
import dto.AlbumDTO;
import dto.DetallesCancionDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IAlbumBO;
import interfaces.IAlbumDAO;
import java.util.Date;
import java.util.List;
import objetos.Albumes;
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
            List<Albumes> albumes = albumDAO.buscarPorNombre(nombre);
            return ConvertidorGeneral.convertidoraListaDTO(albumes, AlbumDTO.class);
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
            List<Albumes> albumes = albumDAO.buscarPorGenero(genero);
            return ConvertidorGeneral.convertidoraListaDTO(albumes, AlbumDTO.class);
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

            List<Albumes> albumes = albumDAO.buscarPorFechaLanzamiento(fechaInicio, fechaFin);
            return ConvertidorGeneral.convertidoraListaDTO(albumes, AlbumDTO.class);
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
            return ConvertidorGeneral.convertidoraListaDTO(albumes, AlbumDTO.class);
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
            return ConvertidorGeneral.convertidoraListaDTO(albumes, AlbumDTO.class);
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

}
