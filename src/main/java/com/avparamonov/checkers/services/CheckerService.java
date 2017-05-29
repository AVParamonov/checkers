package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.CheckerRepository;
import com.avparamonov.checkers.db.entity.Checker;
import com.avparamonov.checkers.db.entity.Side;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Checker service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class CheckerService {

    private final CheckerRepository checkerRepository;

    @Autowired
    public CheckerService(CheckerRepository checkerRepository) {
        this.checkerRepository = checkerRepository;
    }

    public Checker create(Side side) {
        Checker checker = new Checker();
        checker.setSide(side);
        return checkerRepository.save(checker);
    }
}
