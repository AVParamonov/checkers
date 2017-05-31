package com.avparamonov.checkers.services;

import com.avparamonov.checkers.db.dao.PlayerRepository;
import com.avparamonov.checkers.db.entity.Cell;
import com.avparamonov.checkers.db.entity.Checker;
import com.avparamonov.checkers.db.entity.Player;
import com.avparamonov.checkers.exceptions.CheckerNotFoundException;
import com.avparamonov.checkers.exceptions.MoveNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Player service.
 *
 * Created by AVParamonov on 26.05.17.
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private BoardService boardService;

    private List<Cell> availableMoves;
    private List<Cell> availableJumps;

    public void saveOrUpdate(Player player) {
        playerRepository.save(player);
    }

    public void makeMove(Player player, Cell fromCell, Cell toCell) throws MoveNotAllowedException {
        if (!availableJumps.isEmpty() && !availableJumps.contains(toCell)) {
            throw new MoveNotAllowedException("Player with id='" + player.getId()
                    + "' have available jumps, but instead goes to cell with id='" + toCell.getId() + "'");
        }
        if (!availableJumps.isEmpty()) {
//            boardService.makeJump(fromCell, availableJumps.get(toCell), toCell);
        }
        if (!availableMoves.contains(toCell)) {
            throw new MoveNotAllowedException("Move is not allowed from cell with id='" + fromCell.getId()
                    + "' to cell with id='" + toCell.getId() + "'");
        }
        boardService.makeMove(fromCell, toCell);
    }

    public void pickCell(Player player, Cell cell) throws CheckerNotFoundException {
        Checker checker = cell.getChecker();
        if (checker == null) {
            throw new CheckerNotFoundException("Checker not found for Player with id='" + player.getId()
                    + "' in cell with id='" + cell.getId() + "'");
        }
        availableMoves = getAvailableMoves(player, checker);
        availableJumps = getAvailableJumps(player, checker);
    }

    private List<Cell> getAvailableMoves(Player player, Checker checker) {
        return boardService.getMoves(player.getCurrentGame().getBoard(), checker);
    }

    private List<Cell> getAvailableJumps(Player player, Checker checker) {
        return boardService.getJumps(player.getCurrentGame().getBoard(), checker);
    }
}
