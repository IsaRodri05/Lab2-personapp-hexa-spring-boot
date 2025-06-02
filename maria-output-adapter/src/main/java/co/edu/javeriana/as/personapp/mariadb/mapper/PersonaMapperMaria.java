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
        if (person == null) return null;

        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setCc(person.getIdentification());
        personaEntity.setNombre(person.getFirstName());
        personaEntity.setApellido(person.getLastName());
        personaEntity.setGenero(Gender.toCharDbValue(person.getGender()));
        personaEntity.setEdad(validateEdad(person.getAge()));

        // Mapear estudios si existen
        if (person.getStudies() != null) {
            List<EstudiosEntity> estudios = person.getStudies().stream()
                .map(estudiosMapperMaria::fromDomainToAdapter)
                .collect(Collectors.toList());
            estudios.forEach(e -> e.setPersona(personaEntity)); // Establecer relación inversa
            personaEntity.setEstudios(estudios);
        }

        // Mapear teléfonos si existen
        if (person.getPhoneNumbers() != null) {
            List<TelefonoEntity> telefonos = person.getPhoneNumbers().stream()
                .map(phone -> {
                    TelefonoEntity tel = telefonoMapperMaria.fromDomainToAdapter(phone);
                    tel.setDuenio(personaEntity); // Referencia inversa
                    return tel;
                })
                .collect(Collectors.toList());
            personaEntity.setTelefonos(telefonos);
        }

        return personaEntity;
    }

    public Person fromAdapterToDomain(PersonaEntity personaEntity) {
        if (personaEntity == null) return null;

        Person person = new Person(
            personaEntity.getCc(),
            personaEntity.getNombre(),
            personaEntity.getApellido(),
            String.valueOf(personaEntity.getGenero()),
            personaEntity.getEdad()
        );

        // Mapear estudios si existen
        if (personaEntity.getEstudios() != null) {
            person.setStudies(
                personaEntity.getEstudios().stream()
                    .map(estudiosMapperMaria::fromAdapterToDomain)
                    .collect(Collectors.toList())
            );
        }

        // Mapear teléfonos si existen
        if (personaEntity.getTelefonos() != null) {
            person.setPhoneNumbers(
                personaEntity.getTelefonos().stream()
                    .map(tel -> {
                        Phone phone = telefonoMapperMaria.fromAdapterToDomain(tel);
                        phone.setOwner(person); // Referencia inversa
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
