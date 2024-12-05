/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import interfaces.IAlbumDAO;
import interfaces.IArtistaDAO;
import interfaces.IinsercionMasivaDAO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import objetos.Albumes;
import objetos.Artistas;

/**
 *
 * @author Chris
 */
public class InsercionMasivaDAO implements IinsercionMasivaDAO {

    private final MongoCollection<Artistas> coleccionArtistas;
    private final MongoCollection<Albumes> coleccionAlbumes;
    private IArtistaDAO artistadao;
    private IAlbumDAO albumdao;

    public InsercionMasivaDAO() {
        this.coleccionArtistas = ConexionBD.crearConexion().getCollection("artistas", Artistas.class);
        this.coleccionAlbumes = ConexionBD.crearConexion().getCollection("albumes", Albumes.class);
        this.artistadao = new ArtistaDAO();
        this.albumdao = new AlbumDAO();
    }

    @Override
    public void insertarArtistasMasivos() throws PersistenciaException {
        try {
            List<Artistas> artistas = obtenerArtistasConImagenes();

            if (!artistas.isEmpty()) {
                // Filtrar artistas que ya están en la base de datos
                List<Artistas> artistasNuevos = artistas.stream()
                        .filter(artista -> coleccionArtistas.countDocuments(Filters.eq("nombre", artista.getNombre())) == 0)
                        .collect(Collectors.toList());

                if (!artistasNuevos.isEmpty()) {
                    coleccionArtistas.insertMany(artistasNuevos);
                    System.out.println("Inserción masiva de artistas completada. Total de artistas insertados: " + artistasNuevos.size());
                } else {
                    System.out.println("Todos los artistas ya están insertados.");
                }
            } else {
                System.out.println("No hay artistas para insertar.");
            }
        } catch (MongoException ex) {
            throw new PersistenciaException("Error al insertar los 60 artistas.");
        }
    }

    @Override
    public void insertarAlbumesMasivos() throws PersistenciaException {
        try {
            List<Artistas> artistas = coleccionArtistas.find().into(new ArrayList<>());

            if (artistas.isEmpty()) {
                System.out.println("No hay artistas en la base de datos para asociar álbumes.");
                return;
            }

            List<Albumes> albumesAInsertar = new ArrayList<>();

            for (Artistas artista : artistas) {
                List<Albumes> albumesDelArtista = this.albumdao.crearAlbumesParaArtista(artista);

                // Filtrar álbumes que ya están en la base de datos
                List<Albumes> albumesNuevos = albumesDelArtista.stream()
                        .filter(album -> coleccionAlbumes.countDocuments(Filters.and(
                        Filters.eq("nombre", album.getNombre()),
                        Filters.eq("artistaId", artista.getId())
                )) == 0)
                        .collect(Collectors.toList());

                albumesAInsertar.addAll(albumesNuevos);
            }

            if (!albumesAInsertar.isEmpty()) {
                coleccionAlbumes.insertMany(albumesAInsertar);
                System.out.println("Inserción masiva de álbumes completada. Total de álbumes insertados: " + albumesAInsertar.size());
            } else {
                System.out.println("Todos los álbumes ya están insertados.");
            }
        } catch (MongoException ex) {
            throw new PersistenciaException("Error al insertar los álbumes a los 60 artistas.");
        }
    }

