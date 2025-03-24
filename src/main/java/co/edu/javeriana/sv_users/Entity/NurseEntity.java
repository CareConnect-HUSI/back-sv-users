package co.edu.javeriana.sv_users.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Nurse")
public class NurseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String identificationNumber;
    private String identificationType;
    private String address;
    private String phone;
    private String localidad;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)  // Clave foránea
    private Role role;

    public NurseEntity() {}

    public NurseEntity(Long id, String name, String lastname, String identificationNumber, String identificationType, 
                String address, String phone, String localidad, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.identificationNumber = identificationNumber;
        this.identificationType = identificationType;
        this.address = address;
        this.phone = phone;
        this.localidad = localidad;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // GETTERS Y SETTERS CORRECTOS

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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRoleName() {
        return role.getName();
    }

    public void setRoleName(String roleName) {
        this.role.setName(roleName);
    }
}
