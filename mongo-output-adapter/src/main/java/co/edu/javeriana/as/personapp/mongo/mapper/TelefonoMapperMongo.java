package co.edu.javeriana.as.personapp.mongo.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;

@Mapper
public class TelefonoMapperMongo {


    public TelefonoDocument fromDomainToAdapter(Phone phone) {
        if (phone == null)
            return null;

        TelefonoDocument telefonoDocument = new TelefonoDocument();
        telefonoDocument.setId(phone.getNumber());
        telefonoDocument.setOper(phone.getCompany());

        if (phone.getOwner() != null) {
            PersonaDocument duenioDocument = new PersonaDocument();
            duenioDocument.setId(phone.getOwner().getIdentification());
            telefonoDocument.setPrimaryDuenio(duenioDocument);
        }

        return telefonoDocument;
    }

    public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
        Phone phone = new Phone();
        phone.setNumber(telefonoDocument.getId());
        phone.setCompany(telefonoDocument.getOper());

        if (telefonoDocument.getPrimaryDuenio() != null) {
            Person owner = new Person();
            owner.setIdentification(telefonoDocument.getPrimaryDuenio().getId());
            phone.setOwner(owner);
        } else {
            phone.setOwner(null);
        }

        return phone;
    }

}