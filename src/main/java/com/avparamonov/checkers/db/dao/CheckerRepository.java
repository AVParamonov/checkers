package com.avparamonov.checkers.db.dao;

import com.avparamonov.checkers.db.entity.Checker;
import org.springframework.data.repository.CrudRepository;

public interface CheckerRepository extends CrudRepository<Checker, Integer> {

}
