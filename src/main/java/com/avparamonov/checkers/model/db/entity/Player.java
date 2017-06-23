package com.avparamonov.checkers.model.db.entity;

import com.avparamonov.checkers.model.PlayerType;
import com.avparamonov.checkers.model.Side;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * Player entity.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    String nickname;

    @Enumerated(EnumType.STRING)
    PlayerType type;

    @Enumerated
    Side side;

}
