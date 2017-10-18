package com.avparamonov.checkers.controllers;

import com.avparamonov.checkers.dto.GameRequest;
import com.avparamonov.checkers.exceptions.GameNotFoundException;
import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.services.GameService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(value = Api.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = Api.V1.GAME, method = RequestMethod.POST)
    public ModelAndView newGame(@Valid GameRequest gameRequest, BindingResult bindingResult) throws PlayerNotFoundException {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("game");
            return modelAndView;
        }
        val game = gameService.createGame(gameRequest);

        modelAndView.setViewName("redirect:" + Api.ROOT_PATH + Api.V1.GAME + "/" + game.getId());

        return modelAndView;
    }

    @RequestMapping(value = Api.V1.GAME_BY_ID, method = RequestMethod.GET)
    public ModelAndView findGame(@PathVariable(value = "gameId") String gameId, Principal principal) throws GameNotFoundException {
        ModelAndView modelAndView = new ModelAndView();

        val nickname = principal.getName();
        val game = gameService.findById(gameId);

        modelAndView.addObject("currentGame", game);
        modelAndView.addObject("loggedUserNickname", nickname);
        modelAndView.setViewName("game");

        return modelAndView;
    }
}
