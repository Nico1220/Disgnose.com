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
public class Mail {
    @NotNull
    private String name;

    @NotNull
    private String message;

    @NotNull
    private String subject;

    @Id
    @NotNull
    private String email;
}
