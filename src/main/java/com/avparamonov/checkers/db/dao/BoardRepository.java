package com.avparamonov.checkers.db.dao;

import com.avparamonov.checkers.db.entity.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Integer> {

}
