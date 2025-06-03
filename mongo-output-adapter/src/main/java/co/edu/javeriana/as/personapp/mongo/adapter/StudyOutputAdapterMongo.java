package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;

/**
 * Adaptador de salida (output) para Study usando MongoDB.
 */
@Component
@Transactional
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMongo estudiosRepository;

    @Autowired
    private EstudiosMapperMongo estudiosMapper;

    @Override
    public Study createStudy(Study study) {
        EstudiosDocument doc = estudiosMapper.fromDomainToAdapter(study);
        EstudiosDocument saved = estudiosRepository.save(doc);
        return estudiosMapper.fromAdapterToDomain(saved);
    }

    @Override
    public List<Study> findAllStudies() {
        List<EstudiosDocument> docs = estudiosRepository.findAll();
        return docs.stream()
                   .map(estudiosMapper::fromAdapterToDomain)
                   .collect(Collectors.toList());
    }

    @Override
    public Study findStudyById(Integer personId, Integer professionId) {
        String personaIdStr = String.valueOf(personId);
        String profesionIdStr = String.valueOf(professionId);

        EstudiosDocument doc = estudiosRepository.findByPersona_IdAndProfesion_Id(personaIdStr, profesionIdStr);
        return estudiosMapper.fromAdapterToDomain(doc);
    }

    @Override
    public Study updateStudy(Study study) {
        String personaIdStr = String.valueOf(study.getPerson().getIdentification());
        String profesionIdStr = String.valueOf(study.getProfession().getIdentification());

        EstudiosDocument existing = estudiosRepository
            .findByPersona_IdAndProfesion_Id(personaIdStr, profesionIdStr);

        if (existing == null) {
            return null;
        }

        String existingId = existing.getId();
        EstudiosDocument toSave = estudiosMapper.fromDomainToAdapter(study);
        toSave.setId(existingId);

        EstudiosDocument saved = estudiosRepository.save(toSave);
        return estudiosMapper.fromAdapterToDomain(saved);
    }

    @Override
    public boolean deleteStudy(Integer personId, Integer professionId) {
        String personaIdStr = String.valueOf(personId);
        String profesionIdStr = String.valueOf(professionId);

        EstudiosDocument existing = estudiosRepository
            .findByPersona_IdAndProfesion_Id(personaIdStr, profesionIdStr);

        if (existing == null) {
            return false;
        }

        estudiosRepository.delete(existing);
        return true;
    }
}
