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
@Table(name="or_order")
public class Order {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "or_id",nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "or_u_id",nullable = false, unique = true)
    private User user;

    @NotNull
    @Column(name = "or_status", nullable = false, length = 20)
    private String status;

    @NotNull
    @Column(name = "or_content", nullable = false, length = 20)
    private String content;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "or_u_id",nullable = false, unique = true)
    private List<Product> products;
}
