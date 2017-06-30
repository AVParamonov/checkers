package com.avparamonov.checkers.services;

import com.avparamonov.checkers.exceptions.GameNotFoundException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.Checker;
import com.avparamonov.checkers.model.Game;
import com.avparamonov.checkers.model.GameType;
import com.avparamonov.checkers.model.db.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.avparamonov.checkers.model.GameStatus.*;
import static com.avparamonov.checkers.model.Side.WHITE;

/**
 * Game service.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Service
public class GameService {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BoardService boardService;

    private Map<String, Game> currentGames = new HashMap<>();

    public Game createGame(String nickname1, String nickname2, GameType gameType) throws PlayerNotFoundException {
        String gameId = UUID.randomUUID().toString();
        Player player1 = playerService.findPlayerByNickname(nickname1);
        Player player2 = playerService.findPlayerByNickname(nickname2);
        int boardSize = gameType.getSize();

        Game game = Game.builder()
                .id(gameId)
                .board(new Checker[boardSize][boardSize])
                .player1(player1)
                .player2(player2)
                .type(gameType)
                .status(READY)
                .currentPlayer(player1.getSide() == WHITE ? player1 : player2)
                .build();

        boardService.init(game.getBoard());
        currentGames.put(gameId, game);

        return game;
    }

    public List<Game> findUnfinished() {
        return new ArrayList<>(getCurrentGames().values());
    }

    public Game findById(String gameId) throws GameNotFoundException {
        Game game = getCurrentGames().get(gameId);
        if (game == null) throw new GameNotFoundException("Game not found with id='" + gameId + "'");
        return game;
    }

    public Game finish(String gameId) {
        Game game = getCurrentGames().get(gameId);
        game.setStatus(FINISHED);
        getCurrentGames().remove(gameId);
        return game;
    }

    public Map<String, Game> getCurrentGames() {
        return currentGames;
    }
}
