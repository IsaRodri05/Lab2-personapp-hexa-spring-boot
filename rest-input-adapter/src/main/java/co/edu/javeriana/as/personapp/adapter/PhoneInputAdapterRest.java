package co.edu.javeriana.as.personapp.adapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.mapper.PhoneMapperRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PhoneInputAdapterRest {

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
    private PhoneMapperRest phoneMapperRest;

    private PhoneInputPort phoneInputPort;
    private PersonInputPort personInputPort;

    private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria, personOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo, personOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        }
        throw new InvalidOptionException("Invalid database option: " + dbOption);
    }

    public List<PhoneResponse> historial(String database) {
        log.info("Into historial Phone in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            List<Phone> phones = phoneInputPort.findAll();

            return phones.stream()
                    .map(phone -> database.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                            ? phoneMapperRest.fromDomainToAdapterRestMaria(phone)
                            : phoneMapperRest.fromDomainToAdapterRestMongo(phone))
                    .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Error getting phone history: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public PhoneResponse crearTelefono(PhoneRequest request) {
        try {
            String dbOption = setPhoneOutputPortInjection(request.getDatabase());

            // Validación básica
            if (request.getNumber() == null || request.getCompany() == null) {
                throw new IllegalArgumentException("Phone number and company are required");
            }

            Phone phone = new Phone();
            phone.setNumber(request.getNumber());
            phone.setCompany(request.getCompany());

            // Manejo del dueño
            if (request.getOwnerDni() != null && !request.getOwnerDni().isEmpty()) {
                Person owner = getAndValidateOwner(request.getOwnerDni());
                phone.setOwner(owner);
                phone = phoneInputPort.addPhoneToPerson(owner.getIdentification(), phone);
            } else {
                phone = phoneInputPort.create(phone);
            }

            return dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                    ? phoneMapperRest.fromDomainToAdapterRestMaria(phone)
                    : phoneMapperRest.fromDomainToAdapterRestMongo(phone);

        } catch (Exception e) {
            log.error("Error creating phone: {}", e.getMessage());
            return buildErrorResponse(request, e.getMessage());
        }
    }

    private Person getAndValidateOwner(String ownerDni) throws NoExistException, IllegalArgumentException {
        try {
            Integer dni = Integer.parseInt(ownerDni);
            Person owner = personInputPort.findOne(dni);

            // Validar campos no nulos
            if (owner.getGender() == null)
                owner.setGender(Gender.OTHER.name());
            if (owner.getFirstName() == null)
                owner.setFirstName("");
            if (owner.getLastName() == null)
                owner.setLastName("");
            if (owner.getAge() == null)
                owner.setAge(0);

            return owner;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Owner DNI must be a valid number");
        }
    }

    private PhoneResponse buildErrorResponse(PhoneRequest request, String error) {
        return new PhoneResponse(
                request.getNumber(),
                request.getCompany(),
                request.getOwnerDni(),
                request.getDatabase(),
                error);
    }

    public PhoneResponse editarTelefono(String number, PhoneRequest request) {
        try {
            String dbOption = setPhoneOutputPortInjection(request.getDatabase());
            Phone phone = phoneMapperRest.fromAdapterToDomain(request);

            Phone updatedPhone = phoneInputPort.edit(number, phone);

            return dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())
                    ? phoneMapperRest.fromDomainToAdapterRestMaria(updatedPhone)
                    : phoneMapperRest.fromDomainToAdapterRestMongo(updatedPhone);
        } catch (NoExistException e) {
            log.error("Phone not found: {}", e.getMessage());
            return new PhoneResponse(number, request.getCompany(), request.getOwnerDni(),
                    request.getDatabase(), "Phone not found");
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", e.getMessage());
            return new PhoneResponse(number, request.getCompany(), request.getOwnerDni(),
                    request.getDatabase(), "Invalid database option");
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return new PhoneResponse(number, request.getCompany(), request.getOwnerDni(),
                    request.getDatabase(), "Server error");
        }
    }

    public Response eliminarTelefono(String database, String phoneNumber) {
        try {
            setPhoneOutputPortInjection(database);
            boolean deleted = phoneInputPort.drop(phoneNumber);

            return new Response(
                    deleted ? "SUCCESS" : "ERROR",
                    deleted ? "Phone deleted successfully" : "Failed to delete phone",
                    LocalDateTime.now());
        } catch (NoExistException e) {
            log.error("Phone not found: {}", e.getMessage());
            return new Response("NOT_FOUND", "Phone with number " + phoneNumber + " not found", LocalDateTime.now());
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", e.getMessage());
            return new Response("ERROR", "Invalid database option: " + database, LocalDateTime.now());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return new Response("ERROR", "Server error while deleting phone", LocalDateTime.now());
        }
    }

}