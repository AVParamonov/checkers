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

    public Cell createInPosition(int row, int col) {
        Cell cell = new Cell();
        cell.setRow(row);
        cell.setCol(col);
        return cellRepository.save(cell);
    }

    public Cell update(Cell cell) {
        return cellRepository.save(cell);
    }
}
