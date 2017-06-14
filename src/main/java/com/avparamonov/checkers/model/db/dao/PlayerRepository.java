package com.avparamonov.checkers.model.db.dao;

import com.avparamonov.checkers.model.db.entity.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

    Player findByNickname(String nickname);

}
