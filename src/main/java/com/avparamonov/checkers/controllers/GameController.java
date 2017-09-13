package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.dto.GameRequest;
import com.avparamonov.checkers.exceptions.GameNotFoundException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.Game;
import com.avparamonov.checkers.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Api.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = Api.V1.GAME, method = RequestMethod.POST)
    public Game newGame(@Valid GameRequest gameRequest) throws PlayerNotFoundException {

        return gameService.createGame(gameRequest);
    }

    @RequestMapping(value = Api.V1.GAME, method = RequestMethod.GET)
    public Game findGame(@RequestParam(value = "gameId") String gameId) throws GameNotFoundException {

        return gameService.findById(gameId);
    }
}
