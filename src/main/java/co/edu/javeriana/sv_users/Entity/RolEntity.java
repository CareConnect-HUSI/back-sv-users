package co.edu.javeriana.sv_users.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "rol")    
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "rolEntity", cascade = CascadeType.ALL)
    private List<EnfermeraEntity> enfermeras = new ArrayList<>();

    public RolEntity() {
    }

    public RolEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EnfermeraEntity> getEnfermeras() {
        return enfermeras;
    }

    public void setEnfermeras(List<EnfermeraEntity> enfermeras) {
        this.enfermeras = enfermeras;
    }

    public void addEnfermera(EnfermeraEntity enfermera) {
        this.enfermeras.add(enfermera);
        enfermera.setRolEntity(this);
    }

    public void removeEnfermera(EnfermeraEntity enfermera) {
        this.enfermeras.remove(enfermera);
        enfermera.setRolEntity(null);
    }
}


