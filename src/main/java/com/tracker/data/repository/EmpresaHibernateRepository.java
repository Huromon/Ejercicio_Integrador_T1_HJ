package com.tracker.data.repository;

import com.tracker.data.config.HibernateConfig;
import com.tracker.data.entity.Empresa;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Hibernate de empresas.
 * <p>
 * Implementa el mismo contrato que el repositorio JDBC mientras la aplicación
 * termina de migrarse, de modo que pueda sustituirse en la Parte 4 sin tocar
 * los servicios de negocio.
 * </p>
 */
public class EmpresaHibernateRepository implements EmpresaRepository {

    /**
     * Inserta una empresa en una transacción Hibernate.
     *
     * @param empresa empresa a guardar.
     * @return la empresa persistida con su identificador asignado.
     */
    @Override
    public Empresa guardar(Empresa empresa) {
        return HibernateConfig.getSessionFactory().fromTransaction(s -> {
            s.persist(empresa);
            return empresa;
        });
    }

    /**
     * Elimina una empresa por su identificador.
     *
     * @param id identificador de la empresa.
     */
    @Override
    public void eliminar(Integer id) {
        HibernateConfig.getSessionFactory().inTransaction(s -> {
            Empresa empresa = s.find(Empresa.class, id);
            if (empresa != null) {
                s.remove(empresa);
            }
        });
    }

    /**
     * Busca una empresa por su identificador.
     *
     * @param id identificador de la empresa.
     * @return un {@link Optional} con la empresa si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Empresa> buscarPorId(Integer id) {
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.find(Empresa.class, id));
        }
    }

    /**
     * Devuelve todas las empresas registradas.
     *
     * @return lista de empresas.
     */
    @Override
    public List<Empresa> buscarTodos() {
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            return s.createQuery("from Empresa", Empresa.class).getResultList();
        }
    }

    /**
     * Busca una empresa por su nombre exacto.
     *
     * @param nombre nombre de la empresa.
     * @return un {@link Optional} con la empresa si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Empresa> buscarPorNombre(String nombre) {
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.createQuery(
                    "from Empresa e where e.nombre = :nombre", Empresa.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult());
        }
    }

    /**
     * Actualiza los datos de una empresa existente.
     *
     * @param empresa empresa con los datos actualizados y su identificador.
     * @return la empresa actualizada.
     */
    @Override
    public Empresa actualizar(Empresa empresa) {
        return HibernateConfig.getSessionFactory().fromTransaction(s -> {
            return s.merge(empresa);
        });
    }
}
