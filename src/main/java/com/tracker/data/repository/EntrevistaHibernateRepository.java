package com.tracker.data.repository;

import com.tracker.data.config.HibernateConfig;
import com.tracker.data.entity.Empresa;
import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import com.tracker.data.entity.Oferta;
import org.hibernate.Session;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Hibernate de entrevistas.
 * <p>
 * Mantiene el contrato JDBC hasta la Parte 4, incluidos los dos métodos que
 * reciben una {@link Connection}; esos métodos no se usan en esta fase y se
 * retirarán cuando los servicios pasen a trabajar con {@code Session}.
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
        try (Session s = HibernateConfig.getSessionFactory().openSession()) {
            List<Entrevista> entrevistas = s.createQuery(
                    "from Entrevista en where en.idOferta = :id", Entrevista.class)
                    .setParameter("id", idOferta)
                    .getResultList();
            return entrevistas.stream().map(en -> {
                Oferta oferta = s.find(Oferta.class, en.getIdOferta());
                Empresa empresa = oferta == null ? null : s.find(Empresa.class, oferta.getIdEmpresa());
                String titulo = oferta == null ? "" : oferta.getTitulo();
                String nombreEmpresa = empresa == null ? "" : empresa.getNombre();
                return new EntrevistaDetalle(
                        en.getId(),
                        en.getIdOferta(),
                        titulo,
                        nombreEmpresa,
                        en.getFecha(),
                        en.getTipo(),
                        en.getNotas(),
                        en.getResultado()
                );
            }).toList();
        }
    }

    /**
     * Método heredado de la fase JDBC que este repositorio no soporta.
     *
     * @param entrevista entrevista a guardar.
     * @param conn       conexión JDBC.
     * @return nunca, porque lanza una excepción.
     * @throws UnsupportedOperationException siempre, porque se retirará en la Parte 4.
     */
    @Override
    public Entrevista guardar(Entrevista entrevista, Connection conn) {
        throw new UnsupportedOperationException("Método JDBC retirado en la Parte 4.");
    }

    /**
     * Método heredado de la fase JDBC que este repositorio no soporta.
     *
     * @param idOferta identificador de la oferta.
     * @param conn     conexión JDBC.
     * @throws UnsupportedOperationException siempre, porque se retirará en la Parte 4.
     */
    @Override
    public void eliminarPorOferta(Integer idOferta, Connection conn) {
        throw new UnsupportedOperationException("Método JDBC retirado en la Parte 4.");
    }
}
