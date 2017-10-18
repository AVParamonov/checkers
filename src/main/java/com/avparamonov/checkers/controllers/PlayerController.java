package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.exceptions.CheckerNotFoundException;
import com.avparamonov.checkers.exceptions.GameNotFoundException;
import com.avparamonov.checkers.exceptions.MoveNotAllowedException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.Game;
import com.avparamonov.checkers.model.Move;
import com.avparamonov.checkers.model.db.entity.Player;
import com.avparamonov.checkers.services.GameService;
import com.avparamonov.checkers.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = Api.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @RequestMapping(value = Api.V1.MAKE_MOVE, method = RequestMethod.POST)
    public Game makeMove(@RequestParam(value = "fromRow") int fromRow,
                         @RequestParam(value = "fromCol") int fromCol,
                         @RequestParam(value = "toRow") int toRow,
                         @RequestParam(value = "toCol") int toCol,
                         @PathVariable(value = "gameId") String gameId,
                         @PathVariable(value = "playerId") int playerId)
            throws PlayerNotFoundException,
            GameNotFoundException,
            CheckerNotFoundException,
            MoveNotAllowedException {

        Player player = playerService.findPlayerById(playerId);
        Game game = gameService.findById(gameId);
        Move move = new Move(fromRow, fromCol, toRow, toCol);

        return playerService.makeMove(player, move, game);
    }
}
