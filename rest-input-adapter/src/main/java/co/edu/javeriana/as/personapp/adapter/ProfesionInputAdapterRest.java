package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {
    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperRest profesionMapperRest;

    ProfessionInputPort professionInputPort;

    private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())){
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())){
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else { 
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<ProfesionResponse> historial(String database) {
        log.info("Info historial ProfessionEntity in Input Adapter");
        try{
            if (setProfessionOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionInputPort.findAll().stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return professionInputPort.findAll().stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) { 
            log.warn(e.getMessage());
            return new ArrayList<ProfesionResponse>();
        }
    }

    public ProfesionResponse crearProfesion(ProfesionRequest request) { 
        try {
            String dbOption = setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = professionInputPort.create(profesionMapperRest.fromAdapterToDomain(request));

            if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
            } else {
                return profesionMapperRest.fromDomainToAdapterRestMongo(profession);
            }
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", e.getMessage());
            return new ProfesionResponse(
                request.getIdentificacion(),
                request.getNombre(),
                request.getDescripcion(),
                request.getDatabase(),
                "Invalid database option"
            );
        } catch (IllegalArgumentException e) {
            log.error("Invalid input data: {}", e.getMessage());
            return new ProfesionResponse(
                request.getIdentificacion(),
                request.getNombre(),
                request.getDescripcion(),
                request.getDatabase(),
                "Invalid input data: "
            );
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return new ProfesionResponse(
                request.getIdentificacion(),
                request.getNombre(),
                request.getDescripcion(),
                request.getDatabase(),
                "Server error"
            );
        }
    }

    public ProfesionResponse editarProfesion(ProfesionRequest request)  {
        try {
            String dbOption = setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = professionInputPort.edit(
                Integer.parseInt(request.getIdentificacion()), 
                profesionMapperRest.fromAdapterToDomain(request)
            );

            if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
            } else {
                return profesionMapperRest.fromDomainToAdapterRestMongo(profession);
            }

        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", e.getMessage());
            return new ProfesionResponse(
                request.getIdentificacion(),
                request.getNombre(),
                request.getDescripcion(),
                request.getDatabase(),
                "Invalid database option"
            );
        } catch (IllegalArgumentException e) {
            log.error("Invalid input data: {}", e.getMessage());
            return new ProfesionResponse(
                request.getIdentificacion(),
                request.getNombre(),
                request.getDescripcion(),
                request.getDatabase(),
                "Invalid input data: "
            );
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return new ProfesionResponse(
                request.getIdentificacion(),
                request.getNombre(),
                request.getDescripcion(),
                request.getDatabase(),
                "Server error"
            );
        }
    }

    public Boolean eliminarProfesion(Integer identificacion, String database) {
        try {
            setProfessionOutputPortInjection(database);
            return professionInputPort.drop(identificacion);
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Invalid input data: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return false;
        }
    }

    public ProfesionResponse buscarUnaProfesion(Integer identificacion, String dbOption) {
        try {
            Profession profession = professionInputPort.findOne(identificacion);
            if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
            } else {
                return profesionMapperRest.fromDomainToAdapterRestMongo(profession);
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid input data: {}", e.getMessage());
            return new ProfesionResponse(
                String.valueOf(identificacion),
                null,
                null,
                dbOption,
                "Invalid database option"
            );
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return new ProfesionResponse(
                String.valueOf(identificacion),
                null,
                null,
                dbOption,
                "Invalid database option"
            );
        }
    }
}
