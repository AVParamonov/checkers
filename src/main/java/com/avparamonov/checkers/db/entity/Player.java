package com.avparamonov.checkers.db.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

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
    long id;

    String nickname;

    int age;

    @Enumerated(EnumType.STRING)
    PlayerType type = PlayerType.NOT_KNOWN;

    @Enumerated
    Side side;

    @Column(name = "is_active")
    boolean isActive;

    @OneToMany(fetch = FetchType.EAGER)
    List<Checker> checkers;

    @ManyToOne(fetch = FetchType.EAGER)
    Game currentGame;

    @ManyToMany(fetch = FetchType.LAZY)
    List<GameResult> gameResults;

    @Override
    public String toString() {
        return "com.avparamonov.checkers.db.entity.Player(id=" + this.getId()
                + ", nickname=" + this.getNickname()
                + ", age=" + this.getAge()
                + ", type=" + this.getType()
                + ", side=" + this.getSide()
                + ", isActive=" + this.isActive()
                + ", checkers=" + this.getCheckers()
                + ", currentGame=" + this.getCurrentGame().getId()
                + ", gameResults=" + this.getGameResults() + ")";
    }

}
