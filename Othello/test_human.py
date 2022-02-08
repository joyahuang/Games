from player_human import Human
from board import Board
BLACK = 0
BOARD_SIZE = 800
NUM_OF_SQUARE = 8

board = Board(NUM_OF_SQUARE, BOARD_SIZE)
human = Human(BLACK, board)


def test_locate_tile():
    assert human.locate_tile(2, 2) == (1, 1)
    assert human.locate_tile(102, 2) == (2, 1)


def test_make_move():
    assert human.make_move(301, 301) is False
    assert human.make_move(301, 501) is True
    assert len(human.board.tiles) == 5
    assert human.board.bnum == 4
