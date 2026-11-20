package com.tracker.data.repository;

import com.tracker.data.config.DataSourceFactory;
import com.tracker.data.entity.Empresa;
import com.tracker.exception.AccesoDatosException;
import com.tracker.exception.ValidacionException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpresaJdbcRepository implements EmpresaRepository {

    private static final Logger LOGGER = Logger.getLogger(EmpresaJdbcRepository.class.getName());
    private final DataSource dataSource = DataSourceFactory.getDataSource();

    @Override
    public Empresa guardar(Empresa empresa) {
        String sql = "INSERT INTO EMPRESA (nombre, sector, web, fechaRegistro) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, empresa.nombre());
            stmt.setString(2, empresa.sector());
            stmt.setString(3, empresa.web());
            stmt.setDate(4, Date.valueOf(empresa.fechaRegistro()));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Empresa(rs.getInt(1), empresa.nombre(), empresa.sector(), empresa.web(), empresa.fechaRegistro());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar empresa", e);
            throw new AccesoDatosException("Error al guardar empresa", e);
        }
        throw new AccesoDatosException("No se pudo obtener el identificador de la empresa guardada.");
    }

    @Override
    public Empresa actualizar(Empresa empresa) {
        if (empresa.id() == null) {
            throw new ValidacionException("No se puede actualizar una empresa sin ID.");
        }
        String sql = "UPDATE EMPRESA SET nombre = ?, sector = ?, web = ?, fechaRegistro = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, empresa.nombre());
            stmt.setString(2, empresa.sector());
            stmt.setString(3, empresa.web());
            stmt.setDate(4, Date.valueOf(empresa.fechaRegistro()));
            stmt.setInt(5, empresa.id());
            stmt.executeUpdate();
            return empresa;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar empresa", e);
            throw new AccesoDatosException("Error al actualizar empresa", e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM EMPRESA WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar empresa", e);
            throw new AccesoDatosException("Error al eliminar empresa", e);
        }
    }

    @Override
    public Optional<Empresa> buscarPorId(Integer id) {
        String sql = "SELECT * FROM EMPRESA WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar empresa por ID", e);
            throw new AccesoDatosException("Error al buscar empresa", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Empresa> buscarTodos() {
        String sql = "SELECT * FROM EMPRESA";
        List<Empresa> empresas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                empresas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar todas las empresas", e);
            throw new AccesoDatosException("Error al buscar empresas", e);
        }
        return empresas;
    }

    @Override
    public Optional<Empresa> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM EMPRESA WHERE nombre = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar empresa por nombre", e);
            throw new AccesoDatosException("Error al buscar empresa por nombre", e);
        }
        return Optional.empty();
    }

    private Empresa mapRow(ResultSet rs) throws SQLException {
        return new Empresa(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("sector"),
            rs.getString("web"),
            rs.getDate("fechaRegistro").toLocalDate()
        );
    }
}
