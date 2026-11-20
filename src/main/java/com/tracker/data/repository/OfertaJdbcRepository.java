package com.tracker.data.repository;

import com.tracker.data.config.DataSourceFactory;
import com.tracker.data.entity.*;
import com.tracker.exception.AccesoDatosException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OfertaJdbcRepository implements OfertaRepository {

    private static final Logger LOGGER = Logger.getLogger(OfertaJdbcRepository.class.getName());
    private final DataSource dataSource = DataSourceFactory.getDataSource();

    @Override
    public Oferta guardar(Oferta oferta) {
        String sql = "INSERT INTO OFERTA (id_empresa, titulo, descripcion, estado, fechaPostulacion, enlace, salario, notas) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, oferta.idEmpresa());
            stmt.setString(2, oferta.titulo());
            stmt.setString(3, oferta.descripcion());
            stmt.setString(4, oferta.estado().name());
            stmt.setDate(5, Date.valueOf(oferta.fechaPostulacion()));
            stmt.setString(6, oferta.enlace());
            stmt.setBigDecimal(7, oferta.salario());
            stmt.setString(8, oferta.notas());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Oferta(rs.getInt(1), oferta.idEmpresa(), oferta.titulo(), oferta.descripcion(), 
                                      oferta.estado(), oferta.fechaPostulacion(), oferta.enlace(), oferta.salario(), oferta.notas());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar oferta", e);
            throw new AccesoDatosException("Error al guardar oferta", e);
        }
        return null;
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM OFERTA WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar oferta", e);
            throw new AccesoDatosException("Error al eliminar oferta", e);
        }
    }

    @Override
    public void eliminarConDependencias(Integer id) {
        // La gestión transaccional se hará en el servicio, aquí solo borramos
        eliminar(id);
    }

    @Override
    public void cambiarEstado(Integer id, Estado estado) {
        String sql = "UPDATE OFERTA SET estado = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cambiar el estado de la oferta", e);
            throw new AccesoDatosException("Error al cambiar el estado de la oferta", e);
        }
    }

    @Override
    public Optional<Oferta> buscarPorId(Integer id) {
        String sql = "SELECT * FROM OFERTA WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar oferta por ID", e);
            throw new AccesoDatosException("Error al buscar oferta", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Oferta> buscarTodos() {
        String sql = "SELECT * FROM OFERTA";
        List<Oferta> ofertas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ofertas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar todas las ofertas", e);
            throw new AccesoDatosException("Error al buscar ofertas", e);
        }
        return ofertas;
    }

    @Override
    public List<OfertaDetalle> filtrar(FiltroOferta filtro) {
        StringBuilder sql = new StringBuilder(
            "SELECT o.id, o.titulo, e.nombre as empresa, o.estado, o.fechaPostulacion, o.enlace, o.descripcion, o.salario, o.notas " +
            "FROM OFERTA o JOIN EMPRESA e ON o.id_empresa = e.id WHERE 1=1");
        
        List<Object> params = new ArrayList<>();
        if (filtro.empresa() != null) { sql.append(" AND e.nombre LIKE ?"); params.add("%" + filtro.empresa() + "%"); }
        if (filtro.estado() != null) { sql.append(" AND o.estado = ?"); params.add(filtro.estado().name()); }
        if (filtro.salarioMin() != null) { sql.append(" AND o.salario >= ?"); params.add(filtro.salarioMin()); }
        if (filtro.salarioMax() != null) { sql.append(" AND o.salario <= ?"); params.add(filtro.salarioMax()); }
        if (filtro.fechaDesde() != null) { sql.append(" AND o.fechaPostulacion >= ?"); params.add(Date.valueOf(filtro.fechaDesde())); }
        if (filtro.fechaHasta() != null) { sql.append(" AND o.fechaPostulacion <= ?"); params.add(Date.valueOf(filtro.fechaHasta())); }
        if (filtro.texto() != null && !filtro.texto().isBlank()) {
            sql.append(" AND (o.titulo LIKE ? OR o.descripcion LIKE ?)");
            params.add("%" + filtro.texto() + "%");
            params.add("%" + filtro.texto() + "%");
        }
        
        List<OfertaDetalle> resultados = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) stmt.setObject(i + 1, params.get(i));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(new OfertaDetalle(rs.getInt("id"), rs.getString("titulo"), rs.getString("empresa"), 
                        Estado.desdeTexto(rs.getString("estado")), rs.getDate("fechaPostulacion").toLocalDate(), 
                        rs.getString("enlace"), rs.getString("descripcion"), rs.getBigDecimal("salario"), rs.getString("notas")));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar ofertas", e);
            throw new AccesoDatosException("Error al filtrar ofertas", e);
        }
        return resultados;
    }

    private Oferta mapRow(ResultSet rs) throws SQLException {
        return new Oferta(rs.getInt("id"), rs.getInt("id_empresa"), rs.getString("titulo"), rs.getString("descripcion"),
            Estado.desdeTexto(rs.getString("estado")), rs.getDate("fechaPostulacion").toLocalDate(), 
            rs.getString("enlace"), rs.getBigDecimal("salario"), rs.getString("notas"));
    }
}
