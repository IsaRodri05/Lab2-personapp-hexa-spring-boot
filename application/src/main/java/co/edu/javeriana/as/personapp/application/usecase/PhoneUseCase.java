package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phonePersistence;
    private PersonOutputPort personPersistence;

    public PhoneUseCase(
            @Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phoneOutputPort,
            @Qualifier("personOutputAdapterMaria") PersonOutputPort personOutputPort) {
        this.phonePersistence = phoneOutputPort;
        this.personPersistence = personOutputPort;
    }

    @Override
    public void setPersintence(
            @Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersistence,
            @Qualifier("personOutputAdapterMaria") PersonOutputPort personPersistence) {
        this.phonePersistence = phonePersistence;
        this.personPersistence = personPersistence;
    }

    @Override
    public Phone create(Phone phone) throws NoExistException {
        log.debug("Into create on Application Domain");
        if(personPersistence.findById(phone.getOwner().getIdentification()) != null) {
            return phonePersistence.save(phone);
        }
        log.error("The owner of the phone does not exist in the database");
        throw new NoExistException("The owner of the phone does not exist in the database");
    }

    @Override
    public Phone edit(String number, Phone phone) throws NoExistException {
        Phone oldPhone = phonePersistence.findById(number);
        if (oldPhone != null) {
            return phonePersistence.save(phone);
        }
        throw new NoExistException("The phone with number " + number + " does not exist into db, cannot be edited");
    }

    @Override
    public Boolean drop(String number) throws NoExistException {
        Phone oldPhone = phonePersistence.findById(number);
        if (oldPhone != null) {
            return phonePersistence.delete(number);
        }
        throw new NoExistException("The phone with number " + number + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Phone> findAll() {
        log.info("Output: " + phonePersistence.getClass());
        return phonePersistence.find();
    }

    @Override
    public Phone findOne(String number) throws NoExistException {
        Phone phone = phonePersistence.findById(number);
        if (phone != null) {
            return phone;
        }
        throw new NoExistException("The phone with number " + number + " does not exist into db");
    }

    @Override
    public Integer count() {
        return phonePersistence.find().size();
    }


    @Override
    public List<Phone> getPersonPhones(Integer personId) throws NoExistException {
        Person person = personPersistence.findById(personId);
        if (person != null) {
            return phonePersistence.findByPerson(person);
        }
        throw new NoExistException("The person with id " + personId + " does not exist into db, cannot get phones");
    }
}
