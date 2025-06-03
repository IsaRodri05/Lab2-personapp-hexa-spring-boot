package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.PhoneMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PhoneInputAdapterCli {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    private PhoneMapperCli phoneMapperCli;

    PhoneInputPort phoneInputPort;

    public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria, personOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo, personOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void crearTelefono(Scanner keyboard) {
        System.out.println("--- Crear Teléfono ---");

        System.out.print("Número: ");
        String numero = keyboard.nextLine();

        System.out.print("Compañía: ");
        String compañia = keyboard.nextLine();

        System.out.print("Cédula del dueño: ");
        int duenioCc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer

        Phone phone = new Phone();
        phone.setNumber(numero);
        phone.setCompany(compañia);

        Person owner = new Person();
        owner.setIdentification(duenioCc);
        phone.setOwner(owner);

        try {
            Phone created = phoneInputPort.create(phone);
            System.out.println("Teléfono creado: " + phoneMapperCli.fromDomainToAdapterCli(created));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void editarTelefono(Scanner keyboard) {
        System.out.println("\n--- Editar Teléfono ---");
        System.out.print("Ingrese el número del teléfono a editar: ");
        String numero = keyboard.nextLine();

        try {
            Phone existing = phoneInputPort.findOne(numero);
            System.out.println("\nTeléfono actual: " + phoneMapperCli.fromDomainToAdapterCli(existing));

            // Solo permitir editar la compañía
            System.out.print("\nNueva compañía [" + existing.getCompany() + "]: ");
            String compañia = keyboard.nextLine();

            // Si el usuario no ingresa nada, mantener el valor actual
            if (!compañia.isEmpty()) {
                existing.setCompany(compañia);
            }

            Phone result = phoneInputPort.edit(numero, existing);
            System.out.println("\nTeléfono actualizado: " + phoneMapperCli.fromDomainToAdapterCli(result));
        } catch (NoExistException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    public void eliminarTelefono(Scanner keyboard) {
        System.out.println("--- Eliminar Teléfono ---");
        System.out.print("Ingrese el número del teléfono a eliminar: ");
        String numero = keyboard.nextLine();

        try {
            boolean deleted = phoneInputPort.drop(numero);
            if (deleted) {
                System.out.println("Teléfono con número " + numero + " eliminado correctamente");
            } else {
                System.out.println("No se pudo eliminar el teléfono con número " + numero);
            }
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarTelefono(Scanner keyboard) {
        System.out.println("--- Buscar Teléfono ---");
        System.out.print("Ingrese el número del teléfono a buscar: ");
        String numero = keyboard.nextLine();

        try {
            Phone phone = phoneInputPort.findOne(numero);
            System.out.println(phoneMapperCli.fromDomainToAdapterCli(phone));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void historial() {
        System.out.println("--- Todos los Teléfonos ---");
        phoneInputPort.findAll().stream()
                .map(phoneMapperCli::fromDomainToAdapterCli)
                .forEach(System.out::println);
    }

    public void contarTelefonos() {
        System.out.println("--- Contar Teléfonos ---");
        int count = phoneInputPort.count();
        System.out.println("Total teléfonos: " + count);
    }

    public void obtenerTelefonosPersona(Scanner keyboard) {
        System.out.println("--- Teléfonos de Persona ---");
        System.out.print("Ingrese la cédula de la persona: ");
        int cc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer

        try {
            List<Phone> phones = phoneInputPort.getPersonPhones(cc);
            if (phones == null || phones.isEmpty()) {
                System.out.println("La persona no tiene teléfonos registrados");
            } else {
                System.out.println("Teléfonos:");
                phones.stream()
                        .map(phoneMapperCli::fromDomainToAdapterCli)
                        .forEach(System.out::println);
            }
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}