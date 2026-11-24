package com.tracker.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz funcional para operaciones ejecutadas dentro de una transacción JDBC.
 *
 * @param <T> Tipo de resultado devuelto por la operación.
 */
@FunctionalInterface
public interface FuncionTransaccion<T> {

    /**
     * Ejecuta la operación dentro de la conexión transaccional.
     *
     * @param conn conexión con el autocommit desactivado.
     * @return el resultado de la operación.
     * @throws SQLException si falla alguna sentencia SQL.
     */
    T ejecutar(Connection conn) throws SQLException;
}
