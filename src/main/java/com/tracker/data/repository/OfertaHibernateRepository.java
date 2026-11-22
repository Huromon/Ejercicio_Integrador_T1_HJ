package com.tracker.data.repository;

import com.tracker.data.config.HibernateConfig;
import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.Estado;
import com.tracker.data.entity.FiltroOferta;
import com.tracker.data.entity.Oferta;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Hibernate de ofertas.
 * <p>
 * Realiza el CRUD y las consultas de ofertas con HQL sobre el
 * {@link org.hibernate.SessionFactory} de la aplicación.
 * </p>
 */
public class OfertaHibernateRepository implements OfertaRepository {

    /**
     * Inserta una oferta en una transacción Hibernate.
     *
     * @param oferta oferta a guardar.
     * @return la oferta persistida con su identificador asignado.
     */
    @Override
    public Oferta guardar(Oferta oferta) {
        return HibernateConfig.getSessionFactory().fromTransaction(s -> {
            s.persist(oferta);
            return oferta;
        });
    }

    /**
     * Elimina una oferta por su identificador.
     *
     * @param id identificador de la oferta.
     */
    @Override
    public void eliminar(Integer id) {
        HibernateConfig.getSessionFactory().inTransaction(s -> {
            Oferta oferta = s.find(Oferta.class, id);
            if (oferta != null) {
                s.remove(oferta);
            }
        });
    }

    /**
     * Busca una oferta por su identificador.
     *
     * @param id identificador de la oferta.
     * @return un {@link Optional} con la oferta si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Oferta> buscarPorId(Integer id) {
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.find(Oferta.class, id));
        }
    }

    /**
     * Devuelve todas las ofertas registradas.
     *
     * @return lista de ofertas.
     */
    @Override
    public List<Oferta> buscarTodos() {
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            return s.createQuery("from Oferta", Oferta.class).getResultList();
        }
    }

    /**
     * Busca ofertas que cumplen los criterios del filtro.
     *
     * @param filtro criterios de filtrado.
     * @return lista de ofertas que cumplen los criterios.
     */
    @Override
    public List<Oferta> filtrar(FiltroOferta filtro) {
        StringBuilder hql = new StringBuilder(
                "select o from Oferta o, Empresa e where o.idEmpresa = e.id and 1=1 ");
        if (filtro.getEmpresa() != null) {
            hql.append("and e.nombre like :empresa ");
        }
        if (filtro.getEstado() != null) {
            hql.append("and o.estado = :estado ");
        }
        if (filtro.getSalarioMin() != null) {
            hql.append("and o.salario >= :smin ");
        }
        if (filtro.getSalarioMax() != null) {
            hql.append("and o.salario <= :smax ");
        }
        if (filtro.getFechaDesde() != null) {
            hql.append("and o.fechaPostulacion >= :fdesde ");
        }
        if (filtro.getFechaHasta() != null) {
            hql.append("and o.fechaPostulacion <= :fhasta ");
        }
        if (filtro.getTexto() != null && !filtro.getTexto().isBlank()) {
            hql.append("and (o.titulo like :txt or o.descripcion like :txt) ");
        }
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            var consulta = s.createQuery(hql.toString(), Oferta.class);
            if (filtro.getEmpresa() != null) {
                consulta.setParameter("empresa", "%" + filtro.getEmpresa() + "%");
            }
            if (filtro.getEstado() != null) {
                consulta.setParameter("estado", filtro.getEstado());
            }
            if (filtro.getSalarioMin() != null) {
                consulta.setParameter("smin", filtro.getSalarioMin());
            }
            if (filtro.getSalarioMax() != null) {
                consulta.setParameter("smax", filtro.getSalarioMax());
            }
            if (filtro.getFechaDesde() != null) {
                consulta.setParameter("fdesde", filtro.getFechaDesde());
            }
            if (filtro.getFechaHasta() != null) {
                consulta.setParameter("fhasta", filtro.getFechaHasta());
            }
            if (filtro.getTexto() != null && !filtro.getTexto().isBlank()) {
                consulta.setParameter("txt", "%" + filtro.getTexto() + "%");
            }
            return consulta.getResultList();
        }
    }

    /**
     * Elimina una oferta y todas sus entrevistas en una sola transacción.
     *
     * @param id identificador de la oferta.
     */
    @Override
    public void eliminarConDependencias(Integer id) {
        HibernateConfig.getSessionFactory().inTransaction(s -> {
            Oferta oferta = s.find(Oferta.class, id);
            if (oferta == null) {
                return;
            }
            List<Entrevista> entrevistas = s.createQuery(
                    "from Entrevista en where en.idOferta = :id", Entrevista.class)
                    .setParameter("id", id)
                    .getResultList();
            entrevistas.forEach(s::remove);
            s.remove(oferta);
        });
    }

    /**
     * Cambia el estado de una oferta en una transacción Hibernate.
     *
     * @param id     identificador de la oferta.
     * @param estado nuevo estado de la candidatura.
     */
    @Override
    public void cambiarEstado(Integer id, Estado estado) {
        HibernateConfig.getSessionFactory().inTransaction(s -> {
            Oferta oferta = s.find(Oferta.class, id);
            if (oferta != null) {
                oferta.setEstado(estado);
            }
        });
    }
}
