package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.domain.Study;

public interface StudyOutputPort {

    Study createStudy(Study study);

    List<Study> findAllStudies();

    Study findStudyById(Integer personId, Integer professionId);

    Study updateStudy(Study study);

    boolean deleteStudy(Integer personId, Integer professionId);
}
