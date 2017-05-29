package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.GameRepository;
import com.avparamonov.checkers.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public void createGame(Player player1, Player player2) {
        Game game = Game.builder()
                .board(boardService.initBoard())
                .players(Arrays.asList(player1, player2))
                .build();
        gameRepository.save(game);

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
        playerService.update(player1);
        playerService.update(player2);
    }

    public List<Game> findAll() {
        return (List<Game>) gameRepository.findAll();
    }

}
