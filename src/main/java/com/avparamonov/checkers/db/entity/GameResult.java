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
@Table(name = "game_results")
public class GameResult {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "game_result_id")
    long id;

    @OneToOne
    Game game;

    @ManyToOne
    Player winner;

}