package com.tracker.data;

import com.tracker.common.AppContext;
import com.tracker.data.config.DataSourceFactory;
import com.tracker.exception.AccesoDatosException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestor de transacciones JDBC.
 * <p>
 * Permite ejecutar varias operaciones SQL en una sola unidad de trabajo
 * garantizando commit o rollback automático en caso de error.
 * </p>
 */
public class TransaccionManager {

    private static final Logger LOGGER = Logger.getLogger(TransaccionManager.class.getName());
    private final DataSource dataSource;

    /**
     * Crea el gestor con el origen de datos de la base de datos de la aplicación.
     */
    public TransaccionManager() {
        this(DataSourceFactory.getDataSource());
        AppContext.getInstance().put("datasource",this.dataSource);
    }

    /**
     * Crea el gestor con un origen de datos concreto.
     *
     * @param dataSource origen de datos que aporta las conexiones transaccionales.
     */
    public TransaccionManager(DataSource dataSource) {
        this.dataSource = dataSource;
        AppContext.getInstance().put("datasource",this.dataSource);
    }

    /**
     * Ejecuta una función dentro de una transacción con commit y rollback automático.
     *
     * @param funcion Operación a realizar dentro de la conexión en transacción.
     * @param <T>     Tipo del valor devuelto.
     * @return El resultado de la función.
     */
    public <T> T ejecutarEnTransaccion(FuncionTransaccion<T> funcion) {
        try (Connection conn = dataSource.getConnection()) {
            boolean autoCommitInicial = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);
                T resultado = funcion.ejecutar(conn);
                conn.commit();
                return resultado;
            } catch (Exception e) {
                try {
                    conn.rollback();
                    LOGGER.info("Rollback ejecutado correctamente tras un fallo en la transacción.");
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al realizar el rollback de la transacción", ex);
                }
                if (e instanceof RuntimeException re) {
                    throw re;
                }
                throw new AccesoDatosException("Error al ejecutar la transacción", e);
            } finally {
                conn.setAutoCommit(autoCommitInicial);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en la conexión de la transacción", e);
            throw new AccesoDatosException("Error al obtener conexión para la transacción", e);
        }
    }
}
