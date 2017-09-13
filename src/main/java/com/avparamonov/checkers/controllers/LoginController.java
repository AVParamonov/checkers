package com.avparamonov.checkers.controllers;

import javax.validation.Valid;

import com.avparamonov.checkers.dto.PlayerRequest;
import com.avparamonov.checkers.model.db.entity.Player;
import com.avparamonov.checkers.services.PlayerService;
import com.avparamonov.checkers.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = Api.ROOT_PATH)
public class LoginController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SecurityService securityService;


    @RequestMapping(value={Api.V1.LOGIN}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = Api.V1.REGISTRATION, method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("playerRequest", new Player());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = Api.V1.REGISTRATION, method = RequestMethod.POST)
    public ModelAndView createNewPlayer(@Valid PlayerRequest player, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Player registered = playerService.findPlayerByNickname(player.getNickname());
        if (registered != null) {
            bindingResult.rejectValue("nickname", "error.playerRequest", "Player with the same nickname already registered");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            playerService.create(player);
            modelAndView.setViewName("home");
            securityService.autoLogin(player.getNickname(), player.getPassword());
        }
        return modelAndView;
    }

}