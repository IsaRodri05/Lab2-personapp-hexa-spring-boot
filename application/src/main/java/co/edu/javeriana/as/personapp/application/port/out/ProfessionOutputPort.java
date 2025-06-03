package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface ProfessionOutputPort {
        public Profession save(Profession profession);
        public Boolean delete(Integer identification);
        public List<Profession> find();
        public Profession findById(Integer identification);
        public Integer count();
        //public List<Study> getStudies(Integer identification) throws NoExistException;
}
