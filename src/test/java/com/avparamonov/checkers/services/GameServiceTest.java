package com.avparamonov.checkers.services;

import com.avparamonov.checkers.Application;
import com.avparamonov.checkers.db.dao.*;
import com.avparamonov.checkers.db.entity.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static com.avparamonov.checkers.db.entity.CheckerType.*;
import static com.avparamonov.checkers.db.entity.Side.*;


/**
 * Created by AVParamonov on 26.05.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CellRepository cellRepository;

    @Autowired
    private CheckerRepository checkerRepository;

    private Player player1;
    private Player player2;
    private Game game;

    @Before
    public void setUp() {
        player1 = Player.builder()
                .nickname("Player1")
                .age(25)
                .type(PlayerType.HUMAN)
                .side(Side.BLACK)
                .build();
        playerRepository.save(player1);

        player2 = Player.builder()
                .nickname("Player2")
                .type(PlayerType.COMPUTER)
                .side(Side.RED)
                .build();
        playerRepository.save(player2);

    }

    @Test
    public void testCreateGame() throws Exception {
        game = gameService.createGame(player1, player2);

        List<Game> games = (List<Game>) gameRepository.findAll();
        List<Player> players = (List<Player>) playerRepository.findAll();
        List<Board> boards = (List<Board>) boardRepository.findAll();
        List<Cell> cells = (List<Cell>) cellRepository.findAll();
        List<Checker> checkers = (List<Checker>) checkerRepository.findAll();

        Assert.assertEquals(1, games.size());
        Assert.assertEquals(2, players.size());
        Assert.assertEquals(1, boards.size());
        Assert.assertEquals(32, cells.size());
        Assert.assertEquals(24, checkers.size());
    }

    @Test
    public void testMakeJump() throws Exception {
        game = gameService.createGame(player1, player2);

        List<Cell> cells = game.getBoard().getCells();
        cells.forEach(c -> c.setChecker(null));
        cellRepository.save(cells);
        gameRepository.save(game);

        Checker chb1 = Checker.builder().side(BLACK).type(REGULAR).build();
        Checker chb2 = Checker.builder().side(BLACK).type(REGULAR).build();
        Checker chr1 = Checker.builder().side(RED).type(REGULAR).build();
        checkerRepository.save(Arrays.asList(chb1, chb2, chr1));

        game.getBoard().getCells().forEach(c -> {
            if (c.getRow() == 2 && c.getCol() == 4) c.setChecker(chb1);
            if (c.getRow() == 3 && c.getCol() == 5) c.setChecker(chb2);
            if (c.getRow() == 4 && c.getCol() == 4) c.setChecker(chr1);
        });

        Cell cb2 = game.getBoard().getCells().stream().filter(c -> (c.getRow() == 3 && c.getCol() == 5)).findFirst().get();
        Cell cb4 = game.getBoard().getCells().stream().filter(c -> (c.getRow() == 5 && c.getCol() == 3)).findFirst().get();

        playerService.pickCell(player1, cb2);
//        playerService.makeMove(player1, cb2, cb4);
    }

    @After
    public void tearDown() {
//        checkerRepository.deleteAll();
//        cellRepository.deleteAll();
//        boardRepository.deleteAll();
//        playerRepository.deleteAll();
//        gameRepository.deleteAll();
    }

}
