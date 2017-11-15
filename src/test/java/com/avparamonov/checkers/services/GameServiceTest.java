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

    private Player whiteSidePlayer;
    private Player blackSidePlayer;
    private GameRequest gameRequest;
    private Game game;

    @Before
    public void setUp() throws PlayerNotFoundException {
        whiteSidePlayer = playerService.create(new PlayerRequest().setNickname("Tom").setRole("USER").setPassword("passwd"));
        blackSidePlayer = playerService.create(new PlayerRequest().setNickname("Jerry").setRole("USER").setPassword("passwd"));
        playerService.setPlayerSide("Tom", Side.WHITE);
        playerService.setPlayerSide("Jerry", Side.BLACK);
        whiteSidePlayer = playerRepository.findOne(whiteSidePlayer.getId());
        blackSidePlayer = playerRepository.findOne(blackSidePlayer.getId());
        gameRequest = new GameRequest()
                .setGameType(GameType.NORMAL.name())
                .setWhiteSideNickname(whiteSidePlayer.getNickname())
                .setBlackSideNickname(blackSidePlayer.getNickname());
        game = gameService.createGame(gameRequest);
    }

    @Test
    public void testCreateGame() throws Exception {
        gameService.getCurrentGames().clear();

        Game newGame = gameService.createGame(gameRequest);

        Assert.assertNotNull(newGame.getId());
        Assert.assertNotNull(newGame.getBoard());
        Assert.assertEquals(whiteSidePlayer, newGame.getWhiteSidePlayer());
        Assert.assertEquals(blackSidePlayer, newGame.getBlackSidePlayer());
        Assert.assertEquals(newGame, gameService.findById(newGame.getId()));
    }

    @Test
    public void testMakeRegularJump() throws Exception {

        Checker whiteChecker = new Checker().setRow(2).setCol(4).setSide(Side.WHITE).setType(CheckerType.REGULAR);
        Checker blackChecker = new Checker().setRow(3).setCol(3).setSide(Side.BLACK).setType(CheckerType.REGULAR);

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(whiteChecker, blackChecker));

        Move jump = new Move(2, 4, 4, 2);

        Assert.assertEquals(whiteChecker, board[2][4]);
        Assert.assertEquals(blackChecker, board[3][3]);

        playerService.makeMove(whiteSidePlayer, jump, game);

        Assert.assertNull(board[3][3]);
        Assert.assertNull(board[2][4]);
        Assert.assertEquals(whiteChecker, board[4][2]);
    }

    @Test
    public void testMakeRegularMove() throws Exception {

        Checker whiteChecker = new Checker().setRow(2).setCol(4).setSide(Side.WHITE).setType(CheckerType.REGULAR);
        Checker whiteNeighbourChecker = new Checker().setRow(3).setCol(3).setSide(Side.WHITE).setType(CheckerType.REGULAR);

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(whiteChecker, whiteNeighbourChecker));

        Move move = new Move(2, 4, 3, 5);

        Assert.assertNull(board[3][5]);
        Assert.assertEquals(whiteChecker, board[2][4]);
        Assert.assertEquals(whiteNeighbourChecker, board[3][3]);

        playerService.makeMove(whiteSidePlayer, move, game);

        Assert.assertEquals(whiteChecker, board[3][5]);
    }

    @Test
    public void testMakeKingJump() throws Exception {

        Checker whiteKingChecker = new Checker().setRow(2).setCol(4).setSide(Side.WHITE).setType(CheckerType.KING);
        Checker blackChecker = new Checker().setRow(3).setCol(3).setSide(Side.BLACK).setType(CheckerType.REGULAR);

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(whiteKingChecker, blackChecker));

        Move jump = new Move(2, 4, 5, 1);

        Assert.assertEquals(whiteKingChecker, board[2][4]);
        Assert.assertEquals(blackChecker, board[3][3]);

        playerService.makeMove(whiteSidePlayer, jump, game);

        Assert.assertNull(board[3][3]);
        Assert.assertNull(board[2][4]);
        Assert.assertEquals(whiteKingChecker, board[5][1]);
    }

    @Test
    public void testMakeKingMove() throws Exception {

        Checker whiteKingChecker = new Checker().setRow(2).setCol(4).setSide(Side.WHITE).setType(CheckerType.KING);
        Checker whiteNeighbourChecker = new Checker().setRow(3).setCol(3).setSide(Side.WHITE).setType(CheckerType.REGULAR);

        Checker[][] board = fillBoard(makeBoardEmpty(game.getBoard()), Arrays.asList(whiteKingChecker, whiteNeighbourChecker));

        Move jump = new Move(2, 4, 0, 2);

        Assert.assertEquals(whiteKingChecker, board[2][4]);
        Assert.assertEquals(whiteNeighbourChecker, board[3][3]);

        playerService.makeMove(whiteSidePlayer, jump, game);

        Assert.assertNull(board[2][4]);
        Assert.assertEquals(whiteNeighbourChecker, board[3][3]);
        Assert.assertEquals(whiteKingChecker, board[0][2]);
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
