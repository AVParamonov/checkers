package com.avparamonov.checkers.services;

import com.avparamonov.checkers.Application;
import com.avparamonov.checkers.db.dao.BoardRepository;
import com.avparamonov.checkers.db.dao.CellRepository;
import com.avparamonov.checkers.db.dao.CheckerRepository;
import com.avparamonov.checkers.db.dao.PlayerRepository;
import com.avparamonov.checkers.db.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by AVParamonov on 26.05.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CellRepository cellRepository;

    @Autowired
    private CheckerRepository checkerRepository;

    @Test
    public void testCreateGame() throws Exception {
        Player player1 = Player.builder()
                .nickname("Player1")
                .age(25)
                .type(PlayerType.HUMAN)
                .side(Side.BLACK)
                .build();
        playerRepository.save(player1);

        Player player2 = Player.builder()
                .nickname("Player2")
                .type(PlayerType.COMPUTER)
                .side(Side.RED)
                .build();
        playerRepository.save(player2);

        gameService.createGame(player1, player2);

        List<Game> games = gameService.findAll();
        List<Player> players = (List<Player>) playerRepository.findAll();
        List<Board> boards = (List<Board>) boardRepository.findAll();
        List<Cell> cells = (List<Cell>) cellRepository.findAll();
        List<Checker> checkers = (List<Checker>) checkerRepository.findAll();

        Assert.assertEquals(1, games.size());
        Assert.assertEquals(2, players.size());
        Assert.assertEquals(1, boards.size());
        Assert.assertEquals(64, cells.size());
        Assert.assertEquals(24, checkers.size());
    }

}
