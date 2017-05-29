package com.avparamonov.checkers.db.dao;

import com.avparamonov.checkers.db.entity.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

}
