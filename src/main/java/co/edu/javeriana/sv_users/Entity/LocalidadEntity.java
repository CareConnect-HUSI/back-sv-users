package co.edu.javeriana.sv_users.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "localidad")
public class LocalidadEntity {
    @Id
    private String codigo;
    private String nombre; 

    @JsonIgnore
    @OneToMany(mappedBy = "localidad", fetch = FetchType.LAZY)
    private List<BarrioEntity> barrios = new ArrayList<>();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<BarrioEntity> getBarrios() {
        return barrios;
    }

    public void setBarrios(List<BarrioEntity> barrios) {
        this.barrios = barrios;
    }
}