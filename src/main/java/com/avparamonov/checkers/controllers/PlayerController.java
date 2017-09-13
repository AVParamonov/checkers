package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.model.Game;
import com.avparamonov.checkers.model.Move;
import com.avparamonov.checkers.model.db.entity.Player;
import com.avparamonov.checkers.dto.PlayerRequest;
import com.avparamonov.checkers.services.GameService;
import com.avparamonov.checkers.services.PlayerService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = Api.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

//    @RequestMapping(value = Api.V1.REGISTRATION, method = RequestMethod.POST)
//    public Player createPlayer(@RequestBody @Valid PlayerRequest player) {
//
//        return playerService.create(player);
//    }

    @RequestMapping(value = Api.V1.MAKE_MOVE, method = RequestMethod.POST)
    @SneakyThrows
    public Game makeMove(@RequestParam(value = "fromRow") int fromRow,
                         @RequestParam(value = "fromCol") int fromCol,
                         @RequestParam(value = "toRow") int toRow,
                         @RequestParam(value = "toCol") int toCol,
                         @PathVariable(value = "gameId") String gameId,
                         @PathVariable(value = "playerId") int playerId) {

        Player player = playerService.findPlayerById(playerId);
        Game game = gameService.findById(gameId);
        Move move = new Move(fromRow, fromCol, toRow, toCol);

        return playerService.makeMove(player, move, game);
    }
}
