from tile import Tile
from square import Square
import const as const

FOUR_DIRECTION = [[0, 0], [0, 1], [1, 1], [1, 0]]


class Board:
    def __init__(self, num_of_square, board_size):
        self.num_of_square = num_of_square  # number of squares in one edge
        self.board_size = board_size  # width of the board
        self.square_size = board_size / num_of_square  # the width of square
        self.tiles = {}  # dictionary of existing, position: tile
        self.bnum = 0  # black tile number
        self.wnum = 0  # white tile number
        self.first4()  # place four tiles at center by default

    def first4(self):
        """
        initialize first four tiles on board
        consider the first four center tiles as
        click four positions from the center of the board
        :return:
        """
        color = const.BLACK
        for i, j in FOUR_DIRECTION:
            self.add_tile(color, (self.num_of_square / 2 + i,
                                  self.num_of_square / 2 + j))
            color = 1 - color
            self.bnum = 2
            self.wnum = 2

    def display(self):
        """
        display square objects and tile objects on screen
        :return:
        """
        for i in range(0, self.num_of_square):
            for j in range(0, self.num_of_square):
                x = i * self.square_size
                y = j * self.square_size
                square = Square(x, y, self.square_size)
                square.display()
        for position, tile in self.tiles.items():
            tile.display(self.square_size)

    def add_tile(self, color, pos):
        """
        add new tile on board and update tile number
        :param color: color of tile
        :param pos: position of tile
        :return:
        """
        self.tiles[pos] = Tile(color, pos)
        if color == const.BLACK:
            self.bnum += 1
        else:
            self.wnum += 1
