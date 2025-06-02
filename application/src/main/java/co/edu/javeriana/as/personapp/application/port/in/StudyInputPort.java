package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.domain.Study;

public interface StudyInputPort {

    /**
     * @param study
     * @return
     */
    Study createStudy(Study study);

    /**
     * @return
     */
    List<Study> getAllStudies();

    /**
     * @param personId
     * @param professionId
     * @return
     */
    Study getStudyById(Integer personId, Integer professionId);

    /**
     * @param personId
     * @param professionId
     * @param updatedStudy
     * @return
     */
    Study updateStudy(Integer personId, Integer professionId, Study updatedStudy);

    /**
     * @param personId
     * @param professionId
     * @return
     */
    boolean deleteStudy(Integer personId, Integer professionId);
}
