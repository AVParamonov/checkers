package com.avparamonov.checkers.db.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

/**
 * Game entity.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "game_id")
    long id;

    @OneToOne
    @JoinColumn(name = "game_id")
    Board board;

    @ManyToOne
    Player player1;

    @ManyToOne
    Player player2;

    @Override
    public String toString() {
        return "com.avparamonov.checkers.db.entity.Game(id=" + this.getId()
                + ", board=" + this.getBoard().getId()
                + ", player1=" + this.getPlayer1().getId()
                + ", player2=" + this.getPlayer2().getId() + ")";
    }

}