# Gestor de Candidaturas

Ejercicio práctico de la asignatura Acceso a Datos.

## Gestor de Candidaturas v1.0

Aplicación de consola desarrollada por Jesús Pérez Martín y Hugo Roldán Moncayo en Java para gestionar candidaturas laborales y realizar un seguimiento de procesos de selección, permitiendo registrar ofertas, consultar su estado y almacenar la información de forma persistente mediante archivos CSV.

## Funcionalidad

1. Añadir candidaturas.
2. Ver una candidatura mediante su ID.
3. Listar todas las candidaturas registradas.
4. Eliminar candidaturas.
5. Filtrar candidaturas por empresa.
6. Filtrar candidaturas por estado.
7. Importar candidaturas desde un archivo CSV.

## Figma

https://www.figma.com/design/HECEbOoQ0EhoD2xgEMVDYP/Gestor_camdidaturas_HyJ?node-id=0-1&t=cd3ISSGQP5MxbuP9-1

## Comandos disponibles

- `listar`
- `listar --empresa Empresa`
- `listar --estado PENDIENTE`
- `listar --empresa Empresa --estado ENTREVISTA`
- `añadir --titulo X --empresa Y --estado Z --fecha dd/MM/yyyy`
- `ver ID`
- `borrar ID`
- `importar candidaturas.csv`

## Estados disponibles

- PENDIENTE
- ENTREVISTA
- RECHAZADA
- ACEPTADA

# Implementación

- Se usa **Java 24** como lenguaje de programación.
- Proyecto gestionado mediante **Maven**.
- Interfaz de usuario mediante **CLI por argumentos** (`args[]`).
- Persistencia de datos mediante archivos **CSV**.
- Almacenamiento principal en el fichero `candidaturas.csv`.
- Lectura de archivos utilizando `BufferedReader` y `FileReader`.
- Escritura de archivos utilizando `BufferedWriter` y `FileWriter`.
- Gestión de fechas mediante `LocalDate` y `DateTimeFormatter`.
- Uso de **Record** para la entidad `Oferta`.
- Uso de **Enum** para representar los estados de una candidatura.
- Uso de colecciones `ArrayList` para la gestión de datos en memoria.
- Arquitectura multicapa dividida en:
    - Presentation
    - Business
    - Data
- Uso de interfaces para desacoplar la lógica de negocio de la persistencia.
- Implementación del patrón **Repository** para el acceso a datos.
- Generación automática de identificadores para nuevas candidaturas.
- Importación de candidaturas desde archivos CSV externos.
- Validación de argumentos obligatorios y gestión básica de errores.
- Documentación del código mediante **JavaDoc**.

## Estructura del proyecto

```text
com.tracker
├── business
│   ├── OfertaService
│   └── OfertaServicesint
│
├── data
│   ├── entity
│   │   ├── Estado
│   │   └── Oferta
│   │
│   └── repository
│       ├── OfertaRepository
│       └── OfertaRepositoryFileImpl
│
└── presentation
    ├── Main
    └── RecibirArgumentos
```

## Formato de almacenamiento

Las candidaturas se almacenan utilizando el siguiente formato CSV:

```csv
id;titulo;empresa;estado;fechaPostulacion;enlace;descripcion;salario
1;Desarrollador Java;EmpresaX;PENDIENTE;15/10/2026;https://empresa.com/oferta;Oferta junior;24000
```

## Mejoras implementadas

- Filtrado de candidaturas por empresa y estado.
- Importación masiva de candidaturas desde archivos CSV externos.
- Generación automática de identificadores únicos.
- Validación de parámetros obligatorios antes de registrar una candidatura.
- Sistema de logging para el registro de errores y eventos relevantes de la aplicación.
- Documentación completa del proyecto mediante JavaDoc.
