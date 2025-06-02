package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.PhoneRequest;

public class PhoneResponse extends PhoneRequest {
    private String status;
    
    public PhoneResponse(String number, String company, String ownerDni, String database, String status) {
        super(number, company, ownerDni, database);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}