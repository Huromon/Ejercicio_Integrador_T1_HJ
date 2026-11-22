package com.tracker.data.repository;

import com.tracker.data.config.HibernateConfig;
import com.tracker.data.entity.Empresa;
import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import com.tracker.data.entity.Oferta;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Hibernate de entrevistas.
 * <p>
 * Realiza el CRUD y la consulta de detalle con HQL sobre el
 * {@link org.hibernate.SessionFactory} de la aplicación.
 * </p>
 */
public class EntrevistaHibernateRepository implements EntrevistaRepository {

    /**
     * Inserta una entrevista en una transacción Hibernate.
     *
     * @param entrevista entrevista a guardar.
     * @return la entrevista persistida con su identificador asignado.
     */
    @Override
    public Entrevista guardar(Entrevista entrevista) {
        return HibernateConfig.getSessionFactory().fromTransaction(s -> {
            s.persist(entrevista);
            return entrevista;
        });
    }

    /**
     * Elimina una entrevista por su identificador.
     *
     * @param id identificador de la entrevista.
     */
    @Override
    public void eliminar(Integer id) {
        HibernateConfig.getSessionFactory().inTransaction(s -> {
            Entrevista entrevista = s.find(Entrevista.class, id);
            if (entrevista != null) {
                s.remove(entrevista);
            }
        });
    }

    /**
     * Busca una entrevista por su identificador.
     *
     * @param id identificador de la entrevista.
     * @return un {@link Optional} con la entrevista si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Entrevista> buscarPorId(Integer id) {
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.find(Entrevista.class, id));
        }
    }

    /**
     * Devuelve todas las entrevistas registradas.
     *
     * @return lista de entrevistas.
     */
    @Override
    public List<Entrevista> buscarTodos() {
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            return s.createQuery("from Entrevista", Entrevista.class).getResultList();
        }
    }

    /**
     * Actualiza los datos de una entrevista existente.
     *
     * @param entrevista entrevista con los datos actualizados y su identificador.
     * @return la entrevista actualizada.
     */
    @Override
    public Entrevista actualizar(Entrevista entrevista) {
        return HibernateConfig.getSessionFactory().fromTransaction(s -> {
            return s.merge(entrevista);
        });
    }

    /**
     * Busca las entrevistas de una oferta con los datos de la oferta y la empresa.
     *
     * @param idOferta identificador de la oferta.
     * @return lista de detalles de entrevistas.
     */
    @Override
    public List<EntrevistaDetalle> buscarPorOferta(Integer idOferta) {
        String hql = "select en, o, e from Entrevista en, Oferta o, Empresa e " +
                "where en.idOferta = o.id and o.idEmpresa = e.id and en.idOferta = :id";
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            List<Object[]> filas = s.createQuery(hql, Object[].class)
                    .setParameter("id", idOferta)
                    .getResultList();
            return filas.stream().map(fila -> {
                Entrevista en = (Entrevista) fila[0];
                Oferta oferta = (Oferta) fila[1];
                Empresa empresa = (Empresa) fila[2];
                return new EntrevistaDetalle(
                        en.getId(),
                        en.getIdOferta(),
                        oferta.getTitulo(),
                        empresa.getNombre(),
                        en.getFecha(),
                        en.getTipo(),
                        en.getNotas(),
                        en.getResultado()
                );
            }).toList();
        }
    }
}
