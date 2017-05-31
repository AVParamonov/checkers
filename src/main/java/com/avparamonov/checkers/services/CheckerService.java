package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.CheckerRepository;
import com.avparamonov.checkers.db.entity.Checker;
import com.avparamonov.checkers.db.entity.CheckerType;
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

    @Autowired
    private CheckerRepository checkerRepository;

    public Checker createWithSide(Side side) {
        return checkerRepository.save(Checker.builder()
                .type(CheckerType.REGULAR)
                .side(side)
                .isCurrent(false)
                .build());
    }

    public Checker saveOrUpdate(Checker checker) {
        return checkerRepository.save(checker);
    }

    public void remove(Checker checker) {
        checkerRepository.delete(checker);
    }
}
