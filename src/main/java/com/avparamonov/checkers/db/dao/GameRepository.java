package com.avparamonov.checkers.db.dao;

import com.avparamonov.checkers.db.entity.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {

}
