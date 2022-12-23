package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @OneToMany
    @Column(name = "u_role")
    private Set<Role> roles = new HashSet<>();


}

