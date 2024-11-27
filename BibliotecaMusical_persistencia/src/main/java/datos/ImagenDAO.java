/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import com.mongodb.client.MongoCollection;
import conexion.ConexionBD;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class ImagenDAO {
     private final MongoCollection<Imagen> coleccionImagenes;

    public ImagenDAO() {
        this.coleccionImagenes = ConexionBD.crearConexion().getCollection("imagenes", Imagen.class);
    }
}
