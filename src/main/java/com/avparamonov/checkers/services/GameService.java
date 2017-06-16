package com.avparamonov.checkers.services;

import com.avparamonov.checkers.exceptions.MoveNotAllowedException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.*;
import com.avparamonov.checkers.model.db.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.*;

import static com.avparamonov.checkers.model.Side.BLACK;
import static com.avparamonov.checkers.model.Side.WHITE;

/**
 * Game service.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Service
public class GameService {

    @Autowired
    private CheckerService checkerService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BoardService boardService;

    private Map<String, Game> currentGames = new HashMap<>();

    public Map<String, Game> getCurrentGames() {
        return currentGames;
    }

    public Game createGame(String nickname1, String nickname2, GameType gameType) throws PlayerNotFoundException {
        String gameId = UUID.randomUUID().toString();
        Player player1 = playerService.findPlayerByNickname(nickname1);
        Player player2 = playerService.findPlayerByNickname(nickname2);
        int boardSize = gameType.getSize();

        if (player1 == null || player2 == null) {
            throw new ValidationException("Unable to find specified player(s).");
        }
        Game game = Game.builder()
                .id(gameId)
                .board(new Checker[boardSize][boardSize])
                .player1(player1)
                .player2(player2)
                .type(gameType)
                .currentPlayer(player1.getSide() == WHITE ? player1 : player2)
                .build();

        initBoard(game);
        currentGames.put(gameId, game);

        return game;
    }

    private void initBoard(Game game) {
        Checker[][] board = game.getBoard();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if ( row % 2 == col % 2 ) {
                    if (row < 3) {
                        board[row][col] = checkerService.createWithSideAndCoordinates(WHITE, row, col);
                    }
                    else if (row > board.length - 4) {
                        board[row][col] = checkerService.createWithSideAndCoordinates(BLACK, row, col);
                    }
                    else {
                        board[row][col] = null;
                    }
                }
                else {
                    board[row][col] = null;
                }
            }
        }
    }

    public GameStatus makeMove(Player player, Move move, Game game) throws MoveNotAllowedException {
        List<Move> moves = boardService.getAvailableMoves(player, game.getBoard());

        if (moves.isEmpty()) {
            // current player lost
            return GameStatus.FINISHED;
        }

        Move exactMove = moves.stream()
                .filter(m -> m.equals(move))
                .findFirst().orElseThrow(() -> new MoveNotAllowedException("Move " + move + " not allowed."));

        boardService.makeMove(game.getBoard(), exactMove);

        game.setCurrentPlayer(game.getPlayer1().equals(player) ? game.getPlayer2() : game.getPlayer1());
        return GameStatus.IN_PROGRESS;
    }

}
