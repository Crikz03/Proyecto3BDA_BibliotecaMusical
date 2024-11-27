/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conversiones;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase genérica que proporciona métodos de conversión entre objetos de tipo
 * DTO y sus correspondientes entidades, y viceversa. Se pueden usar los métodos
 * para convertir tanto objetos individuales como listas de objetos.
 *
 * También incluye métodos especializados para convertir entre objetos `Evento`
 * y `EventoDTO`.
 *
 * @author Cristopher Alberto Elizalde Andrade - 240005
 * @author Paulina Rodríguez Rodríguez Rayos - 117262
 */
public class ConvertidorGeneral {

    /**
     * Constructor por defecto de la clase ConvertidorGeneral.
     */
    public ConvertidorGeneral() {
    }

    /**
     * Método genérico para convertir un objeto DTO en una entidad.
     *
     * @param <D> Tipo del objeto DTO.
     * @param <E> Tipo de la entidad.
     * @param dto Objeto DTO que se desea convertir.
     * @param entityClass Clase de la entidad destino.
     * @return Objeto de tipo entidad con los valores copiados del DTO.
     * @throws RuntimeException Si ocurre algún error durante la conversión.
     */
    public static <D, E> E convertidorEntidad(D dto, Class<E> entityClass) {
        try {
            E entidad = entityClass.getDeclaredConstructor().newInstance();

            // Obtener los campos del DTO y de la entidad
            Field[] camposDto = dto.getClass().getDeclaredFields();
            Field[] camposEntidad = entityClass.getDeclaredFields();

            // Iterar sobre los campos y copiar los valores que coincidan en nombre y tipo
            for (Field campoDto : camposDto) {
                campoDto.setAccessible(true);
                for (Field campoEntidad : camposEntidad) {
                    campoEntidad.setAccessible(true);
                    if (campoDto.getName().equals(campoEntidad.getName())
                            && campoDto.getType().equals(campoEntidad.getType())) {
                        campoEntidad.set(entidad, campoDto.get(dto));
                        break;
                    } else if (campoDto.getName().equals(campoEntidad.getName())
                            && !campoDto.getType().equals(campoEntidad.getType())) {

                        // Si el campo es de tipo DTO, se convierte recursivamente
                        if (campoDto.getType().getSimpleName().endsWith("DTO")) {
                            Object nestedDto = campoDto.get(dto);
                            if (nestedDto != null) {
                                Object nestedEntity = convertidorEntidad(nestedDto, campoEntidad.getType());
                                campoEntidad.set(entidad, nestedEntity);
                            }
                        }
                    }
                }
            }

            return entidad;
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir DTO a entidad", e);
        }
    }

    /**
     * Método genérico para convertir una entidad en un objeto DTO.
     *
     * @param <E> Tipo de la entidad.
     * @param <D> Tipo del objeto DTO.
     * @param entidad Objeto entidad que se desea convertir.
     * @param claseDto Clase del DTO destino.
     * @return Objeto de tipo DTO con los valores copiados de la entidad.
     * @throws RuntimeException Si ocurre algún error durante la conversión.
     */
    public static <E, D> D convertidoraDTO(E entidad, Class<D> claseDto) {
        try {
            D dto = claseDto.getDeclaredConstructor().newInstance();

            // Obtener los campos de la entidad y del DTO
            Field[] camposEntidad = entidad.getClass().getDeclaredFields();
            Field[] camposDto = claseDto.getDeclaredFields();

            // Iterar sobre los campos y copiar los valores que coincidan en nombre y tipo
            for (Field campoEntidad : camposEntidad) {
                campoEntidad.setAccessible(true);
                for (Field campoDto : camposDto) {
                    campoDto.setAccessible(true);
                    if (campoEntidad.getName().equals(campoDto.getName())
                            && campoEntidad.getType().equals(campoDto.getType())) {
                        campoDto.set(dto, campoEntidad.get(entidad));
                        break;
                    } else if (campoEntidad.getName().equals(campoDto.getName())
                            && !campoEntidad.getType().equals(campoDto.getType())) {

                        // Si el campo es de tipo entidad, se convierte recursivamente
                        if (campoEntidad.getType().getSimpleName().endsWith("DTO")) {
                            Object nestedEntity = campoEntidad.get(entidad);
                            if (nestedEntity != null) {
                                Object nestedDto = convertidoraDTO(nestedEntity, campoDto.getType());
                                campoDto.set(dto, nestedDto);
                            }
                        }
                    }
                }
            }

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir entidad a DTO", e);
        }
    }

    /**
     * Convierte una lista de objetos DTO en una lista de entidades.
     *
     * @param <D> Tipo del objeto DTO.
     * @param <E> Tipo de la entidad.
     * @param listaDto Lista de objetos DTO.
     * @param entityClass Clase de la entidad destino.
     * @return Lista de objetos de tipo entidad.
     */
    public static <D, E> List<E> convertidorListaEntidad(List<D> listaDto, Class<E> entityClass) {
        List<E> listaEntidades = new ArrayList<>();
        for (D dto : listaDto) {
            listaEntidades.add(convertidorEntidad(dto, entityClass));
        }
        return listaEntidades;
    }

    /**
     * Convierte una lista de entidades en una lista de objetos DTO.
     *
     * @param <E> Tipo de la entidad.
     * @param <D> Tipo del objeto DTO.
     * @param listaEntidad Lista de objetos de tipo entidad.
     * @param claseDto Clase del DTO destino.
     * @return Lista de objetos de tipo DTO.
     */
    public static <E, D> List<D> convertidoraListaDTO(List<E> listaEntidad, Class<D> claseDto) {
        List<D> listaDtos = new ArrayList<>();
        for (E entidad : listaEntidad) {
            listaDtos.add(convertidoraDTO(entidad, claseDto));
        }
        return listaDtos;
    }
}
