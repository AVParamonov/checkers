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
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping(value = Api.ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping(Api.V1.GAME_BY_ID)
    public void move(Message<Object> message, Move move, @DestinationVariable("gameId") String gameId) throws
            PlayerNotFoundException, GameNotFoundException, CheckerNotFoundException, MoveNotAllowedException {

        Principal principal = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        String nickname = principal.getName();

        Player player = playerService.findPlayerByNickname(nickname);
        Game game = gameService.findById(gameId);

        Game updatedGame = playerService.makeMove(player, move, game);

        template.convertAndSendToUser(game.getWhiteSidePlayer().getNickname(), "/queue/moves", updatedGame);
        template.convertAndSendToUser(game.getBlackSidePlayer().getNickname(), "/queue/moves", updatedGame);
    }
}
