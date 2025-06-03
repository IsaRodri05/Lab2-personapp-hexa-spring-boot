package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PhoneInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_CREAR = 1;
    private static final int OPCION_EDITAR = 2;
    private static final int OPCION_ELIMINAR = 3;
    private static final int OPCION_BUSCAR = 4;
    private static final int OPCION_VER_TODO = 5;
    private static final int OPCION_CONTAR = 6;
    private static final int OPCION_TELEFONOS_PERSONA = 7;

    public void iniciarMenu(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        phoneInputAdapterCli.setPhoneOutputPortInjection("MARIA");
                        menuOpciones(phoneInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        phoneInputAdapterCli.setPhoneOutputPortInjection("MONGO");
                        menuOpciones(phoneInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                keyboard.nextLine(); // Limpiar buffer después de leer el número
                
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        isValid = true;
                        break;
                    case OPCION_CREAR:
                        phoneInputAdapterCli.crearTelefono(keyboard);
                        break;
                    case OPCION_EDITAR:
                        phoneInputAdapterCli.editarTelefono(keyboard);
                        break;
                    case OPCION_ELIMINAR:
                        phoneInputAdapterCli.eliminarTelefono(keyboard);
                        break;
                    case OPCION_BUSCAR:
                        phoneInputAdapterCli.buscarTelefono(keyboard);
                        break;
                    case OPCION_VER_TODO:
                        phoneInputAdapterCli.historial();
                        break;
                    case OPCION_CONTAR:
                        phoneInputAdapterCli.contarTelefonos();
                        break;
                    case OPCION_TELEFONOS_PERSONA:
                        phoneInputAdapterCli.obtenerTelefonosPersona(keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.nextLine(); // Limpiar buffer
            } catch (Exception e) {
                log.warn("Error: " + e.getMessage());
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_CREAR + " para crear un teléfono");
        System.out.println(OPCION_EDITAR + " para editar un teléfono");
        System.out.println(OPCION_ELIMINAR + " para eliminar un teléfono");
        System.out.println(OPCION_BUSCAR + " para buscar un teléfono");
        System.out.println(OPCION_VER_TODO + " para ver todos los teléfonos");
        System.out.println(OPCION_CONTAR + " para contar los teléfonos");
        System.out.println(OPCION_TELEFONOS_PERSONA + " para ver teléfonos de una persona");
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
            log.warn("Solo se permiten números.");
            keyboard.nextLine(); // Limpiar buffer
            return leerOpcion(keyboard);
        }
    }
}