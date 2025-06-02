package co.edu.javeriana.as.personapp.mariadb.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;

@Mapper
public class TelefonoMapperMaria {

    public TelefonoEntity fromDomainToAdapter(Phone phone) {
        if (phone == null) return null;

        TelefonoEntity telefonoEntity = new TelefonoEntity();
        telefonoEntity.setNum(phone.getNumber());
        telefonoEntity.setOper(phone.getCompany());

        // Setear solo referencia mínima al dueño para evitar ciclo
        if (phone.getOwner() != null) {
            PersonaEntity duenio = new PersonaEntity();
            duenio.setCc(phone.getOwner().getIdentification());
            telefonoEntity.setDuenio(duenio);
        } else {
            telefonoEntity.setDuenio(null);
        }

        return telefonoEntity;
    }

    public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
        if (telefonoEntity == null) return null;

        Phone phone = new Phone();
        phone.setNumber(telefonoEntity.getNum());
        phone.setCompany(telefonoEntity.getOper());

        // Setear solo referencia mínima al owner para evitar ciclo
        if (telefonoEntity.getDuenio() != null) {
            Person owner = new Person();
            owner.setIdentification(telefonoEntity.getDuenio().getCc());
            phone.setOwner(owner);
        } else {
            phone.setOwner(null);
        }

        return phone;
    }
}
