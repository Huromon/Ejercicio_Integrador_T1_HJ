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

public class EntrevistaJdbcRepository implements EntrevistaRepository {

    private static final Logger LOGGER = Logger.getLogger(EntrevistaJdbcRepository.class.getName());
    private final DataSource dataSource = DataSourceFactory.getDataSource();

    @Override
    public Entrevista guardar(Entrevista entrevista) {
        try (Connection conn = dataSource.getConnection()) {
            return guardar(entrevista, conn);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener conexión para guardar entrevista", e);
            throw new AccesoDatosException("Error al guardar entrevista", e);
        }
    }

    @Override
    public Entrevista guardar(Entrevista entrevista, Connection conn) {
        String sql = "INSERT INTO ENTREVISTA (id_oferta, fecha, tipo, notas, resultado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, entrevista.idOferta());
            stmt.setDate(2, Date.valueOf(entrevista.fecha()));
            stmt.setString(3, entrevista.tipo());
            stmt.setString(4, entrevista.notas());
            stmt.setString(5, entrevista.resultado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Entrevista(
                            rs.getInt(1),
                            entrevista.idOferta(),
                            entrevista.fecha(),
                            entrevista.tipo(),
                            entrevista.notas(),
                            entrevista.resultado()
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar entrevista", e);
            throw new AccesoDatosException("Error al guardar entrevista", e);
        }
        throw new AccesoDatosException("No se pudo obtener el identificador de la entrevista guardada.");
    }

    @Override
    public Entrevista actualizar(Entrevista entrevista) {
        if (entrevista.id() == null) {
            throw new ValidacionException("No se puede actualizar una entrevista sin ID.");
        }
        String sql = "UPDATE ENTREVISTA SET fecha = ?, tipo = ?, notas = ?, resultado = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(entrevista.fecha()));
            stmt.setString(2, entrevista.tipo());
            stmt.setString(3, entrevista.notas());
            stmt.setString(4, entrevista.resultado());
            stmt.setInt(5, entrevista.id());
            stmt.executeUpdate();
            return entrevista;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar entrevista", e);
            throw new AccesoDatosException("Error al actualizar entrevista", e);
        }
    }

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
