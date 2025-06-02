package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.PersonaMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PhoneRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private PhoneRepositoryMaria telefonoRepository;
    
    @Autowired
    private TelefonoMapperMaria telefonoMapper;

    @Autowired
    private PersonaMapperMaria personaMapperMaria;

    @Override
    public Phone save(Phone phone) {
        TelefonoEntity entity = telefonoMapper.fromDomainToAdapter(phone);
        TelefonoEntity savedEntity = telefonoRepository.save(entity);
        return telefonoMapper.fromAdapterToDomain(savedEntity);
    }

    @Override
    public Boolean delete(String phoneNumber) {
        telefonoRepository.deleteById(phoneNumber);
        return telefonoRepository.findById(phoneNumber).isEmpty();
    }

    @Override
    public List<Phone> find() {
        return telefonoRepository.findAll().stream()
                .map(telefonoMapper::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String phoneNumber) {
        return telefonoRepository.findById(phoneNumber)
                .map(telefonoMapper::fromAdapterToDomain)
                .orElse(null);
    }

    @Override
    public List<Phone> findByPerson(Person owner) {
        PersonaEntity ownerEntity = personaMapperMaria.fromDomainToAdapter(owner);
        return telefonoRepository.findByDuenio(ownerEntity).stream()
                .map(telefonoMapper::fromAdapterToDomain)
                .collect(Collectors.toList());
    }
}