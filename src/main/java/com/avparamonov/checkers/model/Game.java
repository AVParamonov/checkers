package com.avparamonov.checkers.model;

import com.avparamonov.checkers.model.db.entity.Player;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class Game {

    String id;
    Checker[][] board;
    Player player1;
    Player player2;
    Player currentPlayer;
    GameStatus status;

}