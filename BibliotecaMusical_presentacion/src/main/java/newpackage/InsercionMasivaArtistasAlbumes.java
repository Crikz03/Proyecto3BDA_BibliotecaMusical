/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
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
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Artistas;
import objetos.Integrantes;
import org.bson.types.ObjectId;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

public class InsercionMasivaArtistasAlbumes {

    private static final String DB_NAME = "BibliotecaMusical";
    private static final String COLLECTION_ARTISTAS = "artistas";
    private static final String COLLECTION_ALBUMES = "albumes";

    public static void main(String[] args) {
        // Crear datos de artistas y álbumes
        List<Document> documentosArtistas = new ArrayList<>();
        List<Document> documentosAlbumes = new ArrayList<>();

        // Generar datos para artistas y sus álbumes
        generarDatosArtistasYAlbumes(documentosArtistas, documentosAlbumes);

        // Realizar la inserción masiva
        insertarDocumentos(COLLECTION_ARTISTAS, documentosArtistas);
        insertarDocumentos(COLLECTION_ALBUMES, documentosAlbumes);
    }

    private static void generarDatosArtistasYAlbumes(List<Document> documentosArtistas, List<Document> documentosAlbumes) {
        try {
            Random random = new Random();
            
            // Crear 3 artistas (bandas)
            List<Artistas> artistas = new ArrayList<>();
            
            // Lista para los documentos a insertar
        List<Document> documentos = new ArrayList<>();
            
            // Agregar Green Day
            File imagenGreenDay = new File("images/fotoGreenday.jpg");  // Modifica la ruta aquí
            Imagen imagenGreenDayObj = GestorImagenesMongo.crearImagen("green_day.png", imagenGreenDay);
            Artistas greenDay = new Artistas();
            greenDay.setNombre("Green Day");
            greenDay.setTipo("Banda");
            greenDay.setGenero("Punk Rock");
            greenDay.setImagen(imagenGreenDayObj);
            
            // Agregar Twenty One Pilots
            File imagenTwentyOnePilots = new File("images/fotoTop.jpg");  // Modifica la ruta aquí
            Imagen imagenTOPObj = GestorImagenesMongo.crearImagen("twenty_one_pilots.png", imagenTwentyOnePilots);
            Artistas twentyOnePilots = new Artistas();
            twentyOnePilots.setNombre("Twenty One Pilots");
            twentyOnePilots.setTipo("Banda");
            twentyOnePilots.setGenero("Alternative Hip-Hop");
            twentyOnePilots.setImagen(imagenTOPObj);
            
            // Agregar The Beatles
            File imagenBeatles = new File("images/fotoBeatle.jpg");  // Modifica la ruta aquí
            Imagen imagenBeatlesObj = GestorImagenesMongo.crearImagen("the_beatles.png", imagenBeatles);
            Artistas beatles = new Artistas();
            beatles.setNombre("The Beatles");
            beatles.setTipo("Banda");
            beatles.setGenero("Rock");
            beatles.setImagen(imagenBeatlesObj);
            
            // Agregar los artistas a la lista
            artistas.add(greenDay);
            artistas.add(twentyOnePilots);
            artistas.add(beatles);
            
            // Crear documentos para artistas y álbumes
            for (Artistas artista : artistas) {
                // Crear documento del artista
                ObjectId artistaId = new ObjectId();
                Document documentoArtista = new Document("_id", artistaId)
                        .append("nombre", artista.getNombre())
                        .append("tipo", artista.getTipo())
                        .append("genero", artista.getGenero());
                        // Procesar la imagen
            Imagen imagen = artista.getImagen();
            if (imagen != null) {
                Document imagenDocument = new Document();
                imagenDocument.append("fileName", imagen.getFileName());
                imagenDocument.append("imageData", imagen.getImageData());
                documentoArtista.append("imagen", imagenDocument);
            }

            // Agregar el documento a la lista de documentos
            documentos.add(documentoArtista);
                
                // Agregar el documento del artista a la lista
                documentosArtistas.add(documentoArtista);
                
                // Crear 2 álbumes para cada artista
                List<Document> albumes = generarAlbumes(artistaId, random);
                documentosAlbumes.addAll(albumes); // Agregar los álbumes a la lista general
            }
        } catch (IOException ex) {
            Logger.getLogger(InsercionMasivaArtistasAlbumes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static List<Document> generarAlbumes(ObjectId artistaId, Random random) {
        List<Document> albumes = new ArrayList<>();

        // Crear 2 álbumes por artista
        for (int i = 1; i <= 2; i++) {
            try {
                String nombreAlbum = "Álbum " + i + " del Artista " + artistaId;
                
                File imagenAlbum = new File("images/albumAmerican.jpg");  // Modifica la ruta aquí
                Imagen imagenAlbumObj = GestorImagenesMongo.crearImagen("albumAmerican.jpg", imagenAlbum);
                
                List<Document> canciones = generarCanciones(nombreAlbum, random, imagenAlbumObj);
                
                Document album = new Document()
                        .append("nombre", nombreAlbum)
                        .append("fechaLanzamiento", new Date())
                        .append("genero", generarGenerosAlbum(random))
                        .append("artistaId", artistaId) // Relación con el artista
                        .append("imagen", imagenAlbumObj) // Imagen del álbum
                        .append("detallesCanciones", canciones);
                albumes.add(album);
            } catch (IOException ex) {
                Logger.getLogger(InsercionMasivaArtistasAlbumes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return albumes;
    }

    private static List<Document> generarCanciones(String nombreAlbum, Random random, Imagen imagenAlbum) {
        List<Document> canciones = new ArrayList<>();

        // Crear 5 canciones por álbum
        for (int i = 1; i <= 5; i++) {
            int duracion = random.nextInt(300) + 180; // Duración entre 3 y 8 minutos
            Document cancion = new Document()
                    .append("titulo", "Canción " + i + " del " + nombreAlbum)
                    .append("duracion", duracion)
                    .append("duracionFormateada", getDuracionFormateada(duracion)) // Duración formateada
                    .append("imagen", imagenAlbum);  // Imagen del álbum asignada a la canción
            canciones.add(cancion);
        }

        return canciones;
    }

    private static String getDuracionFormateada(int duracion) {
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%d:%02d", minutos, segundos);
    }

    private static String generarGeneroAleatorio(Random random) {
        String[] generos = {"Rock", "Pop", "Jazz", "Hip-Hop", "Clásica", "Reggae"};
        return generos[random.nextInt(generos.length)];
    }

    private static List<String> generarGenerosAlbum(Random random) {
        List<String> generos = new ArrayList<>();
        generos.add(generarGeneroAleatorio(random));
        if (random.nextBoolean()) { // A veces añadir un segundo género
            generos.add(generarGeneroAleatorio(random));
        }
        return generos;
    }

    private static void insertarDocumentos(String collectionName, List<Document> documentos) {
        try (MongoClient cliente = new MongoClient("localhost", 27017)) {
            MongoDatabase baseDeDatos = cliente.getDatabase(DB_NAME);
            MongoCollection<Document> coleccion = baseDeDatos.getCollection(collectionName);

            coleccion.insertMany(documentos);

            System.out.println("Inserción masiva completada en la colección: " + collectionName);
        }
    }
}


