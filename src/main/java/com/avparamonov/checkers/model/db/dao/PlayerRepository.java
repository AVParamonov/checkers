package com.avparamonov.checkers.model.db.dao;

import com.avparamonov.checkers.model.db.entity.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

    Optional<Player> findByNickname(String nickname);

}
