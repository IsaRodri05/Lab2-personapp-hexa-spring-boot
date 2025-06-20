package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

@Mapper
public class PersonaMapperCli {

    public PersonaModelCli fromDomainToAdapterCli(Person person) {
        PersonaModelCli personaModelCli = new PersonaModelCli();
        personaModelCli.setCc(person.getIdentification());
        personaModelCli.setNombre(person.getFirstName());
        personaModelCli.setApellido(person.getLastName());
        personaModelCli.setGenero(person.getGender());
        personaModelCli.setEdad(person.getAge());
        return personaModelCli;
    }

    public Person fromAdapterCliToDomain(PersonaModelCli personaModelCli) {
        return new Person(
            personaModelCli.getCc(),
            personaModelCli.getNombre(),
            personaModelCli.getApellido(),
            personaModelCli.getGenero(),
            personaModelCli.getEdad()
        );
    }
}