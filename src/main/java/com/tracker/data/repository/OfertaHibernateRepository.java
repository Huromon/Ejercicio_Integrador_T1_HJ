package com.tracker.data.repository;

import com.tracker.data.config.HibernateConfig;
import com.tracker.data.entity.Empresa;
import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.Estado;
import com.tracker.data.entity.FiltroOferta;
import com.tracker.data.entity.Oferta;
import com.tracker.data.entity.OfertaDetalle;
import org.hibernate.Session;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Hibernate de ofertas.
 * <p>
 * Mantiene el contrato JDBC hasta la Parte 4, incluidos los dos métodos que
 * reciben una {@link Connection}; esos métodos no se usan en esta fase y se
 * retirarán cuando los servicios pasen a trabajar con {@code Session}.
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
     * Busca ofertas que cumplen los criterios del filtro y añade el nombre de la empresa.
     *
     * @param filtro criterios de filtrado.
     * @return lista de ofertas con el nombre de la empresa.
     */
    @Override
    public List<OfertaDetalle> filtrar(FiltroOferta filtro) {
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
                consulta.setParameter("estado", filtro.getEstado().name());
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
            return consulta.getResultList().stream().map(oferta -> {
                Empresa empresa = s.find(Empresa.class, oferta.getIdEmpresa());
                String nombre = empresa == null ? "" : empresa.getNombre();
                return new OfertaDetalle(
                        oferta.getId(),
                        oferta.getTitulo(),
                        nombre,
                        oferta.getEstado(),
                        oferta.getFechaPostulacion(),
                        oferta.getEnlace(),
                        oferta.getDescripcion(),
                        oferta.getSalario(),
                        oferta.getNotas()
                );
            }).toList();
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

    /**
     * Método heredado de la fase JDBC que este repositorio no soporta.
     *
     * @param id     identificador de la oferta.
     * @param estado nuevo estado de la candidatura.
     * @param conn   conexión JDBC.
     * @throws UnsupportedOperationException siempre, porque se retirará en la Parte 4.
     */
    @Override
    public void cambiarEstado(Integer id, Estado estado, Connection conn) {
        throw new UnsupportedOperationException("Método JDBC retirado en la Parte 4.");
    }

    /**
     * Método heredado de la fase JDBC que este repositorio no soporta.
     *
     * @param id   identificador de la oferta.
     * @param conn conexión JDBC.
     * @throws UnsupportedOperationException siempre, porque se retirará en la Parte 4.
     */
    @Override
    public void eliminar(Integer id, Connection conn) {
        throw new UnsupportedOperationException("Método JDBC retirado en la Parte 4.");
    }
}
