/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import conversiones.ConvertidorGeneral;
import datos.ArtistaDAO;
import dto.ArtistaDTO;
import dto.IntegrantesDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IArtistaBO;
import interfaces.IArtistaDAO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Artistas;
import objetos.Integrantes;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class ArtistaBO implements IArtistaBO {

    private final IArtistaDAO artistaDAO;

    public ArtistaBO() {
        this.artistaDAO = new ArtistaDAO();
    }

    /**
     * Inserta artistas masivamente en la base de datos.
     *
     * @param artistasDTO Lista de artistas en formato DTO.
     * @throws NegocioException Si hay un error de negocio o persistencia.
     */
    @Override
    public void insertarArtistasMasivamente(List<ArtistaDTO> artistasDTO) throws NegocioException {
        try {
            if (artistasDTO == null || artistasDTO.isEmpty()) {
                throw new NegocioException("La lista de artistas no puede estar vacía.");
            }

            // Convertir lista de DTOs a entidades
            List<Artistas> artistas = ConvertidorGeneral.convertidorListaEntidad(artistasDTO, Artistas.class);

            // Llamar al DAO para la inserción masiva
            artistaDAO.insertarArtistasMasivamente(artistas);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al insertar artistas masivamente: " + e.getMessage(), e);
        }
    }

    /**
     * Busca artistas por nombre.
     *
     * @param nombre El nombre parcial o completo del artista.
     * @return Lista de artistas encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<ArtistaDTO> buscarArtistasPorNombre(String nombre) throws NegocioException {
        try {
            List<Artistas> artistas = artistaDAO.buscarArtistasPorNombre(nombre);
            return ConvertidorGeneral.convertidoraListaDTO(artistas, ArtistaDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar artistas por nombre: " + e.getMessage(), e);
        }
    }

    /**
     * Busca artistas por género musical.
     *
     * @param genero El género musical.
     * @return Lista de artistas encontrados en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<ArtistaDTO> buscarArtistasPorGenero(String genero) throws NegocioException {
        try {
            List<Artistas> artistas = artistaDAO.buscarArtistasPorGenero(genero);
            return ConvertidorGeneral.convertidoraListaDTO(artistas, ArtistaDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar artistas por género: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los integrantes activos de un artista.
     *
     * @param idArtista ID del artista.
     * @return Lista de integrantes activos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<IntegrantesDTO> obtenerIntegrantesActivos(ObjectId idArtista) throws NegocioException {
        try {
            List<Integrantes> integrantes = artistaDAO.obtenerIntegrantesActivos(idArtista);
            return ConvertidorGeneral.convertidoraListaDTO(integrantes, IntegrantesDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener integrantes activos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los integrantes inactivos de un artista.
     *
     * @param idArtista ID del artista.
     * @return Lista de integrantes inactivos en formato DTO.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public List<IntegrantesDTO> obtenerIntegrantesInactivos(ObjectId idArtista) throws NegocioException {
        try {
            List<Integrantes> integrantes = artistaDAO.obtenerIntegrantesInactivos(idArtista);
            return ConvertidorGeneral.convertidoraListaDTO(integrantes, IntegrantesDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener integrantes inactivos: " + e.getMessage(), e);
        }
    }

    /**
     * Agrega un artista a los favoritos de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @param idArtista ID del artista.
     * @throws NegocioException Si ocurre un error de negocio o persistencia.
     */
    @Override
    public void agregarArtistaAFavoritos(ObjectId idUsuario, ObjectId idArtista) throws NegocioException {
        try {
            artistaDAO.agregarAFavoritos(idUsuario, idArtista);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar artista a favoritos: " + e.getMessage(), e);
        }
    }

    public List<String> obtenerGenerosDeArtistas() throws PersistenciaException {
        List<Artistas> artistas = artistaDAO.obtenerTodos(); // Recupera todos los artistas
        Set<String> generos = new HashSet<>();
        for (Artistas artista : artistas) {
            if (artista.getGenero() != null) {
                generos.add(artista.getGenero()); // Agrega géneros únicos
            }
        }
        return new ArrayList<>(generos);
    }

}
