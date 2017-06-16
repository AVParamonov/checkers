package com.avparamonov.checkers.services;

import com.avparamonov.checkers.exceptions.PlayerNotFoundException;
import com.avparamonov.checkers.model.*;
import com.avparamonov.checkers.model.db.dao.PlayerRepository;
import com.avparamonov.checkers.model.db.entity.Player;
import com.avparamonov.checkers.exceptions.CheckerNotFoundException;
import com.avparamonov.checkers.exceptions.MoveNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Player service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class PlayerService {

    @Autowired
    private BoardService boardService;

    @Autowired
    private PlayerRepository playerRepository;


    public Player create(String nickname, PlayerType playerType, Side side) {
        return playerRepository.save(Player.builder()
                .nickname(nickname)
                .type(playerType)
                .side(side)
                .build());
    }

    public Player findPlayerByNickname(String nickname) throws PlayerNotFoundException {
        return playerRepository.findByNickname(nickname).orElseThrow(() ->
                new PlayerNotFoundException("Player not found with nickname='" + nickname +  "'"));
    }

    public Player findPlayerById(int id) throws PlayerNotFoundException {
        Player player = playerRepository.findOne(id);
        if (player == null) {
            throw new PlayerNotFoundException("Player not found with id='" + id +  "'");
        }
        return player;
    }

    public List<Move> getAvailableMoves(Player player, Game game) {
        return boardService.getAvailableMoves(player, game.getBoard());
    }


}
