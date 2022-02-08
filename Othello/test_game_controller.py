from game_controller import GameController
BLACK = 0
WHITE = 1
BOARD_SIZE = 800
NUM_OF_SQUARE = 8

gc = GameController(BOARD_SIZE, NUM_OF_SQUARE)


def test_init():
    assert gc.message == ""
    assert gc.status == ""
    assert gc.board.board_size == BOARD_SIZE
    assert gc.board.num_of_square == NUM_OF_SQUARE
    assert len(gc.board.tiles) == 4
    assert gc.human.color == BLACK
    assert gc.computer.color == WHITE
    assert gc.player_finish_time == 0
    assert gc.curr_color == gc.human.color
    assert gc.has_ended is False


def test_colorname():
    # default is human color
    assert gc.colorname() == "Black"
    # change it to computer color and test
    gc.curr_color = 1-gc.curr_color
    assert gc.colorname() == "White"


def test_switch_player():
    # we changed curr_color to white in the test_colorname move
    assert gc.curr_color == gc.computer.color
    gc.switch_player()
    # now it's human turn
    assert gc.curr_color == gc.human.color
    assert gc.status != ""
    assert gc.message == ""


def test_player_make_move():
    # try drop tile in an occupied square
    gc.player_make_move(301, 301)
    assert gc.message == "Invalid move!"
    assert gc.curr_color == gc.human.color
    # try drop tile in a valid position
    gc.player_make_move(301, 501)
    assert gc.curr_color == gc.computer.color
    assert len(gc.board.tiles) == 5
    assert gc.board.bnum == 4
    # try to drop tile while it is computer's turn
    gc.player_make_move(1, 1)
    assert gc.message == "Not your turn!"


def test_computer_make_move():
    old_wnum = gc.board.wnum
    gc.computer_make_move()
    assert gc.curr_color == gc.human.color
    assert old_wnum < gc.board.wnum


def test_judge_winner():
    # after test moves, it should be a tie
    assert gc.judge_winner() == "TIE"
    # let computer put one more move and win
    gc.computer_make_move()
    assert gc.judge_winner() == "Computer"


def test_is_over():
    # let computer make one more move so that
    # there left no legal moves for both parties
    gc.computer_make_move()
    print(gc.board.wnum, gc.board.bnum)
    gc.is_over()
    assert gc.has_ended is True
