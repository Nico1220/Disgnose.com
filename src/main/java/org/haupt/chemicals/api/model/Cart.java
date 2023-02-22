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
@Table(name="ca_cart")
public class Cart {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_id",nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ca_u_id",nullable = false, unique = true)
    private User user;

    @NotNull
    @Column(name = "ca_createdAt", nullable = false, length = 20)
    private LocalDateTime created;

    @NotNull
    @Column(name = "ca_updatedAt", nullable = false, length = 20)
    private LocalDateTime updated;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @Column(name = "ca_products", nullable = false, unique = true)
    private List<Product> products;

    @ElementCollection
    @MapKeyJoinColumn(name="pr_id")
    @Column(name="ca_maenge")
    @CollectionTable(name="product_maengen", joinColumns=@JoinColumn(name="ca_products"))
    private Map<Product, String> mengen;

}
