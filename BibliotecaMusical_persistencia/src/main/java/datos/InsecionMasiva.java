/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IAlbumDAO;
import interfaces.IArtistaDAO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import org.bson.Document;

/**
 *
 * @author Chris
 */
public class InsecionMasiva {

    private final MongoCollection<Artistas> coleccionArtistas;
    private final MongoCollection<Albumes> coleccionAlbumes;
    private IArtistaDAO artistadao;
    private IAlbumDAO albumdao;

    public InsecionMasiva() {
        this.coleccionArtistas = ConexionBD.crearConexion().getCollection("artistas", Artistas.class);
        this.coleccionAlbumes = ConexionBD.crearConexion().getCollection("albumes", Albumes.class);
        this.artistadao = new ArtistaDAO();
        this.albumdao = new AlbumDAO();
    }

    public void insertarArtistasMasivos() {
        try {
            List<Artistas> artistas = obtenerArtistasConImagenes();

            if (!artistas.isEmpty()) {
                coleccionArtistas.insertMany(artistas);
                System.out.println("Inserción masiva de artistas completada. Total de artistas insertados: " + artistas.size());
            } else {
                System.out.println("No hay artistas para insertar.");
            }
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error en la inserción masiva de artistas", ex);
        }
    }
    
    public void insertarAlbumesMasivos() {
        try {
            // Encontrar todos los artistas insertados
            List<Artistas> artistas = coleccionArtistas.find().into(new ArrayList<>());

            // Lista para almacenar los álbumes a insertar
            List<Albumes> albumesAInsertar = new ArrayList<>();

            // Iterar sobre los artistas para crear álbumes
            for (Artistas artista : artistas) {
                // Crear álbumes según el artista
                albumesAInsertar.addAll(this.albumdao.crearAlbumesParaArtista(artista));
            }

            // Insertar álbumes en la base de datos
            if (!albumesAInsertar.isEmpty()) {
                coleccionAlbumes.insertMany(albumesAInsertar);
                System.out.println("Inserción masiva de álbumes completada. Total de álbumes insertados: " + albumesAInsertar.size());
            } else {
                System.out.println("No hay álbumes para insertar.");
            }
        } catch (Exception ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, "Error en la inserción masiva de álbumes", ex);
        }
    }

    private List<Artistas> obtenerArtistasConImagenes() {
        try {
            List<Artistas> artistas = new ArrayList<>();

            // Ejemplo de banda con integrantes
            artistas.add(artistadao.crearArtista(
                    "Coldplay",
                    "Banda",
                    "Rock Alternativo",
                    "coldplay.jpg",
                    "./images/jbalvin.jpg",
                    artistadao.crearIntegrantes(
                            "Chris Martin", "Vocalista", "1996-01-01", "null", "true",
                            "Jonny Buckland", "Guitarrista", "1996-01-01", "null", "true",
                            "Guy Berryman", "Bajista", "1996-01-01", "null", "true",
                            "Will Champion", "Baterista", "1996-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Arctic Monkeys",
                    "Banda",
                    "Rock Indie",
                    "arcticmonkeys.jpg",
                    "./images/jbalvin.jpg",
                    artistadao.crearIntegrantes(
                            "Alex Turner", "Vocalista", "2002-01-01", "null", "true",
                            "Matt Helders", "Baterista", "2002-01-01", "null", "true",
                            "Jamie Cook", "Guitarrista", "2002-01-01", "null", "true",
                            "Nick O'Malley", "Bajista", "2006-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "The Beatles",
                    "Banda",
                    "Rock",
                    "beatles.jpg",
                    "./images/beatle.jpg",
                    artistadao.crearIntegrantes(
                            "John Lennon", "Vocalista/Guitarrista", "1960-01-01", "1980-12-08", "false",
                            "Paul McCartney", "Vocalista/Bajista", "1960-01-01", "null", "true",
                            "George Harrison", "Guitarrista", "1960-01-01", "2001-11-29", "false",
                            "Ringo Starr", "Baterista", "1962-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Green Day",
                    "Banda",
                    "Punk Rock",
                    "greenday.jpg",
                    "./images/greenday.jpg",
                    artistadao.crearIntegrantes(
                            "Billie Joe Armstrong", "Vocalista/Guitarrista", "1987-01-01", "null", "true",
                            "Mike Dirnt", "Bajista", "1987-01-01", "null", "true",
                            "Tré Cool", "Baterista", "1990-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Twenty One Pilots",
                    "Banda",
                    "Alternative Rock",
                    "twentyonepilots.jpg",
                    "./images/top.jpg",
                    artistadao.crearIntegrantes(
                            "Tyler Joseph", "Vocalista/Teclados", "2009-01-01", "null", "true",
                            "Josh Dun", "Baterista", "2011-01-01", "null", "true"
                    )
            ));

            // Continúa agregando más bandas con sus integrantes...
            // Artistas solistas se mantienen igual
            artistas.add(artistadao.crearArtista("Taylor Swift", "Solista", "Pop", "taylorswift.jpg", "./images/jbalvin.jpg", null));
            // ... resto de artistas solistas ...

            return artistas;
        } catch (ParseException ex) {
            Logger.getLogger(InsecionMasiva.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

}
