package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="r_role")
public class Role {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id",nullable = false, unique = true, length = 45)
    private Long id;

    @Column(name = "r_name",nullable = false, unique = true, length = 45)
    private String name;
}
