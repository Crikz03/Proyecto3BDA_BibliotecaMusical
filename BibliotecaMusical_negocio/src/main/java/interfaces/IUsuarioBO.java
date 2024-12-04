/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dto.UsuarioDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author pauli
 */
public interface IUsuarioBO {

    boolean actualizarUsuario(UsuarioDTO usuarioDTO) throws NegocioException;
    /* public UsuarioDTO obtenerUsuarioPorId(ObjectId idUsuario) throws NegocioException {
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

    boolean registrarUsuario(UsuarioDTO usuarioDTO) throws NegocioException;
    
    UsuarioDTO consultarUsuarioCorreo(String correo) throws NegocioException;

    void actualizarGenerosNoDeseados(ObjectId usuarioId, List<String> generosNoDeseados) throws NegocioException;
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

    UsuarioDTO consultarUsuarioPorNombre(String nombreUsuario) throws NegocioException;

    UsuarioDTO consultarPorId(ObjectId idUsuario) throws PersistenciaException;
    
}
