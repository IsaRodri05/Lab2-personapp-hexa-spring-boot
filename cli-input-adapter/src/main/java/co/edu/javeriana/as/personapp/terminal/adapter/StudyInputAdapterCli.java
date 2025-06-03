package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.StudyMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
@Component
@RequiredArgsConstructor
public class StudyInputAdapterCli {

    private final StudyInputPort studyInputPort;
    private final StudyMapperCli studyMapperCli;

    /**
     * @param studyModelCli
     * @return
     */
    public StudyModelCli createStudy(StudyModelCli studyModelCli) {
        Study domainToSave = studyMapperCli.fromAdapterCliToDomain(studyModelCli);
        Study created = studyInputPort.createStudy(domainToSave);
        return studyMapperCli.fromDomainToAdapterCli(created);
    }

    /**
     * @return
     */
    public List<StudyModelCli> getAllStudies() {
        return studyInputPort.getAllStudies()
                .stream()
                .map(studyMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
    }

    /**
     * @throws NoExistException
     */
    public StudyModelCli getStudyById(Integer personId, Integer professionId) throws NoExistException {
        Study found = studyInputPort.getStudyById(personId, professionId);
        return studyMapperCli.fromDomainToAdapterCli(found);
    }

    /**
     * @throws NoExistException
     */
    public StudyModelCli updateStudy(StudyModelCli studyModelCli) throws NoExistException {
        Study toUpdate = studyMapperCli.fromAdapterCliToDomain(studyModelCli);
        Study updated = studyInputPort.updateStudy(
                studyModelCli.getPersonId(),
                studyModelCli.getProfessionId(),
                toUpdate
        );
        return studyMapperCli.fromDomainToAdapterCli(updated);
    }

    /**
     * @return
     * @throws NoExistException
     */
    public boolean deleteStudy(Integer personId, Integer professionId) throws NoExistException {
        return studyInputPort.deleteStudy(personId, professionId);
    }
}
