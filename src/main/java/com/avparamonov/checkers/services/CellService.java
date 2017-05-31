package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.CellRepository;
import com.avparamonov.checkers.db.entity.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cell service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class CellService {

    @Autowired
    private CellRepository cellRepository;

    public Cell createWithPosition(int row, int col) {
        return cellRepository.save(Cell.builder()
                .row(row)
                .col(col)
                .build());
    }

    public Cell saveOrUpdate(Cell cell) {
        return cellRepository.save(cell);
    }
}
