package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;

@Mapper
public class PhoneMapperCli {

    public PhoneModelCli fromDomainToAdapterCli(Phone phone) {
        PhoneModelCli phoneModelCli = new PhoneModelCli();
        phoneModelCli.setNumero(phone.getNumber());
        phoneModelCli.setCompañia(phone.getCompany());
        phoneModelCli.setDuenioCc(phone.getOwner() != null ? phone.getOwner().getIdentification() : null);
        return phoneModelCli;
    }

    public Phone fromAdapterCliToDomain(PhoneModelCli phoneModelCli) {
        Phone phone = new Phone();
        phone.setNumber(phoneModelCli.getNumero());
        phone.setCompany(phoneModelCli.getCompañia());
        
        if (phoneModelCli.getDuenioCc() != null) {
            Person owner = new Person();
            owner.setIdentification(phoneModelCli.getDuenioCc());
            phone.setOwner(owner);
        }
        
        return phone;
    }
}