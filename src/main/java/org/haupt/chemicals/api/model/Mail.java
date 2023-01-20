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
@Table(name="m_mail")
public class Mail {

    @Id
    @NotNull
    @Column(name = "m_name",nullable = false)
    private String name;

    @NotNull
    @Column(name = "m_subject",nullable = false)
    private String subject;

    @NotNull
    @Column(name = "m_body",nullable = false)
    private String body;

}
