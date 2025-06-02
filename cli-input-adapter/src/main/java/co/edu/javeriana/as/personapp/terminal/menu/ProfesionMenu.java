package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {
    
    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR_PROFESION = 2;
    private static final int OPCION_EDITAR_PROFESION = 3;
    private static final int OPCION_BUSCAR_PROFESION = 4;
    private static final int OPCION_ELIMINAR_PROFESION = 5;
    //private static final int OPCION_ESTUDIOS_POR_PROFESION = 6;

    public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do{
            try{
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        profesionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
                        menuOpciones(profesionInputAdapterCli,keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        profesionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
                        menuOpciones(profesionInputAdapterCli,keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while(!isValid);
    }

    private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do{
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                Scanner scanner_info;
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        isValid = true;
                        break;
                    case OPCION_VER_TODO:
                        profesionInputAdapterCli.historial();
                        break;
                    case OPCION_CREAR_PROFESION:
                        System.out.println("Ingrese la información de la profesión a crear:");
                        scanner_info = new Scanner(System.in);
                        System.out.println("Ingrese el identificador de la profesión:");
                        Integer identification = scanner_info.nextInt();
                        scanner_info.nextLine();
                        System.out.println("Ingrese el nombre de la profesión:");
                        String name = scanner_info.nextLine();
                        System.out.println("Ingrese la descripción de la profesión:");
                        String description = scanner_info.nextLine();
                        profesionInputAdapterCli.create(identification, name, description);
                        break;
                    case OPCION_EDITAR_PROFESION:
                        System.out.println("Ingrese el identificador de la profesión a editar:");
                        scanner_info = new Scanner(System.in);
                        Integer idToEdit = scanner_info.nextInt();
                        scanner_info.nextLine();
                        System.out.println("Ingrese la nueva información de la profesión:");
                        System.out.println("Nuevo nombre:");
                        String newName = scanner_info.nextLine();
                        System.out.println("Nueva descripción:");
                        String newDescription = scanner_info.nextLine();
                        try {
                            profesionInputAdapterCli.edit(idToEdit, newName, newDescription);
                        } catch (NoExistException e) {
                            log.warn("La profesión con el identificador proporcionado no existe.");
                        }
                        break;
                    case OPCION_BUSCAR_PROFESION:
                        System.out.println("Ingrese el identificador de la profesión a buscar:");
                        scanner_info = new Scanner(System.in);
                        Integer idToFind = scanner_info.nextInt();
                        scanner_info.nextLine();
                        try {
                            ProfesionModelCli foundProfesion = profesionInputAdapterCli.findOne(idToFind);
                            System.out.println("Profesión encontrada: " + foundProfesion);
                        } catch (NoExistException e) {
                            log.warn("La profesión con el identificador proporcionado no existe.");
                        }
                        break;
                    case OPCION_ELIMINAR_PROFESION:
                        System.out.println("Ingrese el identificador de la profesión a eliminar:");
                        scanner_info = new Scanner(System.in);
                        Integer idToDelete = scanner_info.nextInt();
                        scanner_info.nextLine();
                        try {
                            boolean deleted = profesionInputAdapterCli.drop(idToDelete);
                            if (deleted) {
                                System.out.println("Profesión con id" + idToDelete + " eliminada exitosamente.");
                            } else {
                                System.out.println("No se pudo eliminar la profesión.");
                            }
                        } catch (NoExistException e) {
                            log.warn("La profesión con el identificador proporcionado no existe.");
                        }
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
		System.out.println(OPCION_CREAR_PROFESION + " para crear una profesion");
        System.out.println(OPCION_EDITAR_PROFESION + " para editar una profesion");
        System.out.println(OPCION_BUSCAR_PROFESION + " para buscar una profesion");
        System.out.println(OPCION_ELIMINAR_PROFESION + " para eliminar una profesion");
        //System.out.println(OPCION_ESTUDIOS_POR_PROFESION + " para ver los estudios por profesion");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Entrada inválida. Por favor, ingrese un número.");
            return leerOpcion(keyboard);
        }
    }
}
