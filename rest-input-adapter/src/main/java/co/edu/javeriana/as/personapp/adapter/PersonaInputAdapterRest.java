package co.edu.javeriana.as.personapp.adapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			if (setPersonOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}

		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PersonaResponse>();
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
		try {
			String dbOption = setPersonOutputPortInjection(request.getDatabase());
			Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));

			// Mapear la respuesta dependiendo de la base de datos usada
			if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personaMapperRest.fromDomainToAdapterRestMaria(person);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMongo(person);
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			return new PersonaResponse(
					request.getDni(), request.getFirstName(), request.getLastName(),
					request.getAge(), request.getSex(), request.getDatabase(), "Invalid database option");
		} catch (IllegalArgumentException e) {
			log.error("Invalid input data: {}", e.getMessage());
			return new PersonaResponse(
					request.getDni(), request.getFirstName(), request.getLastName(),
					request.getAge(), request.getSex(), request.getDatabase(), "Invalid input data");
		} catch (Exception e) {
			log.error("Unexpected error: {}", e.getMessage());
			return new PersonaResponse(
					request.getDni(), request.getFirstName(), request.getLastName(),
					request.getAge(), request.getSex(), request.getDatabase(), "Server error");
		}
	}

	public PersonaResponse editarPersona(PersonaRequest request) {
		try {
			String dbOption = setPersonOutputPortInjection(request.getDatabase());
			Person person = personaMapperRest.fromAdapterToDomain(request);

			// Verificar si la persona existe
			Person existingPerson = personInputPort.findOne(Integer.parseInt(request.getDni()));
			if (existingPerson == null) {
				return new PersonaResponse(
						request.getDni(), request.getFirstName(), request.getLastName(),
						request.getAge(), request.getSex(), request.getDatabase(),
						"Person not found");
			}

			// Actualizar la persona
			Person updatedPerson = personInputPort.edit(Integer.parseInt(request.getDni()), person);

			// Mapear la respuesta dependiendo de la base de datos usada
			if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personaMapperRest.fromDomainToAdapterRestMaria(updatedPerson);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMongo(updatedPerson);
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			return new PersonaResponse(
					request.getDni(), request.getFirstName(), request.getLastName(),
					request.getAge(), request.getSex(), request.getDatabase(), "Invalid database option");
		} catch (NumberFormatException e) {
			log.error("Invalid DNI format: {}", e.getMessage());
			return new PersonaResponse(
					request.getDni(), request.getFirstName(), request.getLastName(),
					request.getAge(), request.getSex(), request.getDatabase(), "Invalid DNI format");
		} catch (Exception e) {
			log.error("Unexpected error: {}", e.getMessage());
			return new PersonaResponse(
					request.getDni(), request.getFirstName(), request.getLastName(),
					request.getAge(), request.getSex(), request.getDatabase(), "Server error");
		}
	}

	public Response eliminarPersona(String database, String dni) {
		try {
			setPersonOutputPortInjection(database);
			int id = Integer.parseInt(dni);

			// Verificar si la persona existe
			Person existingPerson = personInputPort.findOne(id);
			if (existingPerson == null) {
				return new Response("NOT_FOUND", "Person with DNI " + dni + " not found", LocalDateTime.now());
			}

			// Eliminar la persona
			boolean deleted = personInputPort.drop(id);
			if (deleted) {
				return new Response("SUCCESS", "Person with DNI " + dni + " deleted successfully", LocalDateTime.now());
			} else {
				return new Response("ERROR", "Failed to delete person with DNI " + dni, LocalDateTime.now());
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			return new Response("ERROR", "Invalid database option: " + database, LocalDateTime.now());
		} catch (NumberFormatException e) {
			log.error("Invalid DNI format: {}", e.getMessage());
			return new Response("ERROR", "Invalid DNI format: " + dni, LocalDateTime.now());
		} catch (Exception e) {
			log.error("Unexpected error: {}", e.getMessage());
			return new Response("ERROR", "Server error while deleting person", LocalDateTime.now());
		}
	}

}
