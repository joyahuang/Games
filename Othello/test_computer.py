from player_computer import Computer
from board import Board
BLACK = 0
WHITE = 1
BOARD_SIZE = 800
NUM_OF_SQUARE = 8

board = Board(NUM_OF_SQUARE, BOARD_SIZE)
computer = Computer(WHITE, board)


def test_simple_ai():
    min_bnum_increment, min_flip_pos, min_pos = computer.simple_ai()
    assert min_bnum_increment == 1
    assert len(min_flip_pos) == 1


def test_make_move():
    # white number should increase after make move
    old_wnum = computer.board.wnum
    assert computer.make_move() is True
    assert computer.board.wnum >= old_wnum
