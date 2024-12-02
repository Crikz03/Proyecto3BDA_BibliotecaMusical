/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import conversiones.ConvertidorGeneral;
import datos.UsuarioDAO;
import dto.FavoritoDTO;
import dto.UsuarioDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IUsuarioBO;
import java.util.List;
import interfaces.IUsuarioDAO;
import objetos.Favorito;
import objetos.Usuarios;
import org.bson.types.ObjectId;
import recursos.Encriptacion;
import recursos.tipoFavoritos;

/**
 *
 * @author pauli
 */
public class UsuarioBO implements IUsuarioBO {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean registrarUsuario(UsuarioDTO usuarioDTO) throws NegocioException {
        try {
            if (usuarioDTO == null || usuarioDTO.getNombreUsuario() == null || usuarioDTO.getCorreo() == null) {
                throw new NegocioException("El usuario, nombre de usuario o correo no pueden ser nulos.");
            }

            // Validar correo único
            List<Usuarios> usuarios = usuarioDAO.consultarTodos();////////////////////////////////////////////////////////////
            if (usuarios.stream().anyMatch(u -> u.getCorreo().equalsIgnoreCase(usuarioDTO.getCorreo()))) {
                throw new NegocioException("Ya existe un usuario registrado con este correo.");
            }

            // Convertir DTO a entidad usando ConvertidorGeneral
            Usuarios usuario = ConvertidorGeneral.convertidorEntidad(usuarioDTO, Usuarios.class);
            Encriptacion.encriptar(usuario.getContrasena());
            return usuarioDAO.agregar(usuario);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al registrar el usuario: " + e.getMessage(), e);
        }
    }

    public boolean actualizarUsuario(UsuarioDTO usuarioDTO) throws NegocioException {
        try {
            if (usuarioDTO == null || usuarioDTO.getId() == null) {
                throw new NegocioException("El usuario o su ID no pueden ser nulos.");
            }

            // Convertir DTO a entidad usando ConvertidorGeneral
            Usuarios usuario = ConvertidorGeneral.convertidorEntidad(usuarioDTO, Usuarios.class);
            return usuarioDAO.actualizar(usuario);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    /*
    public UsuarioDTO obtenerUsuarioPorId(ObjectId idUsuario) throws NegocioException {
        try {
            Usuarios usuario = usuarioDAO.consultar(new Usuarios(idUsuario));//////////////////////////////////////////////////////////
            if (usuario == null) {
                throw new NegocioException("No se encontró el usuario con el ID: " + idUsuario);
            }

            // Convertir entidad a DTO usando ConvertidorGeneral
            return ConvertidorGeneral.convertidoraDTO(usuario, UsuarioDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener el usuario por ID: " + e.getMessage(), e);
        }
    }*/
    public UsuarioDTO consultarUsuarioCorreo(String correo) throws NegocioException {
        if (correo == null || correo.isEmpty()) {
            throw new NegocioException("El correo del usuario es requerido para realizar la consulta.");
        }

        try {
            // Llama al DAO para obtener la entidad Usuarios
            Usuarios usuario = usuarioDAO.consultarCorreo(correo);

            if (usuario == null) {
                throw new NegocioException("No se encontró un usuario con el correo: " + correo);
            }

            // Convierte la entidad Usuarios a DTO usando el ConvertidorGeneral
            return ConvertidorGeneral.convertidoraDTO(usuario, UsuarioDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener el usuario por correo: " + e.getMessage(), e);
        }
    }

    public List<UsuarioDTO> obtenerTodosLosUsuarios() throws NegocioException {
        try {
            List<Usuarios> usuarios = usuarioDAO.consultarTodos();

            // Convertir lista de entidades a lista de DTOs usando ConvertidorGeneral
            return ConvertidorGeneral.convertidoraListaDTO(usuarios, UsuarioDTO.class);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener todos los usuarios: " + e.getMessage(), e);
        }
    }

    public boolean agregarAFavoritos(ObjectId idUsuario, FavoritoDTO favoritoDTO) throws NegocioException {
        try {
            // Convertir DTO a entidad usando ConvertidorGeneral
            Favorito favorito = ConvertidorGeneral.convertidorEntidad(favoritoDTO, Favorito.class);
            return usuarioDAO.agregarAFavoritos(idUsuario, favorito);////////////////////////////////////////////////////////////////////
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar el favorito: " + e.getMessage(), e);
        }
    }

    public void actualizarGenerosNoDeseados(ObjectId usuarioId, List<String> generosNoDeseados) throws PersistenciaException {
        usuarioDAO.actualizarGenerosNoDeseados(usuarioId, generosNoDeseados);
    }

    public UsuarioDTO consultarPorId(ObjectId idUsuario) throws PersistenciaException {
        try {
            // Obtener el usuario desde el DAO
            Usuarios usuarioEntidad = usuarioDAO.consultarPorId(idUsuario);

            if (usuarioEntidad == null) {
                throw new PersistenciaException("No se encontró un usuario con el ID proporcionado.");
            }

            // Convertir la entidad a DTO usando el convertidor genérico
            return ConvertidorGeneral.convertidoraDTO(usuarioEntidad, UsuarioDTO.class);
        } catch (PersistenciaException e) {
            throw e; // Re-lanzar excepciones de persistencia
        } catch (Exception e) {
            throw new PersistenciaException("Error inesperado al consultar el usuario: " + e.getMessage());
        }
    }
    
public List<String> consultarGenerosBaneados(ObjectId idUsuario) throws Exception {
    try {
        // Consultar usuario desde el DAO
        Usuarios usuario = usuarioDAO.consultarPorId(idUsuario);

        // Validar si el usuario existe
        if (usuario == null) {
            throw new Exception("Usuario no encontrado con ID: " + idUsuario);
        }

        // Devolver la lista de géneros baneados
        return usuario.getGenerosNoDeseados();
    } catch (Exception e) {
        throw new Exception("Error al consultar géneros baneados: " + e.getMessage(), e);
    }
}
    
    

    /*
    public boolean agregarGeneroNoDeseado(ObjectId idUsuario, String genero) throws NegocioException {
        try {
            return usuarioDAO.agregarGeneroNoDeseado(idUsuario, genero);//////////////////////////////////////////////////////////////
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar el género no deseado: " + e.getMessage(), e);
        }
    }

    public boolean eliminarGeneroNoDeseado(ObjectId idUsuario, String genero) throws NegocioException {
        try {
            return usuarioDAO.eliminarGeneroNoDeseado(idUsuario, genero);/////////////////////////////////////////////////////////////
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar el género no deseado: " + e.getMessage(), e);
        }
    }*/


}
