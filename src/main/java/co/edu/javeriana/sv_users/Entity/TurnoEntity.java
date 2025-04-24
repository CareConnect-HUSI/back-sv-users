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
@Table(name = "turno")
public class TurnoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "turnoEntity", cascade = CascadeType.ALL)
    private List<EnfermeraEntity> enfermeraEntities = new ArrayList<>();

    public TurnoEntity() {
    }

    public TurnoEntity(Long id, String name) {
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

    public List<EnfermeraEntity> getEnfermeraEntities() {
        return enfermeraEntities;
    }

    public void setEnfermeraEntities(List<EnfermeraEntity> enfermeraEntities) {
        this.enfermeraEntities = enfermeraEntities;
    }
}
