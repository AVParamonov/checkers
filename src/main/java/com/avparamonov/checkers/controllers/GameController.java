package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.dto.GameRequest;
import com.avparamonov.checkers.model.Game;
import com.avparamonov.checkers.model.GameType;
import com.avparamonov.checkers.services.GameService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Api.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = Api.V1.GAME, method = RequestMethod.POST)
    @SneakyThrows
    public Game newGame(@RequestBody @Valid GameRequest gameRequest) {

        return gameService.createGame(gameRequest);
    }

    @RequestMapping(value = Api.V1.GAME, method = RequestMethod.GET)
    @SneakyThrows
    public Game findGame(@RequestParam(value = "gameId") String gameId) {

        return gameService.findById(gameId);
    }
}
