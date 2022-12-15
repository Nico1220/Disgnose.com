package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Column(name = "pr_titel", nullable = false)
    private String titel;

    @Column(name = "pr_quantity", nullable = true, length = 64)
    private int quantity;

    @NotNull
    @Column(name = "pr_createdAt", nullable = false, length = 20)
    private LocalDateTime created;

    @NotNull
    @Column(name = "pr_updatedAt", nullable = false, length = 20)
    private LocalDateTime updated;

//    @NotNull
//    @Column(name = "first_name", nullable = false, length = 20)
//    private Date published;

    @NotNull
    @Column(name = "pr_content", nullable = false, length = 20)
    private String content;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_ca_id",nullable = true)
    private List<Category> categories;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_t_id",nullable = true)
    private Tag tag;
}
