package com.avparamonov.checkers.model;

import com.avparamonov.checkers.model.db.entity.Player;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Scope("session")
public class Game {

    String id;
    Checker[][] board;
    Player player1;
    Player player2;
    Player currentPlayer;
    List<Move> currentPlayerAvailableMoves;
    GameType type;
    GameStatus status;

}