/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IIntegrantesDAO;
import java.util.List;
import java.util.stream.Collectors;
import objetos.Artistas;
import objetos.Integrantes;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public class IntegrantesDAO implements IIntegrantesDAO {

    private final MongoCollection<Artistas> coleccionArtistas;

    public IntegrantesDAO() {
        this.coleccionArtistas = ConexionBD.crearConexion().getCollection("artistas", Artistas.class);
    }

    @Override
    public void agregarIntegrante(ObjectId idArtista, Integrantes integrante) throws PersistenciaException {
        try {
            if (integrante == null || idArtista == null) {
                throw new PersistenciaException("El integrante o el ID del artista no pueden ser nulos.");
            }
            Bson filtro = Filters.eq("_id", idArtista);
            Bson update = Updates.push("integrantes", integrante);
            coleccionArtistas.updateOne(filtro, update);
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar el integrante: " + e.getMessage());
        }
    }

    @Override
    public List<Integrantes> obtenerIntegrantesActivos(ObjectId idArtista) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", idArtista)).first();
            if (artista == null) {
                throw new PersistenciaException("El artista con ID: " + idArtista + " no existe.");
            }
            return artista.getIntegrantes().stream().filter(Integrantes::isActivo).collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener los integrantes activos: " + e.getMessage());
        }
    }

    @Override
    public List<Integrantes> obtenerIntegrantesInactivos(ObjectId idArtista) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", idArtista)).first();
            if (artista == null) {
                throw new PersistenciaException("El artista con ID: " + idArtista + " no existe.");
            }
            return artista.getIntegrantes().stream().filter(i -> !i.isActivo()).collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener los integrantes inactivos: " + e.getMessage());
        }
    }

}

