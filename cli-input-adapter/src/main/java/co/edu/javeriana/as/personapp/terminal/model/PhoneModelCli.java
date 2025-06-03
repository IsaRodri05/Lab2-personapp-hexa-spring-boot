package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneModelCli {
    private String numero;
    private String compañia;
    private Integer duenioCc;
    
    @Override
    public String toString() {
        return "Teléfono [Número: " + numero + 
               ", Compañía: " + compañia + 
               ", Dueño CC: " + duenioCc + "]";
    }
}