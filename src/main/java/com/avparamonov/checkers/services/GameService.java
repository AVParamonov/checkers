package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.GameRepository;
import com.avparamonov.checkers.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Game service.
 *
 * Created by AVParamonov on 25.05.17.
 */
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private PlayerService playerService;

    public Game createGame(Player player1, Player player2) {
        Game game = gameRepository.save(Game.builder()
                .board(boardService.initBoard())
                .player1(player1)
                .player2(player2)
                .build());

        List<Checker> redCheckers = game.getBoard().getCells().stream()
                .filter(c -> c.getChecker() != null)
                .map(Cell::getChecker)
                .filter(ch -> ch.getSide() == Side.RED)
                .collect(Collectors.toList());

        List<Checker> blackCheckers = game.getBoard().getCells().stream()
                .filter(c -> c.getChecker() != null)
                .map(Cell::getChecker)
                .filter(ch -> ch.getSide() == Side.BLACK)
                .collect(Collectors.toList());

        if (player1.getSide() == Side.RED) {
            player1.setCheckers(redCheckers);
            player2.setCheckers(blackCheckers);
        } else {
            player2.setCheckers(redCheckers);
            player1.setCheckers(blackCheckers);
        }

        player1.setCurrentGame(game);
        player2.setCurrentGame(game);

        playerService.saveOrUpdate(player1);
        playerService.saveOrUpdate(player2);

        return game;
    }

    public Game saveOrUpdate(Game game) {
        return gameRepository.save(game);
    }

    public List<Game> findAll() {
        return (List<Game>) gameRepository.findAll();
    }

}