    private List<Artistas> obtenerArtistasConImagenes() {
        try {
            List<Artistas> artistas = new ArrayList<>();

            // Banda con integrantes (20)
            
            artistas.add(artistadao.crearArtista(
                    "Coldplay",
                    "Banda",
                    "Rock Alternativo",
                    "coldplay.jpg",
                    "./images/coldplay.jpg",
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
                    "./images/arcticmonkeys.jpg",
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
                    "thebeatles.jpg",
                    "./images/thebeatles.jpg",
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
                    "./images/twentyonepilots.jpg",
                    artistadao.crearIntegrantes(
                            "Tyler Joseph", "Vocalista/Teclados", "2009-01-01", "null", "true",
                            "Josh Dun", "Baterista", "2011-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Queen",
                    "Banda",
                    "Rock",
                    "queen.jpg",
                    "./images/queen.jpg",
                    artistadao.crearIntegrantes(
                            "Freddie Mercury", "Vocalista", "1970-01-01", "1991-11-24", "false",
                            "Brian May", "Guitarrista", "1970-01-01", "null", "true",
                            "Roger Taylor", "Baterista", "1970-01-01", "null", "true",
                            "John Deacon", "Bajista", "1971-01-01", "1997-01-01", "false"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "The Rolling Stones",
                    "Banda",
                    "Rock",
                    "rollingstones.jpg",
                    "./images/rollingstones.jpg",
                    artistadao.crearIntegrantes(
                            "Mick Jagger", "Vocalista", "1962-01-01", "null", "true",
                            "Keith Richards", "Guitarrista", "1962-01-01", "null", "true",
                            "Charlie Watts", "Baterista", "1963-01-01", "2021-08-24", "false",
                            "Ronnie Wood", "Guitarrista", "1975-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Nirvana",
                    "Banda",
                    "Grunge",
                    "nirvana.jpg",
                    "./images/nirvana.jpg",
                    artistadao.crearIntegrantes(
                            "Kurt Cobain", "Vocalista/Guitarrista", "1987-01-01", "1994-04-05", "false",
                            "Krist Novoselic", "Bajista", "1987-01-01", "1994-01-01", "false",
                            "Dave Grohl", "Baterista", "1990-01-01", "1994-01-01", "false"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Red Hot Chili Peppers",
                    "Banda",
                    "Funk Rock",
                    "redhotchilipeppers.jpg",
                    "./images/redhotchilipeppers.jpg",
                    artistadao.crearIntegrantes(
                            "Anthony Kiedis", "Vocalista", "1983-01-01", "null", "true",
                            "Flea", "Bajista", "1983-01-01", "null", "true",
                            "John Frusciante", "Guitarrista", "1988-01-01", "null", "true",
                            "Chad Smith", "Baterista", "1988-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Imagine Dragons",
                    "Banda",
                    "Pop Rock",
                    "imaginedragons.jpg",
                    "./images/imaginedragons.jpg",
                    artistadao.crearIntegrantes(
                            "Dan Reynolds", "Vocalista", "2008-01-01", "null", "true",
                            "Wayne Sermon", "Guitarrista", "2008-01-01", "null", "true",
                            "Ben McKee", "Bajista", "2009-01-01", "null", "true",
                            "Daniel Platzman", "Baterista", "2011-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "OneRepublic",
                    "Banda",
                    "Pop Rock",
                    "onerepublic.jpg",
                    "./images/onerepublic.jpg",
                    artistadao.crearIntegrantes(
                            "Ryan Tedder", "Vocalista", "2002-01-01", "null", "true",
                            "Zach Filkins", "Guitarrista", "2002-01-01", "null", "true",
                            "Drew Brown", "Guitarrista", "2002-01-01", "null", "true",
                            "Brent Kutzle", "Bajista/Cellista", "2007-01-01", "null", "true",
                            "Eddie Fisher", "Baterista", "2005-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Maroon 5",
                    "Banda",
                    "Pop Rock",
                    "maroon5.jpg",
                    "./images/maroon5.jpg",
                    artistadao.crearIntegrantes(
                            "Adam Levine", "Vocalista", "1994-01-01", "null", "true",
                            "James Valentine", "Guitarrista", "2001-01-01", "null", "true",
                            "Jesse Carmichael", "Tecladista/Guitarrista", "1994-01-01", "null", "true",
                            "Mickey Madden", "Bajista", "1994-01-01", "2020-01-01", "false",
                            "Matt Flynn", "Baterista", "2006-01-01", "null", "true",
                            "PJ Morton", "Tecladista", "2012-01-01", "null", "true",
                            "Sam Farrar", "Multiinstrumentista", "2016-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Morat",
                    "Banda",
                    "Pop Latino/Folk",
                    "morat.jpg",
                    "./images/morat.jpg",
                    artistadao.crearIntegrantes(
                            "Juan Pablo Villamil", "Vocalista/Guitarrista", "2015-01-01", "null", "true",
                            "Juan Pablo Isaza", "Vocalista/Tecladista", "2015-01-01", "null", "true",
                            "Simón Vargas", "Bajista", "2015-01-01", "null", "true",
                            "Martín Vargas", "Baterista", "2015-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Maná",
                    "Banda",
                    "Rock en Español",
                    "mana.jpg",
                    "./images/mana.jpg",
                    artistadao.crearIntegrantes(
                            "Fher Olvera", "Vocalista", "1986-01-01", "null", "true",
                            "Alex González", "Baterista", "1986-01-01", "null", "true",
                            "Sergio Vallín", "Guitarrista", "1994-01-01", "null", "true",
                            "Juan Calleros", "Bajista", "1986-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Aventura",
                    "Banda",
                    "Bachata",
                    "aventura.jpg",
                    "./images/aventuraa.jpg",
                    artistadao.crearIntegrantes(
                            "Romeo Santos", "Vocalista", "1994-01-01", "null", "true",
                            "Henry Santos", "Vocalista/Guitarrista", "1994-01-01", "null", "true",
                            "Max Santos", "Bajista", "1994-01-01", "null", "true",
                            "Lenny Santos", "Guitarrista", "1994-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Los Ángeles Azules",
                    "Banda",
                    "Cumbia",
                    "losangelesazules.jpg",
                    "./images/losangelesazules.jpg",
                    artistadao.crearIntegrantes(
                            "Jorge Mejía Avante", "Tecladista", "1976-01-01", "null", "true",
                            "Elías Mejía Avante", "Bajista", "1976-01-01", "null", "true",
                            "Alfredo Mejía Avante", "Guitarrista", "1976-01-01", "null", "true",
                            "Cristina Mejía Avante", "Vocalista", "1976-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Enanitos Verdes",
                    "Banda",
                    "Rock en Español",
                    "enanitosverdes.jpg",
                    "./images/enanitosverdes.jpg",
                    artistadao.crearIntegrantes(
                            "Marciano Cantero", "Vocalista/Bajista", "1979-01-01", "null", "true",
                            "Felipe Staiti", "Guitarrista", "1979-01-01", "null", "true",
                            "Daniel Piccolo", "Baterista", "1979-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Caifanes",
                    "Banda",
                    "Rock en Español",
                    "caifanes.jpg",
                    "./images/caifanes.jpg",
                    artistadao.crearIntegrantes(
                            "Saúl Hernández", "Vocalista", "1987-01-01", "null", "true",
                            "Sabo Romo", "Bajista", "1987-01-01", "null", "true",
                            "Alfonso André", "Baterista", "1987-01-01", "null", "true",
                            "Diego Herrera", "Tecladista", "1987-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Panda",
                    "Banda",
                    "Rock Alternativo",
                    "panda.jpg",
                    "./images/panda.jpg",
                    artistadao.crearIntegrantes(
                            "José Madero", "Vocalista", "1996-01-01", "null", "true",
                            "Arturo Arredondo", "Guitarrista", "1996-01-01", "null", "true",
                            "Ricardo Treviño", "Bajista", "1996-01-01", "null", "true",
                            "Jorge Vásquez", "Baterista", "1996-01-01", "null", "true"
                    )
            ));

            artistas.add(artistadao.crearArtista(
                    "Allison",
                    "Banda",
                    "Pop Punk",
                    "allison.jpg",
                    "./images/allison.jpg",
                    artistadao.crearIntegrantes(
                            "Erik Canales", "Vocalista/Guitarrista", "2002-01-01", "null", "true",
                            "Abraham Isael Jarquín", "Bajista", "2002-01-01", "null", "true",
                            "Diego Stommel", "Baterista", "2002-01-01", "null", "true",
                            "Óscar Barba", "Guitarrista", "2005-01-01", "null", "true"
                    )
            ));



            // Artistas solistas (40)
            artistas.add(artistadao.crearArtista("Bad Bunny", "Solista", "Reggaeton", "badbunny.jpg", "./images/badbunny.jpg", null));
            artistas.add(artistadao.crearArtista("Dua Lipa", "Solista", "Pop", "dualipa.jpg", "./images/dualipa.jpg", null));
            artistas.add(artistadao.crearArtista("Taylor Swift", "Solista", "Pop", "taylorswift.jpg", "./images/taylorswift.jpg", null));
            artistas.add(artistadao.crearArtista("The Weeknd", "Solista", "R&B", "theweeknd.jpg", "./images/theweeknd.jpg", null));
            artistas.add(artistadao.crearArtista("Billie Eilish", "Solista", "Alternative Pop", "billieeilish.jpg", "./images/billieeilish.jpg", null));
            artistas.add(artistadao.crearArtista("Harry Styles", "Solista", "Pop Rock", "harrystyles.jpg", "./images/harrystyles.jpg", null));
            artistas.add(artistadao.crearArtista("Ed Sheeran", "Solista", "Pop", "edsheeran.jpg", "./images/edsheeran.jpg", null));
            artistas.add(artistadao.crearArtista("Adele", "Solista", "Soul", "adele.jpg", "./images/adele.jpg", null));
            artistas.add(artistadao.crearArtista("Rosalía", "Solista", "Flamenco Pop", "rosalia.jpg", "./images/rosalia.jpg", null));
            artistas.add(artistadao.crearArtista("Shakira", "Solista", "Pop Latino", "shakira.jpg", "./images/shakira.jpg", null));
            artistas.add(artistadao.crearArtista("Karol G", "Solista", "Reggaeton", "karolg.jpg", "./images/karolg.jpg", null));
            artistas.add(artistadao.crearArtista("Olivia Rodrigo", "Solista", "Pop Rock", "oliviarodrigo.jpg", "./images/oliviarodrigo.jpg", null));
            artistas.add(artistadao.crearArtista("Bruno Mars", "Solista", "Funk", "brunomars.jpg", "./images/brunomars.jpg", null));
            artistas.add(artistadao.crearArtista("Ariana Grande", "Solista", "Pop", "arianagrande.jpg", "./images/arianagrande.jpg", null));
            artistas.add(artistadao.crearArtista("J Balvin", "Solista", "Reggaeton", "jbalvin.jpg", "./images/jbalvin.jpg", null));
            artistas.add(artistadao.crearArtista("Elvis Presley", "Solista", "Rock & Roll", "elvispresley.jpg", "./images/elvispresley.jpg", null));
            artistas.add(artistadao.crearArtista("Bob Dylan", "Solista", "Folk Rock", "bobdylan.jpg", "./images/bobdylan.jpg", null));
            artistas.add(artistadao.crearArtista("Janis Joplin", "Solista", "Blues Rock", "janisjoplin.jpg", "./images/janisjoplin.jpg", null));
            artistas.add(artistadao.crearArtista("Jimi Hendrix", "Solista", "Psychedelic Rock", "jimihendrix.jpg", "./images/jimihendrix.jpg", null));
            artistas.add(artistadao.crearArtista("David Bowie", "Solista", "Glam Rock", "davidbowie.jpg", "./images/davidbowie.jpg", null));
            artistas.add(artistadao.crearArtista("Prince", "Solista", "Funk Rock", "prince.jpg", "./images/prince.jpg", null));
            artistas.add(artistadao.crearArtista("Freddie Mercury", "Solista", "Rock", "freddiemercury.jpg", "./images/freddiemercury.jpg", null));
            artistas.add(artistadao.crearArtista("Michael Jackson", "Solista", "Pop", "michaeljackson.jpg", "./images/michaeljackson.jpg", null));
            artistas.add(artistadao.crearArtista("Amy Winehouse", "Solista", "Jazz Pop", "amywinehouse.jpg", "./images/amywinehouse.jpg", null));
            artistas.add(artistadao.crearArtista("Chris Stapleton", "Solista", "Country Rock", "chrisstapleton.jpg", "./images/chrisstapleton.jpg", null));
            artistas.add(artistadao.crearArtista("Sam Smith", "Solista", "Pop", "samsmith.jpg", "./images/samsmith.jpg", null));
            artistas.add(artistadao.crearArtista("Hozier", "Solista", "Indie Rock", "hozier.jpg", "./images/hozier.jpg", null));
            artistas.add(artistadao.crearArtista("Lana Del Rey", "Solista", "Indie Pop", "lanadelrey.jpg", "./images/lanadelrey.jpg", null));
            artistas.add(artistadao.crearArtista("Post Malone", "Solista", "Hip Hop", "postmalone.jpg", "./images/postmalone.jpg", null));
            artistas.add(artistadao.crearArtista("Shawn Mendes", "Solista", "Pop", "shawnmendes.jpg", "./images/shawnmendes.jpg", null));
            artistas.add(artistadao.crearArtista("Miley Cyrus", "Solista", "Pop Rock", "mileycyrus.jpg", "./images/mileycyrus.jpg", null));/////////************hasta aqui voy
            artistas.add(artistadao.crearArtista("Daddy Yankee", "Solista", "Reggaeton", "daddyyankee.jpg", "./images/daddyyankee.jpg", null));
            artistas.add(artistadao.crearArtista("Ozuna", "Solista", "Reggaeton", "ozuna.jpg", "./images/ozuna.jpg", null));
            artistas.add(artistadao.crearArtista("Anuel AA", "Solista", "Reggaeton", "anuelaa.jpg", "./images/anuelaa.jpg", null));
            artistas.add(artistadao.crearArtista("Maluma", "Solista", "Reggaeton", "maluma.jpg", "./images/maluma.jpg", null));
            artistas.add(artistadao.crearArtista("Natti Natasha", "Solista", "Reggaeton", "nattinatasha.jpg", "./images/nattinatasha.jpg", null));
            artistas.add(artistadao.crearArtista("Becky G", "Solista", "Reggaeton", "beckyg.jpg", "./images/beckyg.jpg", null));
            artistas.add(artistadao.crearArtista("Farruko", "Solista", "Reggaeton", "farruko.jpg", "./images/farruko.jpg", null));
            artistas.add(artistadao.crearArtista("Sech", "Solista", "Reggaeton", "sech.jpg", "./images/sech.jpg", null));
            artistas.add(artistadao.crearArtista("Danny Ocean", "Solista", "Reggaeton", "dannyocean.jpg", "./images/dannyocean.jpg", null));

            return artistas;
        } catch (ParseException ex) {
            Logger.getLogger(InsercionMasivaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

}
