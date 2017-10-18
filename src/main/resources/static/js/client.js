
function createNewGame() {
    $.post('checkers/v1/game',
        {
            whiteSideNickname: $("#whiteSideNickname").val(),
            blackSideNickname: $("#blackSideNickname").val(),
            gameType: $("#gameType").val()
        }
    );
}

function makeMove(gameId, playerId, fromCell, toCell) {
    var fromRow = parseInt(fromCell.charAt(0));
    var fromCol = parseInt(fromCell.charAt(1));
    var toRow = parseInt(toCell.charAt(0));
    var toCol = parseInt(toCell.charAt(1));
    var url = gameId + '/player/' + playerId + '/move';
    return $.post(
        url,
        {
            fromRow: fromRow,
            fromCol: fromCol,
            toRow: toRow,
            toCol: toCol
        }
    );
}

function getStyleIndex(index, loggedUserNickname, game) {
    return loggedUserNickname === game.blackSidePlayer.nickname ? Math.abs(index - game.board.length) - 1 : index;

}