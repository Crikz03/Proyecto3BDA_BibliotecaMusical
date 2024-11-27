/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

import java.util.Objects;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

/**
 *
 * @author Chris
 */
public class Imagen {

    /**
     * Id de la Imagen.
     */
    private ObjectId id;
    /**
     * Nombre del archivo.
     */
    private String fileName;
    /**
     * Datos de la imagen.
     */
    private Binary imageData;

    /**
     * Constructor vacío.
     */
    public Imagen() {
    }

    /**
     * Obtiene la id de la imágen.
     *
     * @return id de la imagen.
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Establece la id de la imágen.
     *
     * @param id
     */
    public void setId(final ObjectId id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la imágen.
     *
     * @return el nombre de la imágen.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Establece el nombre de la imágen.
     *
     * @param fileName
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Obtiene los datos de la imágen.
     *
     * @return los datos de la imágen.
     */
    public Binary getImageData() {
        return imageData;
    }

    /**
     * Establece los datos de la imágen.
     *
     * @param imageData
     */
    public void setImageData(final Binary imageData) {
        this.imageData = imageData;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Imagen other = (Imagen) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Image{" + "fileName=" + fileName + '}';
    }
}
