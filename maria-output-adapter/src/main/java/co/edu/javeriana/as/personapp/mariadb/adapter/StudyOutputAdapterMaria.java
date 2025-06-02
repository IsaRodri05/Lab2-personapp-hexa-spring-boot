package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;

@Component
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMaria estudiosRepository;

    @Autowired
    private EstudiosMapperMaria estudiosMapper;

    @Override
    public Study createStudy(Study study) {
        EstudiosEntity entity = estudiosMapper.fromDomainToAdapter(study);
        EstudiosEntity saved = estudiosRepository.save(entity);
        return estudiosMapper.fromAdapterToDomain(saved);
    }

    @Override
    public List<Study> findAllStudies() {
        List<EstudiosEntity> list = estudiosRepository.findAll();
        return list.stream()
                   .map(estudiosMapper::fromAdapterToDomain)
                   .collect(Collectors.toList());
    }

    @Override
    public Study findStudyById(Integer personId, Integer professionId) {
        EstudiosEntityPK pk = new EstudiosEntityPK();
        pk.setCcPer(personId);
        pk.setIdProf(professionId);
        Optional<EstudiosEntity> opt = estudiosRepository.findById(pk);
        return opt.map(estudiosMapper::fromAdapterToDomain).orElse(null);
    }

    @Override
    public Study updateStudy(Study study) {
        // Asumimos que la entidad existe
        EstudiosEntity entity = estudiosMapper.fromDomainToAdapter(study);
        EstudiosEntity saved = estudiosRepository.save(entity);
        return estudiosMapper.fromAdapterToDomain(saved);
    }

    @Override
    public boolean deleteStudy(Integer personId, Integer professionId) {
        EstudiosEntityPK pk = new EstudiosEntityPK();
        pk.setCcPer(personId);
        pk.setIdProf(professionId);
        if (!estudiosRepository.existsById(pk)) {
            return false;
        }
        estudiosRepository.deleteById(pk);
        return true;
    }
}
