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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "or_u_id",nullable = false)
    private User user;

    @NotNull
    @Column(name = "or_status", nullable = false, length = 20)
    private String status;

//    Wozu ist der Content?
//    @NotNull
//    @Column(name = "or_content", nullable = false, length = 20)
//    private String content;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @Column(name = "or_pr_id",nullable = false)
    private List<Product> products;
}
