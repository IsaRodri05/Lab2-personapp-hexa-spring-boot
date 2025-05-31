package co.edu.javeriana.as.personapp.mongo.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

@Mapper
public class PersonaMapperMongo {

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Autowired
    private TelefonoMapperMongo telefonoMapperMongo;

    public PersonaDocument fromDomainToAdapter(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        PersonaDocument personaDocument = new PersonaDocument();
        personaDocument.setId(person.getIdentification());
        personaDocument.setNombre(person.getFirstName());
        personaDocument.setApellido(person.getLastName());
        personaDocument.setGenero(person.getGender()); // Ya está en formato correcto
        personaDocument.setEdad(validateEdad(person.getAge()));
        personaDocument.setEstudios(validateEstudios(person.getStudies()));
        personaDocument.setTelefonos(validateTelefonos(person.getPhoneNumbers()));
        return personaDocument;
    }

    private Integer validateEdad(Integer age) {
        return age != null && age >= 0 ? age : null;
    }

    private List<EstudiosDocument> validateEstudios(List<Study> studies) {
        return studies != null ? studies.stream()
                .map(estudiosMapperMongo::fromDomainToAdapter)
                .collect(Collectors.toList()) 
                : new ArrayList<>();
    }

    private List<TelefonoDocument> validateTelefonos(List<Phone> phoneNumbers) {
        return phoneNumbers != null ? phoneNumbers.stream()
                .map(telefonoMapperMongo::fromDomainToAdapter)
                .collect(Collectors.toList()) 
                : new ArrayList<>();
    }

    public Person fromAdapterToDomain(PersonaDocument personaDocument) {
        if (personaDocument == null) {
            throw new IllegalArgumentException("PersonaDocument cannot be null");
        }

        Person person = new Person();
        person.setIdentification(personaDocument.getId());
        person.setFirstName(personaDocument.getNombre());
        person.setLastName(personaDocument.getApellido());
        person.setGender(personaDocument.getGenero()); // Asume que ya está en formato M/F/O
        person.setAge(personaDocument.getEdad());
        person.setStudies(validateStudies(personaDocument.getEstudios()));
        person.setPhoneNumbers(validatePhones(personaDocument.getTelefonos()));
        return person;
    }

    private List<Study> validateStudies(List<EstudiosDocument> estudiosDocuments) {
        return estudiosDocuments != null ? estudiosDocuments.stream()
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList()) 
                : new ArrayList<>();
    }

    private List<Phone> validatePhones(List<TelefonoDocument> telefonosDocuments) {
        return telefonosDocuments != null ? telefonosDocuments.stream()
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList()) 
                : new ArrayList<>();
    }
}
