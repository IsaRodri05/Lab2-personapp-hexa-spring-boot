package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    private PersonaMapperCli personaMapperCli;

    PersonInputPort personInputPort;

    public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void crearPersona(Scanner keyboard) {
        System.out.println("--- Crear Persona ---");
        keyboard.nextLine(); // Limpiar buffer
        
        System.out.print("Cédula: ");
        int cc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer
        
        System.out.print("Nombre: ");
        String nombre = keyboard.nextLine();
        
        System.out.print("Apellido: ");
        String apellido = keyboard.nextLine();
        
        System.out.print("Género: ");
        String genero = keyboard.nextLine();
        
        System.out.print("Edad: ");
        int edad = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer
        
        Person person = new Person(cc, nombre, apellido, genero, edad);
        Person created = personInputPort.create(person);
        System.out.println("Persona creada: " + personaMapperCli.fromDomainToAdapterCli(created));
    }

    public void editarPersona(Scanner keyboard) {
        System.out.println("--- Editar Persona ---");
        System.out.print("Ingrese la cédula de la persona a editar: ");
        int cc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer
        
        try {
            // Obtener persona existente
            Person existing = personInputPort.findOne(cc);
            System.out.println("Persona actual: " + personaMapperCli.fromDomainToAdapterCli(existing));
            
            System.out.print("Nuevo nombre (" + existing.getFirstName() + "): ");
            String nombre = keyboard.nextLine();
            nombre = nombre.isEmpty() ? existing.getFirstName() : nombre;
            
            System.out.print("Nuevo apellido (" + existing.getLastName() + "): ");
            String apellido = keyboard.nextLine();
            apellido = apellido.isEmpty() ? existing.getLastName() : apellido;
            
            System.out.print("Nuevo género (" + existing.getGender() + "): ");
            String genero = keyboard.nextLine();
            genero = genero.isEmpty() ? existing.getGender() : genero;
            
            System.out.print("Nueva edad (" + existing.getAge() + "): ");
            String edadStr = keyboard.nextLine();
            int edad = edadStr.isEmpty() ? existing.getAge() : Integer.parseInt(edadStr);
            
            Person updated = new Person(cc, nombre, apellido, genero, edad);
            Person result = personInputPort.edit(cc, updated);
            System.out.println("Persona actualizada: " + personaMapperCli.fromDomainToAdapterCli(result));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarPersona(Scanner keyboard) {
        System.out.println("--- Eliminar Persona ---");
        System.out.print("Ingrese la cédula de la persona a eliminar: ");
        int cc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer
        
        try {
            boolean deleted = personInputPort.drop(cc);
            if (deleted) {
                System.out.println("Persona con cédula " + cc + " eliminada correctamente");
            } else {
                System.out.println("No se pudo eliminar la persona con cédula " + cc);
            }
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPersona(Scanner keyboard) {
        System.out.println("--- Buscar Persona ---");
        System.out.print("Ingrese la cédula de la persona a buscar: ");
        int cc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer
        
        try {
            Person person = personInputPort.findOne(cc);
            System.out.println(personaMapperCli.fromDomainToAdapterCli(person));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void historial() {
        System.out.println("--- Todas las Personas ---");
        personInputPort.findAll().stream()
            .map(personaMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }

    public void contarPersonas() {
        System.out.println("--- Contar Personas ---");
        int count = personInputPort.count();
        System.out.println("Total personas: " + count);
    }

    public void obtenerTelefonos(Scanner keyboard) {
        System.out.println("--- Teléfonos de Persona ---");
        System.out.print("Ingrese la cédula de la persona: ");
        int cc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer
        
        try {
            List<Phone> phones = personInputPort.getPhones(cc);
            if (phones == null || phones.isEmpty()) {
                System.out.println("La persona no tiene teléfonos registrados");
            } else {
                System.out.println("Teléfonos:");
                phones.forEach(System.out::println);
            }
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void obtenerEstudios(Scanner keyboard) {
        System.out.println("--- Estudios de Persona ---");
        System.out.print("Ingrese la cédula de la persona: ");
        int cc = keyboard.nextInt();
        keyboard.nextLine(); // Limpiar buffer
        
        try {
            List<Study> studies = personInputPort.getStudies(cc);
            if (studies == null || studies.isEmpty()) {
                System.out.println("La persona no tiene estudios registrados");
            } else {
                System.out.println("Estudios:");
                studies.forEach(System.out::println);
            }
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}