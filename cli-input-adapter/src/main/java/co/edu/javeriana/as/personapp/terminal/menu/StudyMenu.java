// ruta: cli-input-adapter/src/main/java/co/edu/javeriana/as/personapp/terminal/menu/StudyMenu.java

package co.edu.javeriana.as.personapp.terminal.menu;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.adapter.StudyInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.extern.slf4j.Slf4j;

/**
 * Menú CLI para gestionar Studies (estudios).
 */
@Slf4j
public class StudyMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int CREAR_ESTUDIO           = 1;
    private static final int LISTAR_ESTUDIOS         = 2;
    private static final int BUSCAR_ESTUDIO          = 3;
    private static final int ACTUALIZAR_ESTUDIO      = 4;
    private static final int ELIMINAR_ESTUDIO        = 5;

    private final StudyInputAdapterCli studyAdapter;
    private final Scanner keyboard;

    public StudyMenu(StudyInputAdapterCli studyAdapter, Scanner keyboard) {
        this.studyAdapter = studyAdapter;
        this.keyboard = keyboard;
    }

    public void desplegarMenu() {
        int opcion = -1;
        do {
            mostrarOpciones();
            opcion = leerOpcion();
            try {
                switch (opcion) {
                    case CREAR_ESTUDIO:
                        handleCreate();
                        break;
                    case LISTAR_ESTUDIOS:
                        handleListAll();
                        break;
                    case BUSCAR_ESTUDIO:
                        handleGetById();
                        break;
                    case ACTUALIZAR_ESTUDIO:
                        handleUpdate();
                        break;
                    case ELIMINAR_ESTUDIO:
                        handleDelete();
                        break;
                    case OPCION_REGRESAR_MODULOS:
                        System.out.println(">> Regresando al menú principal...");
                        break;
                    default:
                        throw new InvalidOptionException("Opción inválida en módulo Estudio: " + opcion);
                }
            } catch (InvalidOptionException | NoExistException ex) {
                log.warn(ex.getMessage());
            }
        } while (opcion != OPCION_REGRESAR_MODULOS);
    }

    private void mostrarOpciones() {
        System.out.println("\n=== Módulo ESTUDIOS ===");
        System.out.println(CREAR_ESTUDIO       + ". Crear nuevo Estudio");
        System.out.println(LISTAR_ESTUDIOS     + ". Listar todos los Estudios");
        System.out.println(BUSCAR_ESTUDIO      + ". Buscar Estudio por ID (personId, professionId)");
        System.out.println(ACTUALIZAR_ESTUDIO  + ". Actualizar Estudio existente");
        System.out.println(ELIMINAR_ESTUDIO    + ". Eliminar Estudio");
        System.out.println(OPCION_REGRESAR_MODULOS + ". Regresar");
    }

    private int leerOpcion() {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Entrada inválida. Por favor, ingrese un número.");
            keyboard.nextLine(); // Descarta la entrada no numérica
            return leerOpcion();
        }
    }

    private void handleCreate() {
        System.out.println("-> CREANDO NUEVO ESTUDIO");
        try {
            StudyModelCli nuevo = leerDatosEstudio();
            StudyModelCli creado = studyAdapter.createStudy(nuevo);
            System.out.println("Estudio creado correctamente:\n  " + creado);
        } catch (Exception e) {
            log.warn("Error al crear Estudio: " + e.getMessage());
        }
    }

    private void handleListAll() {
        System.out.println("-> LISTANDO TODOS LOS ESTUDIOS");
        List<StudyModelCli> lista = studyAdapter.getAllStudies();
        if (lista.isEmpty()) {
            System.out.println("No hay estudios registrados.");
        } else {
            lista.forEach(est -> System.out.println("  - " + est));
        }
    }

    private void handleGetById() throws NoExistException {
        System.out.println("-> BUSCAR ESTUDIO POR ID");
        System.out.print("  Ingrese personId: ");
        Integer pid = keyboard.nextInt();
        System.out.print("  Ingrese professionId: ");
        Integer prid = keyboard.nextInt();
        keyboard.nextLine(); // Consumir salto de línea
        StudyModelCli encontrado = studyAdapter.getStudyById(pid, prid);
        System.out.println("Estudio encontrado:\n  " + encontrado);
    }

    private void handleUpdate() throws NoExistException {
        System.out.println("-> ACTUALIZANDO ESTUDIO EXISTENTE");
        StudyModelCli toUpdate = leerDatosEstudio();
        StudyModelCli actualizado = studyAdapter.updateStudy(toUpdate);
        System.out.println("Estudio actualizado:\n  " + actualizado);
    }

    private void handleDelete() throws NoExistException {
        System.out.println("-> ELIMINANDO ESTUDIO");
        System.out.print("  Ingrese personId: ");
        Integer pid = keyboard.nextInt();
        System.out.print("  Ingrese professionId: ");
        Integer prid = keyboard.nextInt();
        keyboard.nextLine(); // Consumir salto de línea
        boolean eliminado = studyAdapter.deleteStudy(pid, prid);
        if (eliminado) {
            System.out.println("Estudio eliminado correctamente.");
        } else {
            System.out.println("No se pudo eliminar el estudio (posiblemente no existía).");
        }
    }

    /**
     * Lee por consola todos los campos necesarios para un StudyModelCli.
     */
    private StudyModelCli leerDatosEstudio() {
        keyboard.nextLine(); // En caso de arrastre de buffer
        System.out.print("  personId (Integer): ");
        Integer pid = Integer.valueOf(keyboard.nextLine());
        System.out.print("  professionId (Integer): ");
        Integer prid = Integer.valueOf(keyboard.nextLine());
        System.out.print("  graduationDate (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(keyboard.nextLine());
        System.out.print("  universityName (String): ");
        String uni = keyboard.nextLine();
        return new StudyModelCli(pid, prid, fecha, uni);
    }
}
