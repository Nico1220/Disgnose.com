package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ca_category")
public class Category {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_id",nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @Column(name = "ca_parentId", nullable = false, unique = true, length = 45)
    private Long parent;

    @NotNull
    @Column(name = "ca_titel",nullable = false, length = 64)
    private String titel;

    @NotNull
    @Column(name = "ca_content", nullable = false, length = 20)
    private String content;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "ca_pr_id",nullable = false)
    private List<Product> products;

}
