from player import Player
from board import Board
BLACK = 0
BOARD_SIZE = 800
NUM_OF_SQUARE = 8

board = Board(NUM_OF_SQUARE, BOARD_SIZE)
player = Player(BLACK, board)


def test_init():
    assert player.color == BLACK
    assert len(player.board.tiles) == 4


def test_empty_squares():
    positions = player.empty_squares()
    assert len(positions) == NUM_OF_SQUARE * \
        NUM_OF_SQUARE-len(player.board.tiles)


def test_calc_flip():
    """
    old    want-to-drop   should-be
    bw     bwb            bbb
    wb     wb             wb
    the upper right white tile is the to-be-flipped tile
    we don't actually flip or drop anything in this method
    """
    pos = (4, 6)
    flip_pos = player.calc_flip(pos)
    assert flip_pos == [(4, 5)]


def test_flip():
    """
    old      should-be
    bw       bb
    wb       wb
    try to flip the upper right white tile
    """
    flip_pos = [(4, 5)]
    player.flip([(4, 5)])
    assert len(player.board.tiles) == 4
    assert player.board.tiles[flip_pos[0]].color == BLACK
    assert player.board.wnum == 1
    assert player.board.bnum == 3


def test_drop_tile():
    """
    old      drop-a-tile   flip-tile
    bb       bb            bb
    wb      bwb           bbb
    try to drop a black tile at lower left
    """
    tile_pos = (5, 3)
    flip_pos = player.calc_flip(tile_pos)
    player.drop_tile(tile_pos, flip_pos)
    assert len(player.board.tiles) == 5
    assert player.board.wnum == 0
    assert player.board.bnum == 5


def test_has_legal():
    """
    from the graph above, the player has no more legal moves
    because all tile are black now.
    """
    assert player.has_legal() is False
    board2 = Board(NUM_OF_SQUARE, BOARD_SIZE)
    player2 = Player(0, board2)
    assert player2.has_legal() is True
