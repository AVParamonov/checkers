package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.BoardRepository;
import com.avparamonov.checkers.db.entity.*;
import com.avparamonov.checkers.exceptions.CellNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.avparamonov.checkers.db.entity.CheckerType.*;
import static com.avparamonov.checkers.db.entity.Side.*;

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

    private List<Cell> availableMoves;
    private List<Cell> availableJumps;

    public Board initBoard() {
        List<Cell> cells = IntStream.range(1, 9)
                .mapToObj(r -> IntStream.range(1, 9)
                        .filter(c -> (r % 2 != 0 && c % 2 != 0) || (r % 2 == 0 && c % 2 == 0))
                        .mapToObj(c -> {
                            Cell cell = cellService.createWithPosition(r, c);

                            if (((r == 1 | r == 3) && c % 2 != 0) || (r == 2 && c % 2 == 0)) {
                                cell.setChecker(checkerService.createWithSide(BLACK));

                            } else if (((r == 6 | r == 8) && c % 2 == 0) || (r == 7 && c % 2 != 0)) {
                                cell.setChecker(checkerService.createWithSide(RED));
                            }

                            return cellService.saveOrUpdate(cell);
                        })
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return boardRepository.save(Board.builder()
                .cells(cells)
                .build());
    }

    public void makeMove(Cell fromCell, Cell toCell) {
        Assert.notNull(fromCell.getChecker());

        Checker checker = fromCell.getChecker();
        int borderRow = toCell.getRow();

        // became a KING
        if (checker.getType() == REGULAR) {
            if ((borderRow == 1 && checker.getSide() == RED) || (borderRow == 8 && checker.getSide() == BLACK)) {
                checker.setType(KING);
                checkerService.saveOrUpdate(checker);
            }
        }
        fromCell.setChecker(null);
        cellService.saveOrUpdate(fromCell);

        toCell.setChecker(checker);
        cellService.saveOrUpdate(toCell);
    }

    public void makeJump(Cell fromCell, Cell overCell, Cell toCell) {
        Assert.notNull(fromCell.getChecker());

        Checker checker = fromCell.getChecker();
        fromCell.setChecker(null);
        cellService.saveOrUpdate(fromCell);

        Checker enemyChecker = overCell.getChecker();
        if (enemyChecker != null) {
            overCell.setChecker(null);
            checkerService.remove(enemyChecker);
        }

        toCell.setChecker(checker);
        cellService.saveOrUpdate(toCell);
    }

    public List<Cell> collectRegularMoves(List<Cell> emptyCells, Cell fromCell) {
        Side side = fromCell.getChecker().getSide();
        return emptyCells.stream()
                // distance is one column long
                .filter(c -> Math.abs(c.getCol() - fromCell.getCol()) == 1)
                // moves are in right direction and distance is one row long
                .filter(c -> (side == BLACK && c.getRow() - fromCell.getRow() == 1)
                             || (side == RED && fromCell.getRow() - c.getRow() == 1))
                .collect(Collectors.toList());
    }

    public List<Cell> collectKingMoves(List<Cell> emptyCells, Cell fromCell) {
        return emptyCells.stream()
                // diagonal route
                .filter(c -> (Math.abs(c.getRow() - fromCell.getRow()) == Math.abs(c.getCol() - fromCell.getCol())))
                .collect(Collectors.toList());
    }

    public List<Cell> collectRegularJumps(Board board, Cell fromCell) {
        return board.getCells().stream()
                // jump cells
                .filter(c -> (Math.abs(c.getRow() - fromCell.getRow()) == 2 && Math.abs(c.getCol() - fromCell.getCol()) == 2))
                .filter(c -> c.getChecker() == null)
                .filter(j -> {
                    int row = (j.getRow() + fromCell.getRow()) / 2;
                    int col = (j.getCol() + fromCell.getCol()) / 2;
                    return board.getCells().stream()
                            // find cell
                            .filter(c -> c.getRow() == row && c.getCol() == col)
                            .filter(c -> c.getChecker() != null)
                            // enemy
                            .anyMatch(e -> e.getChecker().getSide() != fromCell.getChecker().getSide());
                })
                .collect(Collectors.toList());
    }

    public boolean collectKingJumps(List<Cell> emptyCells, Cell fromCell) {
        int fromRow = fromCell.getRow();
        int fromCol = fromCell.getCol();

        List<Cell> kingMoves = collectKingMoves(emptyCells, fromCell);

//        kingMoves.stream()
//
//        // if diagonal route
//        if (Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol)) {
//            if (Math.abs(fromRow - toRow) >= 2 && Math.abs(fromCol - toCol) >= 2) {
//                if () {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    public List<Cell> getMoves(Board board, Checker checker) {
        List<Cell> moves = new ArrayList<>();
        Cell currentCell = null;
        try {
            currentCell = getCell(board, checker);
        } catch (CellNotFoundException e) {
            e.printStackTrace();
        }
        List<Cell> emptyCells = board.getCells().stream()
                .filter(c -> c.getChecker() == null)
                .collect(Collectors.toList());

        if (checker.getType() == CheckerType.REGULAR) {
            moves = collectRegularMoves(emptyCells, currentCell);
        } else if (checker.getType() == CheckerType.KING) {
            moves = collectKingMoves(emptyCells, currentCell);
        }
        return moves;
    }

    public List<Cell> getJumps(Board board, Checker checker) {
        List<Cell> jumps = new ArrayList<>();
        Cell currentCell = null;
        try {
            currentCell = getCell(board, checker);
        } catch (CellNotFoundException e) {
            e.printStackTrace();
        }
        List<Cell> emptyCells = board.getCells().stream()
                .filter(c -> c.getChecker() == null)
                .collect(Collectors.toList());

        if (checker.getType() == CheckerType.REGULAR) {
            jumps = collectRegularJumps(board, currentCell);
//        } else if (checker.getType() == CheckerType.KING) {
//            jumps = collectKingMoves(emptyCells, currentCell);
        }
        return jumps;
    }

    private Cell getCell(Board board, Checker checker) throws CellNotFoundException {
        return board.getCells().stream()
                .filter(c -> c.getChecker() != null)
                .filter(c -> c.getChecker().equals(checker))
                .findFirst()
                .orElseThrow(() -> new CellNotFoundException("Cell not found for Checker with id='" + checker.getId()
                        + "' on Board with id='" + board.getId()+ "'"));
    }
}
