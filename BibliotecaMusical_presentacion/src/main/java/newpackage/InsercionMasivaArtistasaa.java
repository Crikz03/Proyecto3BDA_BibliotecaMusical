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
import datos.ArtistaDAO;
import excepciones.PersistenciaException;
import interfaces.IArtistaDAO;
import org.bson.Document;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

public class InsercionMasivaArtistasaa {

    private static final String DB_NAME = "BibliotecaMusical";
    private static final String COLLECTION_NAME = "artistas";

    public static void main(String[] args) throws PersistenciaException {
        // Lista de artistas con sus datos y rutas de imagen
        ArtistaDAO artisdao = new ArtistaDAO();
        
        

        List<Artistas> artistas = artisdao.obtenerSeisArtistas(); // Método que preparas con los datos de los artistas

        // Lista de álbumes con sus imágenes y canciones
        List<Albumes> albums = obtenerAlbumesConImagenesYCanciones(); // Método para obtener los álbumes y canciones

        // Lista para los documentos de artistas a insertar
        List<Document> documentosArtistas = new ArrayList<>();

        // Recorrer la lista de artistas
        for (Artistas artista : artistas) {
            // Crear un nuevo documento para cada artista
            Document documentoArtista = new Document();
            documentoArtista.append("nombre", artista.getNombre());
            documentoArtista.append("tipo", artista.getTipo());
            documentoArtista.append("genero", artista.getGenero());

            // Procesar la imagen del artista
            Imagen imagenArtista = artista.getImagen();
            if (imagenArtista != null) {
                Document imagenDocumentArtista = new Document();
                imagenDocumentArtista.append("fileName", imagenArtista.getFileName());
                imagenDocumentArtista.append("imageData", imagenArtista.getImageData());
                documentoArtista.append("imagen", imagenDocumentArtista);
            }

            // Agregar el documento de artista a la lista de documentos
            documentosArtistas.add(documentoArtista);
        }

        // Lista para los documentos de álbumes a insertar
        List<Document> documentosAlbumes = new ArrayList<>();

        // Recorrer la lista de álbumes
        for (Albumes album : albums) {
            // Crear un nuevo documento para cada álbum
            Document documentoAlbum = new Document();
            documentoAlbum.append("nombre", album.getNombre());
            documentoAlbum.append("añoLanzamiento", album.getFechaLanzamiento());
            documentoAlbum.append("artista_id", album.getArtistaId());

            // Procesar la imagen del álbum
            Imagen imagenAlbum = album.getImagenPortada();
            if (imagenAlbum != null) {
                Document imagenDocumentAlbum = new Document();
                imagenDocumentAlbum.append("fileName", imagenAlbum.getFileName());
                imagenDocumentAlbum.append("imageData", imagenAlbum.getImageData());
                documentoAlbum.append("imagen", imagenDocumentAlbum);
            }

            // Agregar las canciones al documento del álbum
            List<Document> documentosCanciones = new ArrayList<>();
            for (DetallesCancion cancion : album.getDetallesCanciones()) {
                Document documentoCancion = new Document();
                documentoCancion.append("nombre", cancion.getTitulo());
                documentoCancion.append("duracion", cancion.getDuracionFormateada()); // Llamamos al método que formatea la duración

                // Asociar la imagen de la canción
                if (cancion.getFotoAlbum() != null) {
                    Document imagenDocumentCancion = new Document();
                    imagenDocumentCancion.append("fileName", cancion.getFotoAlbum().getFileName());
                    imagenDocumentCancion.append("imageData", cancion.getFotoAlbum().getImageData());
                    documentoCancion.append("imagen", imagenDocumentCancion);
                }

                // Agregar la canción al listado de canciones
                documentosCanciones.add(documentoCancion);
            }

            // Añadir las canciones al documento del álbum
            documentoAlbum.append("canciones", documentosCanciones);

            // Agregar el documento del álbum a la lista de documentos
            documentosAlbumes.add(documentoAlbum);
        }

        // Realizar la inserción masiva en MongoDB
        insertarArtistasMasivos(documentosArtistas);
        insertarAlbumesMasivos(documentosAlbumes);
    }

// Método para insertar múltiples artistas en MongoDB
    private static void insertarArtistasMasivos(List<Document> documentos) {
        MongoClient cliente = new MongoClient("localhost", 27017); // Conexión a MongoDB
        MongoDatabase baseDeDatos = cliente.getDatabase("BibliotecaMusical");
        MongoCollection<Document> coleccion = baseDeDatos.getCollection("artistas");

        // Insertar los documentos de artistas
        coleccion.insertMany(documentos);
        System.out.println("Inserción masiva de artistas completada");
    }

// Método para insertar múltiples álbumes en MongoDB
    private static void insertarAlbumesMasivos(List<Document> documentos) {
        MongoClient cliente = new MongoClient("localhost", 27017); // Conexión a MongoDB
        MongoDatabase baseDeDatos = cliente.getDatabase("BibliotecaMusical");
        MongoCollection<Document> coleccion = baseDeDatos.getCollection("albumes");

        // Insertar los documentos de álbumes
        coleccion.insertMany(documentos);
        System.out.println("Inserción masiva de álbumes completada");
    }

// Método para preparar una lista de álbumes con imágenes y canciones (como ejemplo)
    private static List<Albumes> obtenerAlbumesConImagenesYCanciones() {
        List<Albumes> albums = new ArrayList<>();
        try {
            // Crear la imagen para el álbum
            File imagenAlbum1 = new File("images/albumAmerican.jpg");
            Imagen imagenAlbumObj1 = GestorImagenesMongo.crearImagen("albumAmerican.jpg", imagenAlbum1);


            // Crear las canciones
            DetallesCancion cancion1 = new DetallesCancion();
            cancion1.setTitulo("Canción 1");
            cancion1.setDuracion(210); // Duración en segundos
            cancion1.setFotoAlbum(imagenAlbumObj1);

            DetallesCancion cancion2 = new DetallesCancion();
            cancion2.setTitulo("Canción 2");
            cancion2.setDuracion(180); // Duración en segundos
            cancion2.setFotoAlbum(imagenAlbumObj1);

            // Crear el álbum y asignar las canciones
            Albumes album1 = new Albumes();
            album1.setNombre("Álbum 1");
            album1.setFechaLanzamiento(new Date());
            album1.setImagenPortada(imagenAlbumObj1);
            album1.setDetallesCanciones(Arrays.asList(cancion1, cancion2));

            albums.add(album1);

            // Agregar más álbumes aquí si es necesario...
        } catch (IOException e) {
            e.printStackTrace();
        }

        return albums;
    }

}
