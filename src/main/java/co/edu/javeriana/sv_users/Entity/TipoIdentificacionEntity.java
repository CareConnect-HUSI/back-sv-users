package co.edu.javeriana.sv_users.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_identificacion")
public class TipoIdentificacionEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoIdentificacion")
    private List<EnfermeraEntity> enfermeras = new ArrayList<>();


    public TipoIdentificacionEntity() {
    }

    public TipoIdentificacionEntity(Long id, String name) {
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
}
