package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudyUseCase implements StudyInputPort {

    private final StudyOutputPort studyOutputPort;

    @Override
    public Study createStudy(Study study) {
        return studyOutputPort.createStudy(study);
    }

    @Override
    public List<Study> getAllStudies() {
        return studyOutputPort.findAllStudies();
    }

    @Override
    public Study getStudyById(Integer personId, Integer professionId) {
        return studyOutputPort.findStudyById(personId, professionId);
    }

    @Override
    public Study updateStudy(Integer personId, Integer professionId, Study updatedStudy) {
        Study existing = studyOutputPort.findStudyById(personId, professionId);
        if (existing == null) {
            return null;
        }
        existing.setGraduationDate(updatedStudy.getGraduationDate());
        existing.setUniversityName(updatedStudy.getUniversityName());

        return studyOutputPort.updateStudy(existing);
    }

    @Override
    public boolean deleteStudy(Integer personId, Integer professionId) {
        return studyOutputPort.deleteStudy(personId, professionId);
    }
}
