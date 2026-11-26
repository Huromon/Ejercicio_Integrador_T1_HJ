# Gestor de Candidaturas v2.0

Aplicación de escritorio para registrar y seguir candidaturas laborales. Esta versión sustituye la consola de la v1.0 por una interfaz JavaFX y la persistencia en CSV por acceso **JDBC puro a MySQL**.

Prototipo de diseño: https://www.figma.com/design/HECEbOoQ0EhoD2xgEMVDYP/Gestor_camdidaturas_HyJ?node-id=0-1&t=cd3ISSGQP5MxbuP9-1

## Autores y reparto de trabajo

| Autor | Parte del ejercicio | Trabajo realizado |
|---|---|---|
| **Jesús Pérez Martín** | Acceso a Datos | Esquema y datos iniciales, configuración y `DataSource`, entidades y contratos de repositorio, implementaciones JDBC, servicios de negocio, fachada, transacciones, filtros y documentación Javadoc. |
| **Hugo Roldán Moncayo** | Desarrollo de Interfaces | Vistas FXML diseñadas con Scene Builder, controladores y eventos, navegación entre pantallas, integración con la capa de servicios y presentación de nombres de empresa y estados. |

## Cambios respecto a v1.0

| v1.0 | v2.0 |
|---|---|
| Interfaz CLI por argumentos (`args[]`) | Interfaz gráfica JavaFX con vistas FXML |
| Persistencia en `candidaturas.csv` | Persistencia en MySQL mediante JDBC |
| Filtros por empresa y estado | Filtros combinables por empresa, título, estado, salario y fechas |
| Solo ofertas | Ofertas, empresas y entrevistas con relaciones entre tablas |
| IDs calculados en código | IDs generados por la base de datos (`AUTO_INCREMENT`) |

La migración a Hibernate que se exploró durante el desarrollo fue descartada: el proyecto **no contiene dependencias, configuración ni código Hibernate/JPA**. El acceso a datos se realiza exclusivamente con `Connection`, `PreparedStatement` y `DataSource`.

## Funcionalidad

- **Listado** de ofertas en un `TableView` con empresa, título, estado, fecha y salario.
- **Alta de ofertas** con validación de campos obligatorios; la empresa se elige de un `ComboBox` editable y, si no existe, se da de alta automáticamente.
- **Detalle de oferta** con información completa, empresa asociada y lista de entrevistas formateadas como `fecha | tipo | resultado`.
- **Cambio de estado** de la candidatura desde el detalle: PENDIENTE, ENTREVISTA, ACEPTADA o RECHAZADA.
- **Eliminación** de ofertas con confirmación y borrado de sus entrevistas en la misma transacción.
- **Filtros combinables** por empresa, título, estado, salario mínimo/máximo y rango de fechas, con botón *Quitar filtro* para restaurar el listado completo.
- **CRUD de empresas, ofertas y entrevistas** en la capa de servicios y repositorios (`crear`, `actualizar`, `listar`, `buscarPorId`, `eliminar`).
- **Datos iniciales** cargados en la base de datos a partir del CSV de la v1.0.

## Tecnología

- **Java 27** y **Maven** (`pom.xml`, versión `2.0-SNAPSHOT`).
- **JavaFX 26.0.2**: `javafx-controls` y `javafx-fxml`; vistas maquetadas con Scene Builder.
- **MySQL Connector/J 26.10.0** y JDBC estándar (`java.sql`, `javax.sql.DataSource`).
- **Lombok 1.18.48** como procesador de anotaciones en tiempo de compilación.
- Arquitectura multicapa: `presentation`, `controllers`, `common`, `business`, `data` y `exception`.
- Patrón **Repository**, fachada de servicios `TrackerServiceFactory`, contexto compartido `AppContext`/`ContextKeys` y navegación centralizada en `SceneNavegator`.
- Transacciones explícitas en `TransaccionManager` (`setAutoCommit(false)`, `commit`/`rollback`) para operaciones que afectan a varias tablas.
- Documentación del código mediante **Javadoc** y logging con `java.util.logging`.

