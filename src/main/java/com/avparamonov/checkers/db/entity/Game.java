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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    Board board;

    @OneToMany(fetch = FetchType.EAGER)
    List<Player> players;

}