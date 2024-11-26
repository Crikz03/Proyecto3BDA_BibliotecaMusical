/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebas;

import datos.ArtistaDAO;
import excepciones.PersistenciaException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import objetos.Artistas;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class pruebaInsercion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArtistaDAO artistasDAO = new ArtistaDAO();

        Imagen imagenArtista = new Imagen();
        imagenArtista.setFileName("imagenJuanPérez.jpg");
        try {
            // Leer la imagen desde un archivo y convertirla a Binary
            File file = new File("/Pictures/Screenshots/yomigod");  // Cambia la ruta al archivo de la imagen
            byte[] imageBytes = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(imageBytes);
            fileInputStream.close();
            imagenArtista.setImageData(new Binary(imageBytes));  // Convertir a Binary

        } catch (IOException e) {
            System.err.println("Error al leer la imagen: " + e.getMessage());
        }
       

        // Crear algunos artistas para insertar
        Artistas artista1 = new Artistas();
        artista1.setId(new ObjectId());  // Generar un ObjectId único para el artista
        artista1.setNombre("Juan Pérez");
        artista1.setTipo("Solista");
        artista1.setGenero("Rock");
        artista1.setImagen(imagenArtista);

        Artistas artista2 = new Artistas();
        artista2.setId(new ObjectId());  // Generar un ObjectId único para el artista
        artista2.setNombre("Ana García");
        artista2.setTipo("Solista");
        artista2.setGenero("Pop");
        artista2.setImagen(imagenArtista);

        Artistas artista3 = new Artistas();
        artista3.setId(new ObjectId());  // Generar un ObjectId único para el artista
        artista3.setNombre("La Banda X");
        artista3.setTipo("Banda");
        artista3.setGenero("Jazz");
        artista3.setImagen(imagenArtista);

        // Crear una lista de artistas
        List<Artistas> listaArtistas = Arrays.asList(artista1, artista2, artista3);

        try {
            // Llamar al método de inserción masiva
            artistasDAO.insertarArtistasMasivamente(listaArtistas);
            System.out.println("Los artistas se han insertado correctamente.");

        } catch (PersistenciaException e) {
            System.err.println("Error al insertar los artistas: " + e.getMessage());
        }
    }

}
