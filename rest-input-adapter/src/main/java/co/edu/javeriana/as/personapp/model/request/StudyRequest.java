package co.edu.javeriana.as.personapp.model.request;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyRequest {
    private Integer personId;
    private Integer professionId;
    private LocalDate graduationDate;
    private String universityName;
}
