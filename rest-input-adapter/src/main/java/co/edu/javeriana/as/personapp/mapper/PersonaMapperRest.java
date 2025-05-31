package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {

    public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
        return fromDomainToAdapterRest(person, "MariaDB");
    }

    public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
        return fromDomainToAdapterRest(person, "MongoDB");
    }

    public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
        return new PersonaResponse(
                person.getIdentification() + "",
                person.getFirstName(),
                person.getLastName(),
                person.getAge() != null ? person.getAge().toString() : null,
                person.getGender(),
                database,
                "OK");
    }

    public Person fromAdapterToDomain(PersonaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Integer identification;
        try {
            identification = Integer.parseInt(request.getDni());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("DNI must be a valid integer", e);
        }

        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and last name are required");
        }

        // Validación y conversión del género usando el enum
        String gender = request.getSex();
        if (gender == null) {
            throw new IllegalArgumentException("Gender (sex) is required");
        }

        String normalizedGender = Gender.toDbValue(gender);

        Integer age = null;
        if (request.getAge() != null && !request.getAge().isEmpty()) {
            try {
                age = Integer.parseInt(request.getAge());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Age must be a valid integer", e);
            }
        }

        Person person = new Person(identification, firstName, lastName, normalizedGender);
        person.setAge(age);
        return person;
    }
}