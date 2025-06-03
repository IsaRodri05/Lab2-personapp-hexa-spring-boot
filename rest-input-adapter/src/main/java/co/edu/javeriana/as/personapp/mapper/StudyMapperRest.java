package co.edu.javeriana.as.personapp.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;

@Mapper
public class StudyMapperRest {

    @Autowired
    private PersonInputPort personUseCase;
    @Autowired
    private ProfessionInputPort professionUseCase;

    public Study fromRequestToDomain(StudyRequest request) {
        Person p;
        try {
            p = personUseCase.findOne(request.getPersonId());
        } catch (NoExistException ex) {
            return null;
        }
        
        Profession prof;
        try {
            prof = professionUseCase.findOne(request.getProfessionId());
        } catch (NoExistException ex) {
            return null;
        }

        if (p == null || prof == null) {
            return null;
        }
        Study s = new Study();
        s.setPerson(p);
        s.setProfession(prof);
        s.setGraduationDate(request.getGraduationDate());
        s.setUniversityName(request.getUniversityName());
        return s;
    }


    public StudyResponse fromDomainToResponse(Study study) {
        if (study == null) return null;


        String personIdent = String.valueOf(study.getPerson().getIdentification());
        
        String professionIdent = String.valueOf(study.getProfession().getIdentification());


        return new StudyResponse(
            personIdent,
            professionIdent,
            study.getGraduationDate(),
            study.getUniversityName()
        );
    }
}
