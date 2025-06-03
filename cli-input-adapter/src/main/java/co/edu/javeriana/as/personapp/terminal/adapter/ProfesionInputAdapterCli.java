package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperCli profesionMapperCli;

    ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())){
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())){
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial1(){
        log.info("Into historial ProfessionEntity in Input Adapter");
        List<ProfesionModelCli> profesion = professionInputPort.findAll().stream()
            .map(profesionMapperCli::fromDomainToAdapterCli)
            .collect(Collectors.toList());
        profesion.forEach(p -> System.out.println(p.toString()));
    }

    public void historial() {
        log.info("Into historial ProfessionEntity in Input Adapter");
        professionInputPort.findAll().stream()
            .map(profesionMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }

    public Profession create(Integer identification, String name, String description) {
        ProfesionModelCli profesionModelCli = new ProfesionModelCli(identification, name, description);
        log.info("Into create ProfessionEntity in Input Adapter");
        return professionInputPort.create(profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
    }

    public Profession edit(Integer identificacion, String newName, String newDescription) throws NoExistException {
        log.info("Into edit ProfessionEntity in Input Adapter");
        ProfesionModelCli profesionModelCli = new ProfesionModelCli(identificacion, newName, newDescription);
        try {
            return professionInputPort.edit(identificacion, profesionMapperCli.fromAdapterCliToDomain(profesionModelCli));
        } catch (NoExistException e) {
            throw new NoExistException("Error in edit profession: " + e.getMessage());
        }
    }

    public Boolean drop(Integer identificacion) throws NoExistException {
        log.info("Into drop ProfessionEntity in Input Adapter");
        try {
            return professionInputPort.drop(identificacion);
        } catch (NoExistException e) {
            throw new NoExistException("Error in delete profession: "+ e.getMessage());
        }
    }

    public ProfesionModelCli findOne(Integer identificacion) throws NoExistException {
        log.info("Into findOne ProfessionEntity in Input Adapter");
        try {
            return profesionMapperCli.fromDomainToAdapterCli(professionInputPort.findOne(identificacion));
        } catch (NoExistException e) {
            throw new NoExistException("Error in find one profession: "+e.getMessage());
        }
    }

    public Integer count() {
        log.info("Into count ProfessionEntity in Input Adapter");
        return professionInputPort.count();
    }

    /*public List<EstudiosModelCli> getStudies(Integer identificacion) throws NoExistException {
        log.info("Into getStudies ProfessionEntity in Input Adapter");
        try {
            return professionInputPort.getStudies(identificacion).stream()
                .map(estudiosMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        } catch (NoExistException e) {
            throw new NoExistException(e.getMessage());
        }
    }*/
}
