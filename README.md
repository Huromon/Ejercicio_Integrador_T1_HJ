# Tracker de Candidaturas Laborales — v2.0 (JDBC + MySQL)

Versión 2.0 del proyecto: la persistencia migró de archivos CSV a una base de datos
relacional MySQL mediante **JDBC**. Este repositorio contiene únicamente la capa de
acceso a datos y de negocio; la interfaz JavaFX es responsabilidad de otro integrante
del equipo.

## Tecnologías

- Java 24 (`maven.compiler.release`)
- MySQL Connector/J 26.10.0
- JDBC (`javax.sql.DataSource`, `PreparedStatement`, transacciones)
- `java.util.logging` para los registros de la capa de datos

## Base de datos

- Servidor: `localhost:3307`
- Esquema: `Candidaturas`
- La URL está fijada como constante en `ConfiguracionBd` (no se externaliza).

### Variables de entorno

Las credenciales **sí** se leen del entorno:

| Variable | Valor | Descripción |
|---|---|---|
| `DB_USER` | `usuario` | Usuario de MySQL |
| `DB_PASSWORD` | `pwd` | Contraseña de MySQL |

Desde PowerShell:

```powershell
$env:DB_USER = "usuario"
$env:DB_PASSWORD = "pwd"
```

Desde IntelliJ: *Run → Edit Configurations → Environment variables*:

```text
DB_USER=usuario;DB_PASSWORD=pwd
```

## Esquema y datos iniciales

- `src/main/resources/schema.sql`: crea `EMPRESA`, `OFERTA` y `ENTREVISTA` con sus
  claves foráneas. Sin `ON DELETE CASCADE`: el borrado en cascada se resuelve con
  transacciones en la capa de negocio.
- `src/main/resources/data.sql`: datos de ejemplo (9 empresas, 21 ofertas y
  5 entrevistas).
- `DataBaseInitializer.inicializar()` crea el esquema y carga los datos solo si la
  tabla `EMPRESA` está vacía. `TrackerServiceFactory.inicializar()` lo invoca una sola vez.

## Arquitectura

```text
com.tracker
├── business
│   ├── EmpresaService / EmpresaServiceImpl
│   ├── OfertaService / OfertaServiceImpl
│   ├── EntrevistaService / EntrevistaServiceImpl
│   ├── TrackerServiceFactory   ← punto de entrada para JavaFX
├── data
│   ├── config (ConfiguracionBd, DataSourceFactory)
│   ├── DataBaseInitializer
│   ├── TransaccionManager / FuncionTransaccion
│   ├── entity (records Empresa, Oferta, Entrevista, proyecciones Detalle, FiltroOferta, Estado)
│   └── repository (interfaces + implementaciones JDBC)
└── exception (TrackerException, AccesoDatosException, ValidacionException,
               NoEncontradoException, ConfiguracionException)
```

## Uso desde JavaFX

El compañero debe llamar una única vez al arrancar:

```java
TrackerServiceFactory.inicializar();

EmpresaService empresas = TrackerServiceFactory.getEmpresaService();
OfertaService ofertas = TrackerServiceFactory.getOfertaService();
EntrevistaService entrevistas = TrackerServiceFactory.getEntrevistaService();
```

## Funcionalidad incluida

- CRUD completo de empresas, ofertas y entrevistas.
- Filtros combinables (empresa, estado, salario, fechas y texto) mediante `FiltroOferta`.
- Consultas con `JOIN` entre `OFERTA`, `EMPRESA` y `ENTREVISTA`.
- Cambio de estado de una candidatura (`OfertaRepository.cambiarEstado`).
- Borrado en cascada y registro de entrevista dentro de transacciones JDBC
  (`TransaccionManager` con `commit` / `rollback`).
- Manejo de errores: `SQLException` se captura en la capa de datos, se registra con
  Logger y se encapsula en `AccesoDatosException`.

## Compilación

```bash
mvn clean compile
```

La importación del CSV de la v1.0 y los tests unitarios quedan fuera del alcance de
esta entrega v2.0.
