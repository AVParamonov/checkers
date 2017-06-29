package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.model.Game;
import com.avparamonov.checkers.model.GameType;
import com.avparamonov.checkers.services.GameService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Api.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = Api.V1.CREATE_GAME, method = RequestMethod.POST)
    @SneakyThrows
    public Game newGame(@RequestParam(value = "nickname1") String nickname1,
                        @RequestParam(value = "nickname2") String nickname2,
                        @RequestParam(value = "gameType") String gameType) {

        return gameService.createGame(nickname1, nickname2, GameType.valueOf(gameType.toUpperCase()));
    }

    @RequestMapping(value = Api.V1.CREATE_GAME, method = RequestMethod.GET)
    @SneakyThrows
    public Game findGame(@RequestParam(value = "gameId") String gameId) {

        return gameService.findById(gameId);
    }
}
