package com.avparamonov.checkers.services;

import com.avparamonov.checkers.dto.GameRequest;
import com.avparamonov.checkers.exceptions.GameNotFoundException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.Checker;
import com.avparamonov.checkers.model.Game;
import com.avparamonov.checkers.model.GameType;
import com.avparamonov.checkers.model.Side;
import com.avparamonov.checkers.model.db.entity.Player;
import com.avparamonov.checkers.model.db.repository.PlayerRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.avparamonov.checkers.model.GameStatus.FINISHED;
import static com.avparamonov.checkers.model.GameStatus.READY;
import static java.util.Optional.ofNullable;

/**
 * Game service.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Service
public class GameService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private BoardService boardService;

    private Map<String, Game> currentGames = new HashMap<>();


    public Game createGame(GameRequest gameRequest) throws PlayerNotFoundException {
        String gameId = UUID.randomUUID().toString();

        val gameType = GameType.valueOf(gameRequest.getGameType().toUpperCase());

        Player whiteCheckersPlayer = ofNullable(playerRepository.findByNickname(gameRequest.getWhiteSideNickname()))
                .orElseThrow(() -> new PlayerNotFoundException("Player with nickname " + gameRequest.getWhiteSideNickname() + " not found"));
        whiteCheckersPlayer.setSide(Side.WHITE);

        Player blackCheckersPlayer = ofNullable(playerRepository.findByNickname(gameRequest.getBlackSideNickname()))
                .orElseThrow(() -> new PlayerNotFoundException("Player with nickname " + gameRequest.getBlackSideNickname() + " not found"));
        blackCheckersPlayer.setSide(Side.BLACK);

        playerRepository.save(whiteCheckersPlayer);
        playerRepository.save(blackCheckersPlayer);

        int boardSize = gameType.getSize();

        Game game = new Game()
                .setId(gameId)
                .setBoard(new Checker[boardSize][boardSize])
                .setWhiteSidePlayer(whiteCheckersPlayer)
                .setBlackSidePlayer(blackCheckersPlayer)
                .setType(gameType)
                .setStatus(READY)
                .setCurrentPlayer(whiteCheckersPlayer);

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

    public Game findByPlayer(String nickname) throws GameNotFoundException {
        return getCurrentGames().values().stream()
                .filter(v -> v.getWhiteSidePlayer().getNickname().equals(nickname) ||
                        v.getBlackSidePlayer().getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new GameNotFoundException("Game not found for player with nickname='" + nickname + "'"));
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
