package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.PersonaMapperMongo;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMongo telefonoRepositoryMongo;

    @Autowired
    private TelefonoMapperMongo telefonoMapperMongo;

    @Autowired
    private PersonaMapperMongo personaMapperMongo;

    @Override
    public Phone save(Phone phone) {
        log.debug("Into save on Phone Adapter MongoDB");
        try {
            TelefonoDocument document = telefonoMapperMongo.fromDomainToAdapter(phone);
            TelefonoDocument savedDoc = telefonoRepositoryMongo.save(document);
            return telefonoMapperMongo.fromAdapterToDomain(savedDoc);
        } catch (MongoWriteException e) {
            log.warn("Error saving phone: {}", e.getMessage());
            return phone;
        }
    }

    @Override
    public Boolean delete(String phoneNumber) {
        log.debug("Into delete on Phone Adapter MongoDB");
        telefonoRepositoryMongo.deleteById(phoneNumber);
        return !telefonoRepositoryMongo.existsById(phoneNumber);
    }

    @Override
    public List<Phone> find() {
        log.debug("Into find on Phone Adapter MongoDB");
        return telefonoRepositoryMongo.findAll().stream()
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String phoneNumber) {
        log.debug("Into findById on Phone Adapter MongoDB");
        return telefonoRepositoryMongo.findById(phoneNumber)
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }

    @Override
    public List<Phone> findByPerson(Person owner) {
        log.debug("Into findByPerson on Phone Adapter MongoDB");
        // Crear documento de persona con solo el ID
        PersonaDocument ownerDoc = new PersonaDocument();
        ownerDoc.setId(owner.getIdentification());

        List<TelefonoDocument> telefonosDocs = telefonoRepositoryMongo.findByPrimaryDuenio(ownerDoc);
        return telefonosDocs.stream()
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }
}