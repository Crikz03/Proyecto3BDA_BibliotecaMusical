/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import org.bson.Document;

/**
 *
 * @author Chris
 */
public class InsecionMasiva {

    private final MongoCollection<Document> coleccionArtistas;
    private final MongoCollection<Document> coleccionAlbumes;

    public InsecionMasiva() {
        this.coleccionArtistas = ConexionBD.crearConexion().getCollection("artistas");
        this.coleccionAlbumes = ConexionBD.crearConexion().getCollection("albumes");
    }

    public void insertarArtistasYAlbumes() throws PersistenciaException {
        try {
            // Green Day
            Document greenDay = new Document("nombre", "Green Day")
                    .append("tipo", "Banda")
                    .append("genero", "Punk Rock");

            Document americanIdiot = new Document("nombre", "American Idiot")
                    .append("artista", "Green Day") // Nombre del artista para referencia
                    .append("fechaLanzamiento", new Date(110, 8, 21)) // Septiembre 21, 2004
                    .append("canciones", List.of(
                            new Document("titulo", "American Idiot").append("duracion", 182),
                            new Document("titulo", "Boulevard of Broken Dreams").append("duracion", 261)
                    ));

            Document dookie = new Document("nombre", "Dookie")
                    .append("artista", "Green Day")
                    .append("fechaLanzamiento", new Date(94, 1, 1)) // Febrero 1, 1994
                    .append("canciones", List.of(
                            new Document("titulo", "Basket Case").append("duracion", 183),
                            new Document("titulo", "When I Come Around").append("duracion", 177)
                    ));

            // Relacionar los álbumes con el artista Green Day
            greenDay.append("albumes", List.of(americanIdiot.get("_id"), dookie.get("_id")));

            // Insertar los documentos de Green Day, los álbumes y las canciones en las colecciones respectivas
            coleccionArtistas.insertOne(greenDay);
            coleccionAlbumes.insertMany(List.of(americanIdiot, dookie));

            // The Weeknd
            Document theWeeknd = new Document("nombre", "The Weeknd")
                    .append("tipo", "Solista")
                    .append("genero", "R&B");

            Document afterHours = new Document("nombre", "After Hours")
                    .append("artista", "The Weeknd")
                    .append("fechaLanzamiento", new Date(120, 2, 20)) // Marzo 20, 2020
                    .append("canciones", List.of(
                            new Document("titulo", "Blinding Lights").append("duracion", 200),
                            new Document("titulo", "Save Your Tears").append("duracion", 215)
                    ));

            Document starboy = new Document("nombre", "Starboy")
                    .append("artista", "The Weeknd")
                    .append("fechaLanzamiento", new Date(116, 10, 25)) // Noviembre 25, 2016
                    .append("canciones", List.of(
                            new Document("titulo", "Starboy").append("duracion", 230),
                            new Document("titulo", "I Feel It Coming").append("duracion", 270)
                    ));

            // Relacionar los álbumes con The Weeknd
            theWeeknd.append("albumes", List.of(afterHours.get("_id"), starboy.get("_id")));

            // Insertar los documentos de The Weeknd y los álbumes en las colecciones respectivas
            coleccionArtistas.insertOne(theWeeknd);
            coleccionAlbumes.insertMany(List.of(afterHours, starboy));

            // Twenty One Pilots
            Document twentyOnePilots = new Document("nombre", "Twenty One Pilots")
                    .append("tipo", "Banda")
                    .append("genero", "Alternative Rock");

            Document blurryface = new Document("nombre", "Blurryface")
                    .append("artista", "Twenty One Pilots")
                    .append("fechaLanzamiento", new Date(115, 4, 17)) // Mayo 17, 2015
                    .append("canciones", List.of(
                            new Document("titulo", "Stressed Out").append("duracion", 202),
                            new Document("titulo", "Ride").append("duracion", 212)
                    ));

            Document trench = new Document("nombre", "Trench")
                    .append("artista", "Twenty One Pilots")
                    .append("fechaLanzamiento", new Date(118, 9, 5)) // Octubre 5, 2018
                    .append("canciones", List.of(
                            new Document("titulo", "Jumpsuit").append("duracion", 240),
                            new Document("titulo", "My Blood").append("duracion", 210)
                    ));

            // Relacionar los álbumes con Twenty One Pilots
            twentyOnePilots.append("albumes", List.of(blurryface.get("_id"), trench.get("_id")));

            // Insertar los documentos de Twenty One Pilots y los álbumes en las colecciones respectivas
            coleccionArtistas.insertOne(twentyOnePilots);
            coleccionAlbumes.insertMany(List.of(blurryface, trench));

            System.out.println("Inserción de artistas y álbumes completada.");
        } catch (MongoException e) {
            throw new PersistenciaException("No se ha podido realizar la inserción masiva de artistas y álbumes: " + e);
        }
    }

}
