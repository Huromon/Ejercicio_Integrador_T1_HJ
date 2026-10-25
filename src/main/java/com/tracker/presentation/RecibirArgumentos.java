package com.tracker.presentation;

import com.tracker.business.OfertaService;
import com.tracker.business.OfertaServicesint;
import com.tracker.data.repository.OfertaRepository;
import com.tracker.data.repository.OfertaRepositoryFileImpl;

/**
 * Gestiona la recepción y procesamiento de argumentos desde la línea de comandos.
 */
public class RecibirArgumentos {

    /**
     * Valida y ejecuta el comando solicitado según los argumentos recibidos.
     * @param arg array de argumentos de entrada
     */
    public static void argumento(String[] arg) {
        boolean esValido = validarArgumentosEntrada(arg);

        if (esValido) {
            OfertaRepository repository = new OfertaRepositoryFileImpl();
            OfertaServicesint servicio = new OfertaService(repository);

            String comando = arg[0].toLowerCase();

            try {
                ejecutarComando(servicio, comando, arg);
            } catch (NumberFormatException e) {
                System.out.println("Error: El ID proporcionado debe ser un número entero válido.");
            } catch (Exception e) {
                System.out.println("Error durante la ejecución del comando: " + e.getMessage());
            }
        }
    }

    /**
     * Comprueba que se haya introducido al menos un argumento.
     * @param arg array de argumentos
     * @return true si es válido, false en caso contrario
     */
    private static boolean validarArgumentosEntrada(String[] arg) {
        boolean esValido = true;
        if (arg == null || arg.length == 0) {
            System.out.println("Error: No se ha especificado ningún comando.");
            System.out.println("Comandos disponibles: -listar, -añadir, -ver, -borrar, -importar");
            esValido = false;
        }
        return esValido;
    }

    /**
     * Dirige la ejecución al método correspondiente según el comando indicado.
     * @param servicio instancia del servicio de negocio
     * @param comando comando principal
     * @param arg array de argumentos completos
     */
    private static void ejecutarComando(OfertaServicesint servicio, String comando, String[] arg) {
        switch (comando) {
            case "-listar", "listar" -> procesarListarOFiltrar(servicio, arg);
            case "-añadir", "añadir" -> procesarAnadir(servicio, arg);
            case "-borrar", "borrar", "-eliminar", "eliminar" -> procesarBorrar(servicio, arg);
            case "-ver", "ver" -> procesarVer(servicio, arg);
            case "-importar", "importar" -> procesarImportar(servicio, arg);
            default -> System.out.println("Error: Comando '" + arg[0] + "' no reconocido.");
        }
    }

    /**
     * Determina si se debe mostrar todo el listado o aplicar filtros.
     * @param servicio instancia del servicio de negocio
     * @param arg array de argumentos
     */
    private static void procesarListarOFiltrar(OfertaServicesint servicio, String[] arg) {
        boolean tieneFiltros = arg.length > 1;
        if (tieneFiltros) {
            procesarFiltrar(servicio, arg);
        } else {
            System.out.println(servicio.listar());
        }
    }

    /**
     * Procesa los parámetros introducidos para añadir una nueva oferta.
     * @param servicio instancia del servicio de negocio
     * @param arg array de argumentos
     */
    private static void procesarAnadir(OfertaServicesint servicio, String[] arg) {
        String titulo = null;
        String empresa = null;
        String estadoStr = null;
        String fechaStr = null;
        String enlace = "";
        String descripcion = "";
        Double salario = 0.0;

        for (int i = 0; i < arg.length - 1; i++) {
            String argActual = arg[i].toLowerCase();
            String siguienteValor = arg[i + 1];

            switch (argActual) {
                case "--titulo" -> titulo = siguienteValor;
                case "--empresa" -> empresa = siguienteValor;
                case "--estado" -> estadoStr = siguienteValor;
                case "--fecha" -> fechaStr = siguienteValor;
                case "--enlace" -> enlace = siguienteValor;
                case "--descripcion" -> descripcion = siguienteValor;
                case "--salario" -> salario = parsearSalarioSeguro(siguienteValor);
                default -> { }
            }
        }

        String resultado = servicio.anadir(titulo, empresa, estadoStr, fechaStr, enlace, descripcion, salario);
        System.out.println(resultado);
    }

    /**
     * Convierte de forma segura una cadena de texto en un valor Double para el salario.
     * @param valor texto del salario
     * @return valor Double parseado o 0.0 si hay error
     */
    private static Double parsearSalarioSeguro(String valor) {
        double salarioParseado = 0.0;
        try {
            salarioParseado = Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            salarioParseado = 0.0;
        }
        return salarioParseado;
    }

    /**
     * Extrae los criterios de filtrado y ejecuta la búsqueda por empresa o estado.
     * @param servicio instancia del servicio de negocio
     * @param arg array de argumentos
     */
    private static void procesarFiltrar(OfertaServicesint servicio, String[] arg) {
        String empresaFiltro = null;
        String estadoFiltro = null;

        for (int i = 0; i < arg.length - 1; i++) {
            String argActual = arg[i];
            String siguienteValor = arg[i + 1];

            if (argActual.equalsIgnoreCase("--empresa")) {
                empresaFiltro = siguienteValor;
            } else if (argActual.equalsIgnoreCase("--estado")) {
                estadoFiltro = siguienteValor;
            }
        }

        String resultado = servicio.filtrar(empresaFiltro, estadoFiltro);
        System.out.println(resultado);
    }

    /**
     * Procesa la eliminación de una candidatura utilizando su ID.
     * @param servicio instancia del servicio de negocio
     * @param arg array de argumentos
     */
    private static void procesarBorrar(OfertaServicesint servicio, String[] arg) {
        boolean parametroCorrecto = arg.length >= 2;
        if (parametroCorrecto) {
            int id = Integer.parseInt(arg[1]);
            System.out.println(servicio.eliminarCandidatura(id));
        } else {
            System.out.println("Error: Debe indicar el ID de la candidatura a eliminar. Ejemplo: -borrar 13");
        }
    }

    /**
     * Procesa la visualización de una candidatura concreta por ID.
     * @param servicio instancia del servicio de negocio
     * @param arg array de argumentos
     */
    private static void procesarVer(OfertaServicesint servicio, String[] arg) {
        boolean parametroCorrecto = arg.length >= 2;
        if (parametroCorrecto) {
            int id = Integer.parseInt(arg[1]);
            System.out.println(servicio.verCandidatura(id));
        } else {
            System.out.println("Error: Debe indicar el ID de la candidatura a consultar. Ejemplo: -ver 13");
        }
    }

    /**
     * Procesa la importación de candidaturas desde un archivo externo.
     * @param servicio instancia del servicio de negocio
     * @param arg array de argumentos
     */
    private static void procesarImportar(OfertaServicesint servicio, String[] arg) {
        boolean parametroCorrecto = arg.length >= 2;
        if (parametroCorrecto) {
            System.out.println(servicio.importar(arg[1]));
        } else {
            System.out.println("Error: Debe indicar la ruta del archivo CSV a importar. Ejemplo: -importar datos.csv");
        }
    }
}