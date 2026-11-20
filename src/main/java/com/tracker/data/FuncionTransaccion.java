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
    T ejecutar(Connection conn) throws SQLException;
}
