/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;
import java.text.ParseException;
import java.util.List;
import objetos.Artistas;
import objetos.Integrantes;
import org.bson.types.ObjectId;

/**
 *
 * @author Chris
 */
public interface IArtistaDAO {

    public void insertarArtistasMasivamente(List<Artistas> artistas) throws PersistenciaException;
    
    public Artistas crearArtista(String nombre, String tipo, String genero, String nombreImagen, String rutaImagen, List<Integrantes> integrantes);
    
    public List<Integrantes> crearIntegrantes(String... datosIntegrantes) throws ParseException;

    public List<Artistas> buscarArtistasPorNombre(String nombre) throws PersistenciaException;
    
    public List<Artistas> obtenerCincoArtistas() throws PersistenciaException;

    public List<Artistas> buscarArtistasPorGenero(String genero) throws PersistenciaException;

    public List<Integrantes> obtenerIntegrantesActivos(ObjectId artistaId) throws PersistenciaException;

    public List<Integrantes> obtenerIntegrantesInactivos(ObjectId artistaId) throws PersistenciaException;

    public void agregarAFavoritos(ObjectId usuarioId, ObjectId artistaId) throws PersistenciaException;

    public List<Artistas> obtenerArtistasFavoritos(ObjectId usuarioId) throws PersistenciaException;

    public boolean verificarArtistaConGenero(ObjectId artistaId, List<String> generosNoDeseados) throws PersistenciaException;

    List<String> obtenerGenerosDeArtistas() throws PersistenciaException;

    List<Artistas> obtenerTodos() throws PersistenciaException;
}
