
var stompClient = null;

function createNewGame() {
    $.post('/game',
        {
            whiteSideNickname: $("#whiteSideNickname").val(),
            blackSideNickname: $("#blackSideNickname").val(),
            gameType: $("#gameType").val()
        }
    );
}

function makeMove(gameId, fromCell, toCell) {
    var fromRow = parseInt(fromCell.charAt(0));
    var fromCol = parseInt(fromCell.charAt(1));
    var toRow = parseInt(toCell.charAt(0));
    var toCol = parseInt(toCell.charAt(1));

    stompClient.send('/checkers/v1/game/' + gameId, {}, JSON.stringify({
        'fromRow': fromRow,
        'fromCol': fromCol,
        'toRow': toRow,
        'toCol': toCol
    }));
}

function connectWS(gameId) {
    var ws = new SockJS('/v1/game/' + gameId);
    stompClient = Stomp.over(ws);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/queue/moves', function (game) {
            var updatedGame = JSON.parse(game.body);
            Game.drawCheckers(updatedGame);
        });
        stompClient.subscribe('/user/queue/error', function (error) {
            console.log(error);
            var message = error.body.slice(-12) === 'not allowed.' ? error.body.replace(/\(.*?\)/, "") : error.body;
            alert(message);
        });
    });

}

function getStyleIndex(index, loggedUserNickname, game) {
    return loggedUserNickname === game.blackSidePlayer.nickname ? Math.abs(index - game.board.length) - 1 : index;

}

function getSideByNickname(nickname, game) {
    return nickname === game.blackSidePlayer.nickname ? 'black' : 'white';


}