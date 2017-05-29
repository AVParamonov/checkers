package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.BoardRepository;
import com.avparamonov.checkers.db.entity.Board;
import com.avparamonov.checkers.db.entity.Cell;
import com.avparamonov.checkers.db.entity.Side;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Board service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CellService cellService;

    @Autowired
    private CheckerService checkerService;

    public Board initBoard() {
        List<Cell> cells = IntStream.range(1, 9)
                .mapToObj(r -> IntStream.range(1, 9)
                        .mapToObj(c -> {
                            Cell cell = cellService.createInPosition(r, c);
                            if(((r == 1 | r == 3) && c % 2 != 0) || (r == 2 && c % 2 == 0)) {
                                cell.setChecker(checkerService.create(Side.BLACK));
                                cell.setOccupied(true);
                            } else if(((r == 6 | r == 8) && c % 2 == 0) || (r == 7 && c % 2 != 0)) {
                                cell.setChecker(checkerService.create(Side.RED));
                                cell.setOccupied(true);
                            }
                            return cellService.update(cell);
                        })
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Board board = Board.builder()
                .cells(cells)
                .build();
        return boardRepository.save(board);
    }
}
