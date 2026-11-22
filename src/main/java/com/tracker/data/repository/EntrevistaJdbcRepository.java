package com.tracker.data.repository;

import com.tracker.data.config.DataSourceFactory;
import com.tracker.data.entity.Entrevista;
import com.tracker.data.entity.EntrevistaDetalle;
import com.tracker.exception.AccesoDatosException;
import com.tracker.exception.ValidacionException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación JDBC del repositorio de entrevistas.
 */
public class EntrevistaJdbcRepository implements EntrevistaRepository {

    private static final Logger LOGGER = Logger.getLogger(EntrevistaJdbcRepository.class.getName());
    private final DataSource dataSource = DataSourceFactory.getDataSource();

    /**
     * Inserta una entrevista con su propia conexión.
     *
     * @param entrevista entrevista a guardar.
     * @return la entrevista guardada con su identificador asignado.
     */
    @Override
    public Entrevista guardar(Entrevista entrevista) {
        try (Connection conn = dataSource.getConnection()) {
            return guardar(entrevista, conn);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener conexión para guardar entrevista", e);
            throw new AccesoDatosException("Error al guardar entrevista", e);
        }
    }

    /**
     * Inserta una entrevista usando una conexión existente y recupera su clave generada.
     *
     * @param entrevista entrevista a guardar.
     * @param conn       conexión abierta por el gestor de transacciones.
     * @return la entrevista guardada con su identificador asignado.
     */
    @Override
    public Entrevista guardar(Entrevista entrevista, Connection conn) {
        String sql = "INSERT INTO ENTREVISTA (id_oferta, fecha, tipo, notas, resultado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, entrevista.getIdOferta());
            stmt.setDate(2, Date.valueOf(entrevista.getFecha()));
            stmt.setString(3, entrevista.getTipo());
            stmt.setString(4, entrevista.getNotas());
            stmt.setString(5, entrevista.getResultado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Entrevista(
                            rs.getInt(1),
                            entrevista.getIdOferta(),
                            entrevista.getFecha(),
                            entrevista.getTipo(),
                            entrevista.getNotas(),
                            entrevista.getResultado()
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar entrevista", e);
            throw new AccesoDatosException("Error al guardar entrevista", e);
        }
        throw new AccesoDatosException("No se pudo obtener el identificador de la entrevista guardada.");
    }

    /**
     * Modifica la fecha, el tipo, las notas y el resultado de una entrevista.
     *
     * @param entrevista entrevista con los datos actualizados y su identificador.
     * @return la entrevista actualizada.
     */
    @Override
    public Entrevista actualizar(Entrevista entrevista) {
        if (entrevista.getId() == null) {
            throw new ValidacionException("No se puede actualizar una entrevista sin ID.");
        }
        String sql = "UPDATE ENTREVISTA SET fecha = ?, tipo = ?, notas = ?, resultado = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(entrevista.getFecha()));
            stmt.setString(2, entrevista.getTipo());
            stmt.setString(3, entrevista.getNotas());
            stmt.setString(4, entrevista.getResultado());
            stmt.setInt(5, entrevista.getId());
            stmt.executeUpdate();
            return entrevista;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar entrevista", e);
            throw new AccesoDatosException("Error al actualizar entrevista", e);
        }
    }

    /**
     * Borra todas las entrevistas de una oferta usando una conexión existente.
     *
     * @param idOferta identificador de la oferta.
     * @param conn     conexión abierta por el gestor de transacciones.
     */
    @Override
    public void eliminarPorOferta(Integer idOferta, Connection conn) {
        String sql = "DELETE FROM ENTREVISTA WHERE id_oferta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOferta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar entrevistas de la oferta", e);
            throw new AccesoDatosException("Error al eliminar entrevistas de la oferta", e);
        }
    }

    /**
     * Elimina una entrevista por su identificador.
     *
     * @param id identificador de la entrevista a eliminar.
     */
    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM ENTREVISTA WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar entrevista", e);
            throw new AccesoDatosException("Error al eliminar entrevista", e);
        }
    }

    /**
     * Busca una entrevista por su identificador.
     *
     * @param id identificador de la entrevista.
     * @return un {@link Optional} con la entrevista si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Entrevista> buscarPorId(Integer id) {
        String sql = "SELECT * FROM ENTREVISTA WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar entrevista por ID", e);
            throw new AccesoDatosException("Error al buscar entrevista", e);
        }
        return Optional.empty();
    }

    /**
     * Devuelve todas las entrevistas registradas.
     *
     * @return lista de entrevistas.
     */
    @Override
    public List<Entrevista> buscarTodos() {
        String sql = "SELECT * FROM ENTREVISTA";
        List<Entrevista> entrevistas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                entrevistas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar todas las entrevistas", e);
            throw new AccesoDatosException("Error al buscar entrevistas", e);
        }
        return entrevistas;
    }

    /**
     * Busca las entrevistas de una oferta con JOIN a OFERTA y EMPRESA.
     *
     * @param idOferta identificador de la oferta.
     * @return lista de detalles de entrevistas.
     */
    @Override
    public List<EntrevistaDetalle> buscarPorOferta(Integer idOferta) {
        String sql = "SELECT en.id, en.id_oferta, o.titulo as tituloOferta, e.nombre as empresa, " +
                "en.fecha, en.tipo, en.notas, en.resultado " +
                "FROM ENTREVISTA en " +
                "JOIN OFERTA o ON en.id_oferta = o.id " +
                "JOIN EMPRESA e ON o.id_empresa = e.id " +
                "WHERE en.id_oferta = ?";
        List<EntrevistaDetalle> detalles = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOferta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new EntrevistaDetalle(
                            rs.getInt("id"),
                            rs.getInt("id_oferta"),
                            rs.getString("tituloOferta"),
                            rs.getString("empresa"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getString("tipo"),
                            rs.getString("notas"),
                            rs.getString("resultado")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar entrevistas por oferta", e);
            throw new AccesoDatosException("Error al buscar entrevistas por oferta", e);
        }
        return detalles;
    }

    /**
     * Convierte la fila actual del resultado en una entidad de entrevista.
     *
     * @param rs resultado situado en la fila a convertir.
     * @return la entrevista creada a partir de la fila.
     * @throws SQLException si no se puede leer alguna columna.
     */
    private Entrevista mapRow(ResultSet rs) throws SQLException {
        return new Entrevista(
                rs.getInt("id"),
                rs.getInt("id_oferta"),
                rs.getDate("fecha").toLocalDate(),
                rs.getString("tipo"),
                rs.getString("notas"),
                rs.getString("resultado")
        );
    }
}
