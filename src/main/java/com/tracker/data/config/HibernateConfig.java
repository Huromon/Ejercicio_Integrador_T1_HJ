package com.tracker.data.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Configuración centralizada de Hibernate para la aplicación.
 * <p>
 * Construye el {@link SessionFactory} a partir de {@code hibernate.cfg.xml} e inyecta
 * las credenciales leídas de {@code DB_USER} y {@code DB_PASSWORD}, de modo que no
 * aparecen datos sensibles en el archivo XML.
 * </p>
 */
public final class HibernateConfig {

    private static SessionFactory sessionFactory;

    /**
     * Constructor privado para impedir la creación de instancias.
     */
    private HibernateConfig() {
    }

    /**
     * Inicializa el {@link SessionFactory} si todavía no existe.
     *
     * @throws com.tracker.exception.ConfiguracionException si faltan las variables de entorno.
     */
    public static void inicializar() {
        if (sessionFactory != null) {
            return;
        }
        ConfiguracionBd cfg = ConfiguracionBd.desdeEntorno();

        Configuration configuration = new Configuration().configure();
        configuration.setProperty("hibernate.connection.username", cfg.getUsuario());
        configuration.setProperty("hibernate.connection.password", cfg.getPassword());
        sessionFactory = configuration.buildSessionFactory();
    }

    /**
     * Devuelve el {@link SessionFactory} ya inicializado.
     *
     * @return el {@link SessionFactory} de la aplicación.
     * @throws IllegalStateException si no se ha llamado todavía a {@link #inicializar()}.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("Debe llamar a HibernateConfig.inicializar() primero.");
        }
        return sessionFactory;
    }

    /**
     * Cierra el {@link SessionFactory} y permite volver a inicializarlo.
     */
    public static void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
