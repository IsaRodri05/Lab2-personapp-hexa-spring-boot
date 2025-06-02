package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.model.request.EditStudyRequest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

@Component
public class StudyInputAdapterRest {

    @Autowired
    private StudyInputPort studyUseCase;

    @Autowired
    private PersonInputPort personUseCase;

    @Autowired
    private ProfessionInputPort professionUseCase;

    @Autowired
    private StudyMapperRest studyMapper;

    public StudyResponse createStudy(StudyRequest request) {
        Study domain = studyMapper.fromRequestToDomain(request);
        if (domain == null) {
            return null;
        }
        Study created = studyUseCase.createStudy(domain);
        return studyMapper.fromDomainToResponse(created);
    }

    public List<StudyResponse> getAllStudies() {
        List<Study> list = studyUseCase.getAllStudies();
        return list.stream()
                   .map(studyMapper::fromDomainToResponse)
                   .collect(Collectors.toList());
    }

    public StudyResponse getStudyById(Integer personId, Integer professionId) {
        Study domain = studyUseCase.getStudyById(personId, professionId);
        return studyMapper.fromDomainToResponse(domain);
    }

    public StudyResponse updateStudy(Integer personId, Integer professionId, EditStudyRequest request) {
        Integer personIdInt = personId.intValue();
        Integer professionIdInt = professionId.intValue();

        Person persona;
        try {
            persona = personUseCase.findOne(personIdInt);
        } catch (NoExistException ex) {
            return null;
        }

        Profession profesion;
        try {
            profesion = professionUseCase.findOne(professionIdInt);
        } catch (NoExistException ex) {
            return null;
        }

        Study estudioParaActualizar = new Study();
        estudioParaActualizar.setPerson(persona);
        estudioParaActualizar.setProfession(profesion);
        estudioParaActualizar.setGraduationDate(request.getGraduationDate());
        estudioParaActualizar.setUniversityName(request.getUniversityName());

        Study updated = studyUseCase.updateStudy(personId, professionId, estudioParaActualizar);
        return studyMapper.fromDomainToResponse(updated);
    }

    public boolean deleteStudy(Integer personId, Integer professionId) {
        return studyUseCase.deleteStudy(personId, professionId);
    }
}
