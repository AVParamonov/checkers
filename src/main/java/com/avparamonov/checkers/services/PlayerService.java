package com.avparamonov.checkers.services;

import com.avparamonov.checkers.exceptions.CheckerNotFoundException;
import com.avparamonov.checkers.exceptions.MoveNotAllowedException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.*;
import com.avparamonov.checkers.model.db.dao.PlayerRepository;
import com.avparamonov.checkers.model.db.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.avparamonov.checkers.model.GameStatus.*;

/**
 * Player service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class PlayerService {

    @Autowired
    private BoardService boardService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepository playerRepository;


    public Player create(String nickname, PlayerType playerType, Side side) {
        return playerRepository.save(Player.builder()
                .nickname(nickname)
                .type(playerType)
                .side(side)
                .build());
    }

    public Player findPlayerByNickname(String nickname) throws PlayerNotFoundException {
        return playerRepository.findByNickname(nickname).orElseThrow(() ->
                new PlayerNotFoundException("Player not found with nickname='" + nickname +  "'"));
    }

    public Player findPlayerById(int id) throws PlayerNotFoundException {
        Player player = playerRepository.findOne(id);
        if (player == null) {
            throw new PlayerNotFoundException("Player not found with id='" + id +  "'");
        }
        return player;
    }

    public List<Move> getAvailableMoves(Player player, Game game) {
        List<Move> jumps = new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        Checker[][] board = game.getBoard();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                Checker checker = board[row][col];
                if (checker == null || checker.getSide() != player.getSide()) {
                    continue;
                }
                for (Directions direction: Directions.values()) {
                    jumps.addAll(boardService.getJumps(board, row, col, checker.getType(), direction));
                    moves.addAll(boardService.getMoves(board, row, col, direction));
                }
            }
        }
        if (jumps.isEmpty()) {
            return moves;
        }
        return jumps;
    }

    public Game makeMove(Player player, Move move, Game game) throws MoveNotAllowedException, CheckerNotFoundException {
        Player opponent = game.getPlayer1().equals(player) ? game.getPlayer2() : game.getPlayer1();
        List<Move> moves = getAvailableMoves(player, game);

        Move exactMove = moves.stream()
                .filter(m -> m.equals(move))
                .findFirst().orElseThrow(() -> new MoveNotAllowedException(move + " not allowed."));

        boardService.apply(game.getBoard(), exactMove);
        game.setStatus(IN_PROGRESS);

        List<Move> opponentMoves = getAvailableMoves(opponent, game);
        game.setCurrentPlayerAvailableMoves(opponentMoves);

        if (opponentMoves.isEmpty()) {
            // Game finishes, opponent lost
            game = gameService.finish(game.getId());
        }

        game.setCurrentPlayer(opponent);
        return game;
    }
}
