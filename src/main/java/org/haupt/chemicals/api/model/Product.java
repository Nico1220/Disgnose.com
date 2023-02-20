package org.haupt.chemicals.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pr_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_id", nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @Column(name = "pr_titel", nullable = false, length = 64)
    private String titel;

    @Column(name = "pr_quantity", nullable = true, length = 64)
    private int quantity;

    @NotNull
    @Column(name = "pr_createdAt", nullable = false, length = 20)
    private LocalDateTime created;

    @NotNull
    @Column(name = "pr_updatedAt", nullable = false, length = 20)
    private LocalDateTime updated;

    @NotNull
    @Column(name = "pr_content",length = 64)
    private String content;

    @NotNull
    @Column(name = "pr_maenge",length = 64)
    private String maenge;
}
