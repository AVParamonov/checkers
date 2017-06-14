package com.avparamonov.checkers.services;

import com.avparamonov.checkers.model.Checker;
import com.avparamonov.checkers.model.CheckerType;
import com.avparamonov.checkers.model.Side;
import org.springframework.stereotype.Service;

/**
 * Checker service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class CheckerService {

    public Checker createWithSideAndCoordinates(Side side, int row, int col) {
        return Checker.builder()
                .row(row)
                .col(col)
                .type(CheckerType.REGULAR)
                .side(side)
                .build();
    }

}
