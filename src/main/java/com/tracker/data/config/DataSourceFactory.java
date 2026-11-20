package com.tracker.data.config;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Factoría de {@link DataSource} para MySQL.
 * <p>
 * Crea los orígenes de datos una sola vez y los reutiliza. Hay dos orígenes:
 * uno contra el servidor, necesario para crear la base de datos, y otro contra
 * la base de datos de la aplicación.
 * </p>
 */
public final class DataSourceFactory {

    private static DataSource dataSource;
    private static DataSource dataSourceServidor;

    /**
     * Constructor privado para impedir la creación de instancias de la factoría.
     */
    private DataSourceFactory() {
    }

    /**
     * Devuelve el origen de datos de la base de datos de la aplicación.
     *
     * @return el {@link DataSource} de {@code Candidaturas}.
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = crearDataSource(ConfiguracionBd.getUrlBaseDatos());
        }
        return dataSource;
    }

    /**
     * Devuelve el origen de datos del servidor, sin base de datos seleccionada.
     *
     * @return el {@link DataSource} del servidor MySQL.
     */
    public static DataSource getDataSourceServidor() {
        if (dataSourceServidor == null) {
            dataSourceServidor = crearDataSource(ConfiguracionBd.getUrlServidor());
        }
        return dataSourceServidor;
    }

    /**
     * Configura un origen de datos MySQL con la URL y las credenciales del entorno.
     *
     * @param url URL JDBC de conexión.
     * @return el origen de datos configurado.
     */
    private static DataSource crearDataSource(String url) {
        ConfiguracionBd configuracion = ConfiguracionBd.desdeEntorno();

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(url);
        mysqlDataSource.setUser(configuracion.getUsuario());
        mysqlDataSource.setPassword(configuracion.getPassword());
        return mysqlDataSource;
    }
}
