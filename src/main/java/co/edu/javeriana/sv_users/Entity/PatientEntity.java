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
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String identificationNumber;
    private String identificationType;
    private String address;
    private String phone;
    private String nombreFamiliar;
    private String parentescoFamiliar;
    private String telefonoFamiliar;
    private String barrio;
    private String conjunto;
    private String latitud;
    private String longitud;
    
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParentescoFamiliar() {
        return parentescoFamiliar;
    }

    public void setParentescoFamiliar(String parentescoFamiliar) {
        this.parentescoFamiliar = parentescoFamiliar;
    }

    public String getTelefonoFamiliar() {
        return telefonoFamiliar;
    }

    public void setTelefonoFamiliar(String telefonoFamiliar) {
        this.telefonoFamiliar = telefonoFamiliar;
    }

    public String getConjunto() {
        return conjunto;
    }

    public void setConjunto(String conjunto) {
        this.conjunto = conjunto;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

}
