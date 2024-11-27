/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IDetallesCancionDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import objetos.DetallesCancion;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class DetallesCancionDAO implements IDetallesCancionDAO {

    private final MongoCollection<DetallesCancion> coleccionCanciones;

    public DetallesCancionDAO() {
        this.coleccionCanciones = ConexionBD.crearConexion().getCollection("detallesCanciones", DetallesCancion.class);
    }

    /**
     *
     * @param cancion
     * @throws PersistenciaException
     */
    @Override
    public void insertarCancion(DetallesCancion cancion) throws PersistenciaException {
        try {
            if (cancion == null || cancion.getTitulo() == null || cancion.getDuracion() == null) {
                throw new PersistenciaException("Los datos de la canción no pueden estar incompletos.");
            }
            coleccionCanciones.insertOne(cancion);
        } catch (PersistenciaException e) {
            throw new PersistenciaException("Error al insertar la canción: " + e.getMessage());
        }
    }

    /**
     *
     * @param titulo
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<DetallesCancion> buscarPorTitulo(String titulo) throws PersistenciaException {
        try {
            Bson filtro = Filters.regex("titulo", Pattern.compile(titulo, Pattern.CASE_INSENSITIVE));
            return coleccionCanciones.find(filtro).into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar canciones por título: " + e.getMessage());
        }
    }



    /**
     *
     * @param artistaId
     * @return
     * @throws PersistenciaException
     */
    @Override
    public List<DetallesCancion> buscarPorArtista(ObjectId artistaId) throws PersistenciaException {
        try {
            Bson filtro = Filters.eq("artista._id", artistaId);
            return coleccionCanciones.find(filtro).into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar canciones por artista: " + e.getMessage());
        }
    }


}
