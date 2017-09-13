package com.avparamonov.checkers.model.db.entity;

import com.avparamonov.checkers.model.PlayerType;
import com.avparamonov.checkers.model.Side;
import com.avparamonov.checkers.model.db.Role;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * Player entity.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "player_id")
    int id;

    String firstName;
    String lastName;

    @Column(unique = true)
    @NotBlank(message = "Please provide your nickname")
    String nickname;

    @Length(min = 8, message = "*Your password must have at least 8 characters")
    @NotBlank(message = "Please provide your password")
    private String password;

    @Enumerated(EnumType.STRING)
    PlayerType type;

    @Enumerated
    Side side;

    @Enumerated(EnumType.STRING)
    Role role;

    boolean isActive;

}
