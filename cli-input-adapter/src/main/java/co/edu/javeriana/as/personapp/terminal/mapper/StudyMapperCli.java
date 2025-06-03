package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;

@Mapper
public class StudyMapperCli {

    public Study fromAdapterCliToDomain(StudyModelCli model) {
        Person person = new Person();
        person.setIdentification(model.getPersonId());

        Profession profession = new Profession();
        profession.setIdentification(model.getProfessionId());

        Study study = new Study();
        study.setPerson(person);
        study.setProfession(profession);
        study.setGraduationDate(model.getGraduationDate());
        study.setUniversityName(model.getUniversityName());
        return study;
    }

    public StudyModelCli fromDomainToAdapterCli(Study study) {
        StudyModelCli model = new StudyModelCli();
        model.setPersonId(study.getPerson().getIdentification());
        model.setProfessionId(study.getProfession().getIdentification());
        model.setGraduationDate(study.getGraduationDate());
        model.setUniversityName(study.getUniversityName());
        return model;
    }
}
