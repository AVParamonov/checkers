var currentGame;
var Game;

$(document).ready(function () {

    createNewGame().done( function(data) {
        currentGame = data;
        console.log('success', currentGame);
        Game.initialize(currentGame);
    });

    Game = {
        board: null,
        cellsElement: $('div.cells'),
        dictionary: ["0vmin", "10vmin", "20vmin", "30vmin", "40vmin", "50vmin", "60vmin", "70vmin", "80vmin", "90vmin"],
        initialize: function (game) {
            this.board = game.board;
            for (row in this.board) {
                for (col in this.board[row]) {
                    if(row%2 === col%2) {
                        this.cellsElement.append("<div class='cell' id='"+row+col+"' style='bottom:"+this.dictionary[row]+";left:"+this.dictionary[col]+";'></div>");
                    }
                }
            }
            Game.drawCheckers(game);
        },
        drawCheckers: function (game) {
            $('.white_checkers').empty();
            $('.black_checkers').empty();
            currentGame = game;
            this.board = game.board;
            for (row in this.board) {
                for (col in this.board[row]) {
                    var checker = this.board[row][col];
                    if(checker) {
                        $('.' + checker.side.toLowerCase() + '_checkers').append("<div class='checker' id='checker"+checker.row+checker.col+"' style='bottom:"+this.dictionary[row]+";left:"+this.dictionary[col]+";'></div>");
                    }
                }
            }
        }
    };
});

$(document).click(function(e) {
    console.log('click', e.target);
    var element = $(e.target);
    if (element.attr("class") === 'checker') {
        console.log('checker', element);
        var selected;
        var playerCheckers = element.parent().attr("class").split(' ')[0];
        console.log('playerCheckers', playerCheckers);
        var isPlayerTurn = (playerCheckers === currentGame.currentPlayer.side.toLowerCase()+"_checkers");
        if(isPlayerTurn) {
            console.log('playerTurn', playerCheckers);
            if(element.hasClass('selected')) selected = true;
            $('.checker').each(function(index) {$('.checker').eq(index).removeClass('selected')});
            if(!selected) {
                element.addClass('selected');
            }
        }
    } else if (element.attr("class") === 'cell') {
        if($('.selected').length !== 0) {
            var fromPosition = $('.selected').attr("id").replace(/checker/, '');
            var toPosition = element.attr("id");
            console.log('fromPosition', fromPosition);
            console.log('toPosition', toPosition);
            if (fromPosition && toPosition) {
                makeMove(currentGame.id, currentGame.currentPlayer.id, fromPosition, toPosition).done( function(data) {
                    console.log('Move done', data);
                    Game.drawCheckers(data);
                });
            }
        }
    }
});

function createNewGame() {
    return $.post(
        'checkers/v1/game',
        {
            nickname1: "Tom",
            nickname2: "Jerry",
            gameType: "normal"
        }
    );
}

function makeMove(gameId, playerId, fromCell, toCell) {
    var fromRow = parseInt(fromCell.charAt(0));
    var fromCol = parseInt(fromCell.charAt(1));
    var toRow = parseInt(toCell.charAt(0));
    var toCol = parseInt(toCell.charAt(1));
    var url = 'checkers/v1/game/' + gameId + '/player/' + playerId + '/move';
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