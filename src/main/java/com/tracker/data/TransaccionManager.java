package com.tracker.data;

import com.tracker.data.config.HibernateConfig;
import com.tracker.exception.AccesoDatosException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;

/**
 * Gestor de transacciones Hibernate de la aplicación.
 * <p>
 * Ejecuta una unidad de trabajo dentro de una {@link Session} propia, con
 * commit automático al terminar y rollback cuando se produce un error.
 * </p>
 */
public class TransaccionManager {

    /**
     * Ejecuta una función dentro de una transacción Hibernate.
     *
     * @param trabajo operación a realizar dentro de la sesión transaccional.
     * @param <T>     tipo del valor devuelto por la operación.
     * @return el resultado de la operación.
     * @throws AccesoDatosException si falla la transacción o la sesión.
     */
    public <T> T ejecutarEnTransaccion(Function<Session, T> trabajo) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T resultado = trabajo.apply(session);
                tx.commit();
                return resultado;
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        } catch (RuntimeException e) {
            throw new AccesoDatosException("Error en la transacción Hibernate", e);
        }
    }
}
