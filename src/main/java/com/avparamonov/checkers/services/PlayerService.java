package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.PlayerRepository;
import com.avparamonov.checkers.db.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Player service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public void create(Player player) {
        playerRepository.save(player);
    }

    public void update(Player player) {
        playerRepository.save(player);
    }
}
