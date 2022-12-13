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
@Table(name="t_tag")
public class Tag {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id",nullable = false, unique = true, length = 45)
    private Long id;

    @NotNull
    @Column(name = "t_titel",nullable = false, unique = true, length = 45)
    private String titel;

    @NotNull
    @Column(name = "t_content", nullable = false, length = 64)
    private String content;
}
