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
public class Diagnose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_id", nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @Column(name = "pr_titel", nullable = false, length = 64)
    private String titel;

    @NotNull
    @Column(name = "pr_diagnose", nullable = false, length = 64)
    private String diagnoses;

    @NotNull
    @Column(name = "pr_createdAt", length = 20)
    private LocalDateTime created;

    @NotNull
    @Column(name = "pr_updatedAt",  length = 20)
    private LocalDateTime updated;

}
