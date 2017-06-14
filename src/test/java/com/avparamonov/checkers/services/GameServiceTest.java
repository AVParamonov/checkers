package com.avparamonov.checkers.services;

import com.avparamonov.checkers.Application;
import com.avparamonov.checkers.model.*;
import com.avparamonov.checkers.model.db.dao.PlayerRepository;
import com.avparamonov.checkers.model.db.entity.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


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
    private PlayerRepository playerRepository;

    private Player player1;
    private Player player2;
    private Game game;

    @Before
    public void setUp() {
        player1 = playerService.create("Tom", PlayerType.HUMAN, Side.WHITE);
        player2 = playerService.create("Jerry", PlayerType.COMPUTER, Side.BLACK);
    }

    @Test
    public void testCreateGame() throws Exception {
        game = gameService.createGame("Tom", "Jerry");

    }

    @Test
    public void testMakeRegularJump() throws Exception {
        game = gameService.createGame("Tom", "Jerry");

        Checker[][] board = game.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        board[2][4] = Checker.builder().row(2).col(4).side(Side.WHITE).type(CheckerType.REGULAR).build();
        board[3][3] = Checker.builder().row(3).col(3).side(Side.BLACK).type(CheckerType.REGULAR).build();

        Assert.assertNotNull(board[3][3]);

        Move jump = new Move(2, 4, 4, 2);
        gameService.makeMove(player1, jump, game);

        Assert.assertNull(board[3][3]);
    }

    @Test
    public void testMakeKingJump() throws Exception {
        game = gameService.createGame("Tom", "Jerry");

        Checker[][] board = game.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        board[2][4] = Checker.builder().row(2).col(4).side(Side.WHITE).type(CheckerType.KING).build();
        board[3][3] = Checker.builder().row(3).col(3).side(Side.BLACK).type(CheckerType.REGULAR).build();

        Assert.assertNotNull(board[3][3]);

        Move jump = new Move(2, 4, 5, 1);
        gameService.makeMove(player1, jump, game);

        Assert.assertNull(board[3][3]);
    }

    @After
    public void tearDown() {
        playerRepository.deleteAll();
    }
}
