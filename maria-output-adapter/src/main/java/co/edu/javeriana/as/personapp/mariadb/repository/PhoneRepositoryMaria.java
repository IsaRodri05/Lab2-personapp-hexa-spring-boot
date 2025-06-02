package co.edu.javeriana.as.personapp.mariadb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

public interface PhoneRepositoryMaria extends JpaRepository<TelefonoEntity, String> {
    List<TelefonoEntity> findByDuenio(PersonaEntity duenio);
}
