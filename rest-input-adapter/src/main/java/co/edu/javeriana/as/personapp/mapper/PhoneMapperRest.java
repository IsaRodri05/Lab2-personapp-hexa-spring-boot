package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;

@Mapper
public class PhoneMapperRest {

    public PhoneResponse fromDomainToAdapterRestMaria(Phone phone) {
        return fromDomainToAdapterRest(phone, "MariaDB");
    }

    public PhoneResponse fromDomainToAdapterRestMongo(Phone phone) {
        return fromDomainToAdapterRest(phone, "MongoDB");
    }

    private PhoneResponse fromDomainToAdapterRest(Phone phone, String database) {
        return new PhoneResponse(
                phone.getNumber(),
                phone.getCompany(),
                phone.getOwner() != null ? phone.getOwner().getIdentification().toString() : null,
                database,
                "OK");
    }

    public Phone fromAdapterToDomain(PhoneRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.getNumber() == null || request.getCompany() == null) {
            throw new IllegalArgumentException("Phone number and company are required");
        }

        Phone phone = new Phone();
        phone.setNumber(request.getNumber());
        phone.setCompany(request.getCompany());
        
        // El owner se establecerá más tarde en el adapter
        return phone;
    }
}