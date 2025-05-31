package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;

@Mapper
public class PersonaMapperMaria {

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    public PersonaEntity fromDomainToAdapter(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setCc(person.getIdentification());
        personaEntity.setNombre(person.getFirstName());
        personaEntity.setApellido(person.getLastName());
        personaEntity.setGenero(Gender.toCharDbValue(person.getGender()));
        personaEntity.setEdad(validateEdad(person.getAge()));
        personaEntity.setEstudios(mapStudiesToEntities(person.getStudies()));
        personaEntity.setTelefonos(mapPhonesToEntities(person.getPhoneNumbers()));
        return personaEntity;
    }

    public Person fromAdapterToDomain(PersonaEntity personaEntity) {
        if (personaEntity == null) {
            throw new IllegalArgumentException("PersonaEntity cannot be null");
        }

        Person person = new Person();
        person.setIdentification(personaEntity.getCc());
        person.setFirstName(personaEntity.getNombre());
        person.setLastName(personaEntity.getApellido());
        person.setGender(String.valueOf(personaEntity.getGenero()));
        person.setAge(personaEntity.getEdad());
        person.setStudies(mapEntitiesToStudies(personaEntity.getEstudios()));
        person.setPhoneNumbers(mapEntitiesToPhones(personaEntity.getTelefonos()));
        return person;
    }

    private Integer validateEdad(Integer age) {
        return age != null && age >= 0 ? age : null;
    }

    private List<EstudiosEntity> mapStudiesToEntities(List<Study> studies) {
        return studies != null ? 
            studies.stream()
                .map(estudiosMapperMaria::fromDomainToAdapter)
                .collect(Collectors.toList()) : 
            new ArrayList<>();
    }

    private List<TelefonoEntity> mapPhonesToEntities(List<Phone> phones) {
        return phones != null ? 
            phones.stream()
                .map(telefonoMapperMaria::fromDomainToAdapter)
                .collect(Collectors.toList()) : 
            new ArrayList<>();
    }

    private List<Study> mapEntitiesToStudies(List<EstudiosEntity> estudiosEntities) {
        return estudiosEntities != null ? 
            estudiosEntities.stream()
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList()) : 
            new ArrayList<>();
    }

    private List<Phone> mapEntitiesToPhones(List<TelefonoEntity> telefonoEntities) {
        return telefonoEntities != null ? 
            telefonoEntities.stream()
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList()) : 
            new ArrayList<>();
    }
}