package co.edu.javeriana.as.personapp.model.request;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditStudyRequest {
    private LocalDate graduationDate;
    private String universityName;
}
