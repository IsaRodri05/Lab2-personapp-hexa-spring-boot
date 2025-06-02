package co.edu.javeriana.as.personapp.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Person {
	@NonNull
	private Integer identification;
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@NonNull
	private String gender;
	private Integer age;
	@ToString.Exclude
	private List<Phone> phoneNumbers;
	@ToString.Exclude
	private List<Study> studies;

	public Person(Integer identification, String firstName, String lastName, 
			String gender, Integer age) {
		this.identification = identification;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
	}

	public Boolean isValidAge() {
		return this.age >= 0;
	}
}
