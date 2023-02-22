package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="or_order")
public class Order {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "or_id",nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "or_u_id",nullable = false)
    private User user;

    @NotNull
    @Column(name = "or_status", nullable = false, length = 20)
    private String status;

    @NotNull
    @Column(name = "or_createdAt", nullable = false, length = 20)
    private LocalDateTime created;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @Column(name = "or_pr_id",nullable = false)
    private List<Product> products;

    @ElementCollection
    @Column(name="or_maenge")
    private Map<Product, String> menge;
}
