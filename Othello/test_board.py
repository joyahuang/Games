from board import Board
BLACK = 0
WHITE = 1
BOARD_SIZE = 800
NUM_OF_SQUARE = 8

board = Board(NUM_OF_SQUARE, BOARD_SIZE)


def test_init():
    assert board.board_size == 800
    assert board.num_of_square == 8
    assert board.square_size == 100
    assert len(board.tiles) == 4
    assert board.bnum == 2
    assert board.wnum == 2


def test_add_tile():
    board.add_tile(BLACK, (1, 1))
    assert board.bnum == 3
    assert board.wnum == 2
    assert len(board.tiles) == 5
