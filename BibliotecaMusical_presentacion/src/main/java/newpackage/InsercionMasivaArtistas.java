/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

/**
 *
 * @author Chris
 */
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import objetos.Artistas;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

public class InsercionMasivaArtistas {

    private static final String DB_NAME = "BibliotecaMusical";
    private static final String COLLECTION_NAME = "artistas";

    public static void main(String[] args) {
        // Lista de artistas con sus datos y rutas de imagen
        List<Artistas> artistas = obtenerArtistasConImagenes(); // Método que preparas con los datos de los artistas

        // Lista para los documentos a insertar
        List<Document> documentos = new ArrayList<>();

        // Recorrer la lista de artistas
        for (Artistas artista : artistas) {
            // Crear un nuevo documento para cada artista
            Document documento = new Document();
            documento.append("nombre", artista.getNombre());
            documento.append("tipo", artista.getTipo());
            documento.append("genero", artista.getGenero());
            
            // Procesar la imagen
            Imagen imagen = artista.getImagen();
            if (imagen != null) {
                Document imagenDocument = new Document();
                imagenDocument.append("fileName", imagen.getFileName());
                imagenDocument.append("imageData", imagen.getImageData());
                documento.append("imagen", imagenDocument);
            }

            // Agregar el documento a la lista de documentos
            documentos.add(documento);
        }

        // Realizar la inserción masiva en MongoDB
        insertarArtistasMasivos(documentos);
    }

    // Método para insertar múltiples documentos en MongoDB
    private static void insertarArtistasMasivos(List<Document> documentos) {
        MongoClient cliente = new MongoClient("localhost", 27017); // Conexión a MongoDB
        MongoDatabase baseDeDatos = cliente.getDatabase("BibliotecaMusical");
        MongoCollection<Document> coleccion = baseDeDatos.getCollection("artistas");
        
        // Insertar los documentos
        coleccion.insertMany(documentos);
        
        System.out.println("Inserción masiva completada");
    }

    // Método para preparar una lista de artistas con sus imágenes (como ejemplo)
    private static List<Artistas> obtenerArtistasConImagenes() {
        List<Artistas> artistas = new ArrayList<>();
        
        // Aquí deberías crear tus objetos de artistas. Ejemplo:
        try {
            File imagen1 = new File("C:/Users/Chris/Documents/GitHub/Proyecto3BDA_BibliotecaMusical/BibliotecaMusical_presentacion/images/yomigod.png");
            Imagen imagen1Obj = GestorImagenesMongo.crearImagen("yomigod.png", imagen1);
            
            Artistas artista1 = new Artistas();
            artista1.setNombre("Artista 3");
            artista1.setTipo("Tipo 1");
            artista1.setGenero("Género 1");
            artista1.setImagen(imagen1Obj);

            // Agregar el artista a la lista
            artistas.add(artista1);

            // Agregar más artistas aquí según sea necesario...

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return artistas;
    }
}

