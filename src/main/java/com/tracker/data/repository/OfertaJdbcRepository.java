package com.tracker.data.repository;

import com.tracker.business.EmpresaServiceImpl;
import com.tracker.common.AppContext;
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

/**
 * Implementación JDBC del repositorio de ofertas.
 */
public class OfertaJdbcRepository implements OfertaRepository {

    private static final Logger LOGGER = Logger.getLogger(OfertaJdbcRepository.class.getName());
    private final DataSource dataSource = DataSourceFactory.getDataSource();

    /**
     * Inserta una oferta y recupera el identificador generado por la base de datos.
     *
     * @param oferta oferta a guardar.
     * @return la oferta guardada con su identificador asignado.
     */
    @Override
    public Oferta guardar(Oferta oferta) {

        String sql = "INSERT INTO OFERTA (id_empresa, titulo, descripcion, estado, fechaPostulacion, enlace, salario, notas) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            stmt.setObject(1, oferta.getIdEmpresa(), java.sql.Types.INTEGER);
            stmt.setString(2, oferta.getTitulo());
            stmt.setString(3, oferta.getDescripcion());
            stmt.setString(4, oferta.getEstado() != null ? oferta.getEstado().name() : null);
            stmt.setDate(5, oferta.getFechaPostulacion() != null ? Date.valueOf(oferta.getFechaPostulacion()) : null);
            stmt.setString(6, oferta.getEnlace());
            stmt.setBigDecimal(7, oferta.getSalario());

            stmt.setString(8, oferta.getNotas());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Oferta(rs.getInt(1), oferta.getIdEmpresa(), oferta.getTitulo(), oferta.getDescripcion(),
                            oferta.getEstado(), oferta.getFechaPostulacion(), oferta.getEnlace(), oferta.getSalario(), oferta.getNotas());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar oferta", e);
            throw new AccesoDatosException("Error al guardar oferta", e);
        }
        throw new AccesoDatosException("No se pudo obtener el identificador de la oferta guardada.");
    }

    /**
     * Elimina una oferta con su propia conexión.
     *
     * @param id identificador de la oferta a eliminar.
     */
    @Override
    public void eliminar(Integer id) {
        try (Connection conn = dataSource.getConnection()) {
            eliminar(id, conn);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener conexión para eliminar oferta", e);
            throw new AccesoDatosException("Error al eliminar oferta", e);
        }
    }

    /**
     * Elimina una oferta usando una conexión existente.
     *
     * @param id   identificador de la oferta a eliminar.
     * @param conn conexión abierta por el gestor de transacciones.
     */
    @Override
    public void eliminar(Integer id, Connection conn) {
        String sql = "DELETE FROM OFERTA WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar oferta", e);
            throw new AccesoDatosException("Error al eliminar oferta", e);
        }
    }

    /**
     * Elimina la oferta; la gestión transaccional de las dependencias se realiza en el servicio.
     *
     * @param id identificador de la oferta a eliminar.
     */
    @Override
    public void eliminarConDependencias(Integer id) {
        eliminar(id);
    }

    /**
     * Cambia el estado de una oferta con su propia conexión.
     *
     * @param id     identificador de la oferta.
     * @param estado nuevo estado de la candidatura.
     */
    @Override
    public void cambiarEstado(Integer id, Estado estado) {
        try (Connection conn = dataSource.getConnection()) {
            cambiarEstado(id, estado, conn);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener conexión para cambiar estado", e);
            throw new AccesoDatosException("Error al cambiar el estado de la oferta", e);
        }
    }

    /**
     * Cambia el estado de una oferta usando una conexión existente.
     *
     * @param id     identificador de la oferta.
     * @param estado nuevo estado de la candidatura.
     * @param conn   conexión abierta por el gestor de transacciones.
     */
    @Override
    public void cambiarEstado(Integer id, Estado estado, Connection conn) {
        String sql = "UPDATE OFERTA SET estado = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cambiar el estado de la oferta", e);
            throw new AccesoDatosException("Error al cambiar el estado de la oferta", e);
        }
    }

    /**
     * Busca una oferta por su identificador.
     *
     * @param id identificador de la oferta.
     * @return un {@link Optional} con la oferta si existe, o vacío en caso contrario.
     */
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

    /**
     * Devuelve todas las ofertas registradas.
     *
     * @return lista de ofertas.
     */
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

    /**
     * Busca ofertas con JOIN a EMPRESA aplicando todos los criterios no nulos del filtro.
     *
     * @param filtro criterios de filtrado.
     * @return lista de ofertas con el nombre de la empresa.
     */
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

    /**
     * Convierte la fila actual del resultado en una entidad de oferta.
     *
     * @param rs resultado situado en la fila a convertir.
     * @return la oferta creada a partir de la fila.
     * @throws SQLException si no se puede leer alguna columna.
     */
    private Oferta mapRow(ResultSet rs) throws SQLException {
        return new Oferta(rs.getInt("id"), rs.getInt("id_empresa"), rs.getString("titulo"), rs.getString("descripcion"),
            Estado.desdeTexto(rs.getString("estado")), rs.getDate("fechaPostulacion").toLocalDate(), 
            rs.getString("enlace"), rs.getBigDecimal("salario"), rs.getString("notas"));
    }
}
