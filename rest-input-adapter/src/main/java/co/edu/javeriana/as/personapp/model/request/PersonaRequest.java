package co.edu.javeriana.as.personapp.model.request;

import co.edu.javeriana.as.personapp.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaRequest {
	private String dni;
	private String firstName;
	private String lastName;
	private String age;
	private String sex;
	private String database;
}
