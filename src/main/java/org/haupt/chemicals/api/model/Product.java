package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pr_product")
public class Product {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_id", nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_u_id",nullable = false)
    private User user;

    @NotNull
    @Column(name = "pr_titel", nullable = false, length = 45)
    private String titel;

    @NotNull
    @Column(name = "pr_quantity", nullable = false, length = 64)
    private int quantity;

    @NotNull
    @Column(name = "pr_createdAt", nullable = false, length = 20)
    private Date created;

    @NotNull
    @Column(name = "pr_updatedAt", nullable = false, length = 20)
    private Date updated;

//    @NotNull
//    @Column(name = "first_name", nullable = false, length = 20)
//    private Date published;

    @NotNull
    @Column(name = "pr_content", nullable = false, length = 20)
    private String content;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_ca_id",nullable = false)
    private List<Category> categories;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_t_id",nullable = false)
    private Tag tag;
}
