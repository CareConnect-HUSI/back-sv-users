package co.edu.javeriana.sv_users.Entity;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String barrio;
    private String nombreFamiliar;
    private String parentesco;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Treatment> tratamientos;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Procedure> procedimientos;

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getNombreFamiliar() {
        return nombreFamiliar;
    }

    public void setNombreFamiliar(String nombreFamiliar) {
        this.nombreFamiliar = nombreFamiliar;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public List<Treatment> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<Treatment> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public List<Procedure> getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(List<Procedure> procedimientos) {
        this.procedimientos = procedimientos;
    }

    public String getIdentificationNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
