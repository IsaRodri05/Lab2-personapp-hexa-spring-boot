package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.model.request.EditStudyRequest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StudyInputAdapterRest {

    @Autowired 
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired 
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    private StudyUseCase studyUseCase;

    @Autowired
    private PersonInputPort personUseCase;

    @Autowired
    private ProfessionInputPort professionUseCase;

    @Autowired
    private StudyMapperRest studyMapper;


    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption == null) {
            throw new InvalidOptionException("Database option no puede ser nula");
        }

        String dbUpper = dbOption.toUpperCase();
        if (dbUpper.equals(DatabaseOption.MARIA.toString())) {
            studyUseCase = new StudyUseCase(studyOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbUpper.equals(DatabaseOption.MONGO.toString())) {
            studyUseCase = new StudyUseCase(studyOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }


    public StudyResponse createStudy(StudyRequest request) {
        String dbOption = request.getDatabase();
        try {
            String dbUsed = setStudyOutputPortInjection(dbOption);
            log.info("Creando Study en DB={}", dbUsed);

            Study domain = studyMapper.fromRequestToDomain(request);
            if (domain == null) {
                log.error("Estudio inválido en fromRequestToDomain");
                return null;
            }

            Study created = studyUseCase.createStudy(domain);
            return studyMapper.fromDomainToResponse(created);

        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", e.getMessage());

            return new StudyResponse(
                String.valueOf(request.getPersonId()),
                String.valueOf(request.getProfessionId()),
                request.getGraduationDate(),
                request.getUniversityName(),
                "Invalid database option"
            );
        } catch (Exception e) {
            log.error("Error inesperado en createStudy: {}", e.getMessage());
            return new StudyResponse(
                String.valueOf(request.getPersonId()),
                String.valueOf(request.getProfessionId()),
                request.getGraduationDate(),
                request.getUniversityName(),
                "Unexpected error"
            );
        }
    }


    public List<StudyResponse> getAllStudies(String dbOption) {
        try {
            String dbUsed = setStudyOutputPortInjection(dbOption);
            log.info("Listando todos los Study en DB={}", dbUsed);

            List<Study> all = studyUseCase.getAllStudies();
            return all.stream()
                      .map(studyMapper::fromDomainToResponse)
                      .collect(Collectors.toList());

        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", e.getMessage());
            return List.<StudyResponse>of();
        } catch (Exception e) {
            log.error("Error inesperado en getAllStudies: {}", e.getMessage());
            return List.<StudyResponse>of();
        }
    }


    public StudyResponse getStudyById(Integer personId, Integer professionId, String dbOption) {
        try {
            String dbUsed = setStudyOutputPortInjection(dbOption);
            log.info("Buscando Study por IDs (personId={}, professionId={}) en DB={}", 
                     personId, professionId, dbUsed);

            Study domain = studyUseCase.getStudyById(personId, professionId);
            return studyMapper.fromDomainToResponse(domain);

        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", e.getMessage());
            return new StudyResponse(
                String.valueOf(personId),
                String.valueOf(professionId),
                null,
                null,
                "Invalid database option"
            );
        } catch (Exception e) {
            log.error("Error inesperado en getStudyById: {}", e.getMessage());
            return new StudyResponse(
                String.valueOf(personId),
                String.valueOf(professionId),
                null,
                null,
                "Unexpected error"
            );
        }
    }


    public StudyResponse updateStudy(Integer personId, Integer professionId, 
                                     EditStudyRequest request, String dbOption) {
        try {
            String dbUsed = setStudyOutputPortInjection(dbOption);
            log.info("Actualizando Study (personId={}, professionId={}) en DB={}", 
                     personId, professionId, dbUsed);

            Person persona;
            try {
                persona = personUseCase.findOne(personId);
            } catch (Exception ex) {
                log.error("La persona con ID={} no existe", personId);
                return null;
            }

            Profession profesion;
            try {
                profesion = professionUseCase.findOne(professionId);
            } catch (Exception ex) {
                log.error("La profesión con ID={} no existe", professionId);
                return null;
            }

            Study estudioParaActualizar = new Study();
            estudioParaActualizar.setPerson(persona);
            estudioParaActualizar.setProfession(profesion);
            estudioParaActualizar.setGraduationDate(request.getGraduationDate());
            estudioParaActualizar.setUniversityName(request.getUniversityName());

            Study updated = studyUseCase.updateStudy(personId, professionId, estudioParaActualizar);
            return studyMapper.fromDomainToResponse(updated);

        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", e.getMessage());
            return new StudyResponse(
                String.valueOf(personId),
                String.valueOf(professionId),
                request.getGraduationDate(),
                request.getUniversityName(),
                "Invalid database option"
            );
        } catch (Exception e) {
            log.error("Error inesperado en updateStudy: {}", e.getMessage());
            return new StudyResponse(
                String.valueOf(personId),
                String.valueOf(professionId),
                request.getGraduationDate(),
                request.getUniversityName(),
                "Unexpected error"
            );
        }
    }

    public boolean deleteStudy(Integer personId, Integer professionId, String dbOption) {
        try {
            String dbUsed = setStudyOutputPortInjection(dbOption);
            log.info("Eliminando Study (personId={}, professionId={}) en DB={}", 
                     personId, professionId, dbUsed);

            return studyUseCase.deleteStudy(personId, professionId);

        } catch (InvalidOptionException e) {
            log.error("Opción de base de datos no válida: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Error inesperado en deleteStudy: {}", e.getMessage());
            return false;
        }
    }
}