## Base de datos

MySQL en `localhost:3307`, esquema `Candidaturas`. Tablas y relaciones:

```text
EMPRESA (id, nombre, sector, web, fechaRegistro)
OFERTA  (id, id_empresa FK, titulo, descripcion, estado, fechaPostulacion, enlace, salario, notas)
ENTREVISTA (id, id_oferta FK, fecha, tipo, notas, resultado)
```

- `src/main/resources/schema.sql`: creación del esquema y de las claves foráneas.
- `src/main/resources/data.sql`: datos iniciales importados del CSV de la v1.0.
- `DataBaseInitializer` ejecuta ambos scripts al arrancar si la base de datos está vacía.
- Las credenciales se leen de las variables de entorno **`DB_USER`** y **`DB_PASSWORD`**; la URL de conexión está fijada en `ConfiguracionBd`.

## Ejecución

Requisitos: JDK 27, Maven y MySQL escuchando en el puerto 3307 con el esquema `Candidaturas` accesible.

También se puede ejecutar desde IntelliJ con la clase `com.tracker.presentation.Launcher` (que llama a `Application.launch(Aplicacion.class, args)`) o directamente `com.tracker.presentation.Aplicacion`, configurada como `mainClass` del plugin de JavaFX.

## Estructura del proyecto

```text
src/main/java/com/tracker
├── business
│   ├── EmpresaService / EmpresaServiceImpl
│   ├── EntrevistaService / EntrevistaServiceImpl
│   ├── OfertaService / OfertaServiceImpl
│   └── TrackerServiceFactory
├── common
│   ├── AppContext / ContextKeys
│   └── SceneNavegator
├── controllers
│   ├── AñadirController / DetallesController
│   └── FiltroController / ListadoController
├── data
│   ├── config (ConfiguracionBd / DataSourceFactory)
│   ├── entity (Empresa, Oferta, Entrevista, Estado y DTOs)
│   ├── repository (interfaces y Empresa/Oferta/EntrevistaJdbcRepository)
│   ├── DataBaseInitializer
│   ├── FuncionTransaccion / TransaccionManager
├── exception
│   ├── AccesoDatosException / ConfiguracionException
│   └── NoEncontradoException / TrackerException / ValidacionException
└── presentation
    ├── Aplicacion / Launcher
src/main/resources
├── schema.sql / data.sql
└── com/tracker/views (main-view, nuevo-view, Candidatura-view, filtro-view .fxml)
```

### Acceso a Datos (Jesús)

- Esquema MySQL con `EMPRESA`, `OFERTA` y `ENTREVISTA` y sus claves foráneas (`schema.sql`).
- Operaciones de la v1.0 migradas de archivos a JDBC.
- Filtros con salario y combinaciones múltiples de empresa, estado, salario y fechas.
- CRUD completo de empresas, ofertas y entrevistas en la capa de datos.
- Cambio de estado de una candidatura.
- Datos del CSV inicial importados en la base de datos (`data.sql`).
- Consultas con `JOIN` entre ofertas, empresas y entrevistas.
- Transacciones para las operaciones que afectan a varias tablas.
- Configuración de conexión externalizada mediante variables de entorno.

### Desarrollo de Interfaces (Hugo)

- Interfaz JavaFX + Scene Builder basada en el prototipo Figma de la v1.0.
- Listado de ofertas con `TableView`, campos de filtro y botones de acción.
- Detalle de oferta con información completa, empresa y lista de entrevistas.
- Formularios y controles de alta y edición comunicados con la capa de servicios.
- Vistas maquetadas en archivos `.fxml` y controladores que gestionan los eventos.
- Navegación fluida entre pantallas mediante `FXMLLoader` y `SceneNavegator`.
