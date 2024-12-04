/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IArtistaDAO;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import objetos.Artistas;
import objetos.Integrantes;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class ArtistaDAO implements IArtistaDAO {

    private final MongoCollection<Artistas> coleccionArtistas;

    public ArtistaDAO() {
        this.coleccionArtistas = ConexionBD.crearConexion().getCollection("artistas", Artistas.class);
    }

    @Override
    public Artistas crearArtista(String nombre, String tipo, String genero, String nombreImagen, String rutaImagen, List<Integrantes> integrantes) {
        try {
            File imagenFile = new File(rutaImagen);
            Imagen imagenObj = GestorImagenesMongo.crearImagen(nombreImagen, imagenFile);

            Artistas artista = new Artistas();
            artista.setNombre(nombre);
            artista.setTipo(tipo);
            artista.setGenero(genero);
            artista.setImagen(imagenObj);

            if (tipo.equals("Banda") && integrantes != null && !integrantes.isEmpty()) {
                artista.setIntegrantes(integrantes);
            }

            return artista;
        } catch (IOException ex) {
            Logger.getLogger(ArtistaDAO.class.getName()).log(Level.SEVERE, "Error al crear artista: " + nombre, ex);
            return null;
        }
    }

    @Override
    public List<Integrantes> crearIntegrantes(String... datosIntegrantes) throws ParseException {
        List<Integrantes> integrantes = new ArrayList<>();

        for (int i = 0; i < datosIntegrantes.length; i += 5) {
            try {
                Integrantes integrante = new Integrantes();
                integrante.setNombreCompleto(datosIntegrantes[i]);
                integrante.setRol(datosIntegrantes[i + 1]);

                // Parsear fechas si no son nulas
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                integrante.setFechaIngreso(datosIntegrantes[i + 2].equals("null") ? null : sdf.parse(datosIntegrantes[i + 2]));
                integrante.setFechaSalida(datosIntegrantes[i + 3].equals("null") ? null : sdf.parse(datosIntegrantes[i + 3]));

                integrante.setActivo(Boolean.parseBoolean(datosIntegrantes[i + 4]));

                integrantes.add(integrante);
            } catch (MongoException ex) {
                Logger.getLogger(ArtistaDAO.class.getName()).log(Level.SEVERE, "Error al crear integrante", ex);
            }
        }

        return integrantes;
    }

    @Override
    public void insertarArtistasMasivamente(List<Artistas> artistas) throws PersistenciaException {
        try {
            if (artistas == null || artistas.isEmpty()) {
                throw new PersistenciaException("La lista de artistas no puede estar vacía.");
            }

            for (Artistas artista : artistas) {
                if (artista.getNombre() == null || artista.getNombre().isEmpty()) {
                    throw new PersistenciaException("El nombre del artista no puede ser nulo o vacío.");
                }
                if (artista.getTipo() == null || artista.getTipo().isEmpty()) {
                    throw new PersistenciaException("El tipo de artista no puede ser nulo o vacío.");
                }
                if (artista.getGenero() == null || artista.getGenero().isEmpty()) {
                    throw new PersistenciaException("El género del artista no puede ser nulo o vacío.");
                }
            }

            this.coleccionArtistas.insertMany(artistas);

        } catch (MongoException e) {
            throw new PersistenciaException("No se ha podido realizar la inserción masiva de artistas: " + e);
        }
    }

    @Override
    /*public List<Artistas> buscarArtistasPorNombre(String nombre) throws PersistenciaException {
        try {
            return coleccionArtistas.find(Filters.regex("nombre", Pattern.compile(nombre, Pattern.CASE_INSENSITIVE)))
                    .into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar artistas por nombre: " + e.getMessage());
        }
    }*/
    public List<Artistas> buscarArtistasPorNombre(String pattern) throws PersistenciaException {
    try {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("El patrón de búsqueda no puede ser nulo o vacío");
        }
        
        Bson filtro = Filters.regex("nombre", Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
        return coleccionArtistas.find(filtro).into(new ArrayList<>());
    } catch (Exception e) {
        throw new PersistenciaException("Error al buscar artistas por nombre: " + e.getMessage());
    }
}

    @Override
    public List<Artistas> obtenerCincoArtistas() throws PersistenciaException {
        try {
            return coleccionArtistas.find()
                    .limit(5) // Limita el resultado a 6 documentos
                    .into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener 6 artistas: " + e.getMessage());
        }
    }

    @Override
    public List<Artistas> buscarArtistasPorGenero(String genero) throws PersistenciaException {
        try {
            return coleccionArtistas.find(Filters.eq("genero", genero)).into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar artistas por género: " + e.getMessage());
        }
    }

    @Override
    public List<Integrantes> obtenerIntegrantesActivos(ObjectId artistaId) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", artistaId)).first();
            if (artista == null) {
                throw new PersistenciaException("No se encontró el artista con ID: " + artistaId);
            }
            return artista.getIntegrantes().stream().filter(Integrantes::isActivo).collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener integrantes activos: " + e.getMessage());
        }
    }

    @Override
    public List<Integrantes> obtenerIntegrantesInactivos(ObjectId artistaId) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", artistaId)).first();
            if (artista == null) {
                throw new PersistenciaException("No se encontró el artista con ID: " + artistaId);
            }
            return artista.getIntegrantes().stream().filter(i -> !i.isActivo()).collect(Collectors.toList());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener integrantes inactivos: " + e.getMessage());
        }
    }

    @Override
    public void agregarAFavoritos(ObjectId usuarioId, ObjectId artistaId) throws PersistenciaException {
        try {
            Bson filtroUsuario = Filters.eq("_id", usuarioId);
            Bson updateFavorito = Updates.push("favoritos.artistas", artistaId);
            coleccionArtistas.updateOne(filtroUsuario, updateFavorito);
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar el artista a favoritos: " + e.getMessage());
        }
    }

    @Override
    public List<Artistas> obtenerArtistasFavoritos(ObjectId usuarioId) throws PersistenciaException {
        try {
            return coleccionArtistas.find(Filters.in("_id", usuarioId)).into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener artistas favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean verificarArtistaConGenero(ObjectId artistaId, List<String> generosNoDeseados) throws PersistenciaException {
        try {
            Artistas artista = coleccionArtistas.find(Filters.eq("_id", artistaId)).first();
            if (artista == null) {
                throw new PersistenciaException("No se encontró el artista con ID: " + artistaId);
            }
            return generosNoDeseados.contains(artista.getGenero());
        } catch (Exception e) {
            throw new PersistenciaException("Error al verificar el género del artista: " + e.getMessage());
        }
    }

    public List<String> obtenerGenerosDeArtistas() throws PersistenciaException {
        try {
            // Recuperar todos los artistas
            FindIterable<Artistas> artistas = coleccionArtistas.find();
            Set<String> generosUnicos = new HashSet<>();

            // Extraer géneros únicos
            for (Artistas artista : artistas) {
                if (artista.getGenero() != null && !artista.getGenero().isEmpty()) {
                    generosUnicos.add(artista.getGenero());
                }
            }

            // Retornar los géneros como lista
            return new ArrayList<>(generosUnicos);
        } catch (Exception e) {
            // Lanzar PersistenciaException si ocurre algún error
            throw new PersistenciaException("Error al obtener géneros de los artistas: " + e.getMessage());
        }
    }
    
    
    @Override
    public List<Artistas> obtenerTodos() throws PersistenciaException {
    try {
        // Devuelve la lista de todos los artistas en la colección
        return coleccionArtistas.find().into(new ArrayList<>());
    } catch (Exception e) {
        throw new PersistenciaException("Error al obtener todos los artistas: " + e.getMessage());
    }
}
    
    
    public Artistas buscarPorId(ObjectId idArtista) throws PersistenciaException {
    try {
        // Busca el artista por ID en la base de datos
        Artistas artista = coleccionArtistas.find(Filters.eq("_id", idArtista)).first();
        if (artista == null) {
            throw new PersistenciaException("No se encontró el artista con ID: " + idArtista);
        }
        return artista;
    } catch (Exception e) {
        throw new PersistenciaException("Error al buscar artista por ID: " + e.getMessage());
    }
}
}
