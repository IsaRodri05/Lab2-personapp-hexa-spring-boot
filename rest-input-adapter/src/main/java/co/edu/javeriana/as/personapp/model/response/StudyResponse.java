package co.edu.javeriana.as.personapp.model.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class StudyResponse {

    private String personIdentification;    
    private String professionIdentification; 
    private LocalDate graduationDate;
    private String universityName;
    private String message; 

    public StudyResponse(String personIdentification,
                         String professionIdentification,
                         LocalDate graduationDate,
                         String universityName) {
        this.personIdentification = personIdentification;
        this.professionIdentification = professionIdentification;
        this.graduationDate = graduationDate;
        this.universityName = universityName;
        this.message = null;
    }
}
