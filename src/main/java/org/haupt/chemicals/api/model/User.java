package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.management.relation.Role;
import javax.persistence.*;


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="u_users")
public class User {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id",nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @Column(name = "u_email", nullable = false, unique = true, length = 45)
    private String email;

    @NotNull
    @Column(name = "u_password",nullable = false, length = 64)
    private String password;

    @NotNull
    @Column(name = "u_firstName", nullable = false, length = 20)
    private String firstName;

    @NotNull
    @Column(name = "u_lastName", nullable = false, length = 20)
    private String lastName;


    @Column(name = "u_role", nullable = true, length = 20)
    private String role;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getRole(){return role;}
//
//    public void setRole(String role){this.role = role;}

}

