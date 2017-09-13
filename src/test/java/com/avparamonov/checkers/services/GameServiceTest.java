package com.avparamonov.checkers.services;

import com.avparamonov.checkers.Application;
import com.avparamonov.checkers.dto.GameRequest;
import com.avparamonov.checkers.dto.PlayerRequest;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.*;
import com.avparamonov.checkers.model.db.repository.PlayerRepository;
import com.avparamonov.checkers.model.db.entity.Player;
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
    public void setUp() throws PlayerNotFoundException {
        player1 = playerService.create(new PlayerRequest().setNickname("Tom").setRole("USER").setPassword("passwd"));
        player2 = playerService.create(new PlayerRequest().setNickname("Jerry").setRole("USER").setPassword("passwd"));
        playerService.setPlayerSide("Tom", Side.WHITE);
        playerService.setPlayerSide("Jerry", Side.BLACK);
        GameRequest gameRequest = new GameRequest()
                .setGameType(GameType.NORMAL.name())
                .setWhiteCheckersNickname(player1.getNickname())
                .setBlackCheckersNickname(player2.getNickname());
        game = gameService.createGame(gameRequest);
    }

    @Test
    public void testCreateGame() throws Exception {
        gameService.getCurrentGames().clear();

        GameRequest gameRequest = new GameRequest()
                .setGameType(GameType.NORMAL.name())
                .setWhiteCheckersNickname(player1.getNickname())
                .setBlackCheckersNickname(player2.getNickname());

        game = gameService.createGame(gameRequest);

        Assert.assertNotNull(game.getId());
        Assert.assertNotNull(game.getBoard());
        Assert.assertEquals(player1, game.getPlayer1());
        Assert.assertEquals(player2, game.getPlayer2());
        Assert.assertEquals(game, gameService.findById(game.getId()));
    }

    @Test
    public void testMakeRegularJump() throws Exception {

        Checker playerChecker = Checker.builder().row(2).col(4).side(Side.WHITE).type(CheckerType.REGULAR).build();
        Checker enemyChecker = Checker.builder().row(3).col(3).side(Side.BLACK).type(CheckerType.REGULAR).build();

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(playerChecker, enemyChecker));

        Move jump = new Move(2, 4, 4, 2);

        Assert.assertEquals(playerChecker, board[2][4]);
        Assert.assertEquals(enemyChecker, board[3][3]);

        playerService.makeMove(player1, jump, game);

        Assert.assertNull(board[3][3]);
        Assert.assertNull(board[2][4]);
        Assert.assertEquals(playerChecker, board[4][2]);
    }

    @Test
    public void testMakeRegularMove() throws Exception {

        Checker checker1 = Checker.builder().row(2).col(4).side(Side.WHITE).type(CheckerType.REGULAR).build();
        Checker checker2 = Checker.builder().row(3).col(3).side(Side.WHITE).type(CheckerType.REGULAR).build();

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(checker1, checker2));

        Move move = new Move(2, 4, 3, 5);

        Assert.assertNull(board[3][5]);
        Assert.assertEquals(checker1, board[2][4]);
        Assert.assertEquals(checker2, board[3][3]);

        playerService.makeMove(player1, move, game);

        Assert.assertEquals(checker1, board[3][5]);
    }

    @Test
    public void testMakeKingJump() throws Exception {

        Checker playerKingChecker = Checker.builder().row(2).col(4).side(Side.WHITE).type(CheckerType.KING).build();
        Checker enemyChecker = Checker.builder().row(3).col(3).side(Side.BLACK).type(CheckerType.REGULAR).build();

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(playerKingChecker, enemyChecker));

        Move jump = new Move(2, 4, 5, 1);

        Assert.assertEquals(playerKingChecker, board[2][4]);
        Assert.assertEquals(enemyChecker, board[3][3]);

        playerService.makeMove(player1, jump, game);

        Assert.assertNull(board[3][3]);
        Assert.assertNull(board[2][4]);
        Assert.assertEquals(playerKingChecker, board[5][1]);
    }

    @Test
    public void testMakeKingMove() throws Exception {

        Checker playerKingChecker = Checker.builder().row(2).col(4).side(Side.WHITE).type(CheckerType.KING).build();
        Checker playerOtherChecker = Checker.builder().row(3).col(3).side(Side.WHITE).type(CheckerType.REGULAR).build();

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(playerKingChecker, playerOtherChecker));

        Move jump = new Move(2, 4, 0, 2);

        Assert.assertEquals(playerKingChecker, board[2][4]);
        Assert.assertEquals(playerOtherChecker, board[3][3]);

        playerService.makeMove(player1, jump, game);

        Assert.assertNull(board[2][4]);
        Assert.assertEquals(playerOtherChecker, board[3][3]);
        Assert.assertEquals(playerKingChecker, board[0][2]);
    }

    private Checker[][] fillBoard(Checker[][] board, List<Checker> checkers) {
        for (Checker checker: checkers) {
            putOnBoard(board, checker);
        }
        return board;
    }

    private Checker[][] makeBoardEmpty(Checker[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                board[row][col] = null;
            }
        }
        return board;
    }

    private void putOnBoard(Checker[][] board, Checker checker) {
        board[checker.getRow()][checker.getCol()] = checker;
    }

    @After
    public void tearDown() {
        playerRepository.deleteAll();
        gameService.getCurrentGames().clear();
    }
}
