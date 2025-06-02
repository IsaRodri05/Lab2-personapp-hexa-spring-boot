package co.edu.javeriana.as.personapp.mongo.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

@Mapper
public class PersonaMapperMongo {

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Autowired
    private TelefonoMapperMongo telefonoMapperMongo;

    public PersonaDocument fromDomainToAdapter(Person person) {
        if (person == null) return null;

        PersonaDocument personaDocument = new PersonaDocument();
        personaDocument.setId(person.getIdentification());
        personaDocument.setNombre(person.getFirstName());
        personaDocument.setApellido(person.getLastName());
        personaDocument.setGenero(person.getGender());
        personaDocument.setEdad(validateEdad(person.getAge()));
        
        if(person.getStudies() != null) {
            personaDocument.setEstudios(
                person.getStudies().stream()
                    .map(s -> estudiosMapperMongo.fromDomainToAdapter(s))
                    .collect(Collectors.toList())
            );
        }
        
        if(person.getPhoneNumbers() != null) {
            personaDocument.setTelefonos(
                person.getPhoneNumbers().stream()
                    .map(p -> {
                        TelefonoDocument telDoc = telefonoMapperMongo.fromDomainToAdapter(p);
                        telDoc.setPrimaryDuenio(personaDocument); 
                        return telDoc;
                    })
                    .collect(Collectors.toList())
            );
        }
        
        return personaDocument;
    }

    public Person fromAdapterToDomain(PersonaDocument personaDocument) {
        if (personaDocument == null) return null;

        Person person = new Person(
            personaDocument.getId(),
            personaDocument.getNombre(),
            personaDocument.getApellido(),
            personaDocument.getGenero(),
            personaDocument.getEdad()
        );

        // Mapeamos estudios
        if(personaDocument.getEstudios() != null) {
            person.setStudies(
                personaDocument.getEstudios().stream()
                    .map(estudiosMapperMongo::fromAdapterToDomain)
                    .collect(Collectors.toList())
            );
        }

        if(personaDocument.getTelefonos() != null) {
            person.setPhoneNumbers(
                personaDocument.getTelefonos().stream()
                    .map(t -> {
                        Phone phone = telefonoMapperMongo.fromAdapterToDomain(t);
                        phone.setOwner(person); 
                        return phone;
                    })
                    .collect(Collectors.toList())
            );
        }
        
        return person;
    }

    private Integer validateEdad(Integer age) {
        return age != null && age >= 0 ? age : null;
    }
}