package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Mapper
public class ProfesionMapperRest {

    public ProfesionResponse fromDomainToAdapterRestMaria(Profession profession) {
        return fromDomainToAdapterRest(profession, "MariaDB");
    }

    public ProfesionResponse fromDomainToAdapterRestMongo(Profession profession) {
        return fromDomainToAdapterRest(profession, "MongoDB");
    }

    public ProfesionResponse fromDomainToAdapterRest(Profession profession, String database) {
        return new ProfesionResponse(
            profession.getIdentification() + "",
            profession.getName(),
            profession.getDescription(),
            database,
            "OK");
    }

    public Profession fromAdapterToDomain(ProfesionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Integer identification;

        try {
            identification = Integer.parseInt(request.getIdentificacion());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Identification must be a valid integer", e);
        }

        String name = request.getNombre();
        if (name == null) {
            throw new IllegalArgumentException("Name is required");
        }

        String description = request.getDescripcion();
        if (description == null) {
            throw new IllegalArgumentException("Description is required");
        }

        Profession profession = new Profession(identification, name);
        profession.setDescription(description);
        return profession;
    }

}
