
import const
EIGHT_DIRECTION = [[-1, -1], [-1, 1], [1, 1],
                   [1, -1], [1, 0], [0, 1], [-1, 0], [0, -1]]


class Player:
    def __init__(self, color, board):
        self.color = color  # color of player
        self.board = board  # board

    def empty_squares(self):
        """
        find empty squares
        :return: squares that can still be placed with tiles
        """
        positions = [(i+1, j+1)
                     for i in range(self.board.num_of_square)
                     for j in range(self.board.num_of_square)]
        positions = filter(
            lambda x: x not in self.board.tiles.keys(), positions)
        return list(positions)

    def has_legal(self):
        """
        judge if current player still can make legam moves
        :return: boolean, True if still have legal moves
        """
        positions = self.empty_squares()
        for pos in positions:
            increment = len(self.calc_flip(
                pos))
            if increment > 0:
                return True
        return False

    def calc_flip(self,  pos):
        """
        calculate flip scenario without actually flip them
        :param pos: intended position for the new tile
        :return: list of to-be-flipped positions
        """
        flip_pos_all = []
        for direction in EIGHT_DIRECTION:
            curr_pos = pos
            flip_pos_one = []
            while True:
                curr_pos = (curr_pos[0]+direction[0],
                            curr_pos[1]+direction[1])
                if curr_pos not in self.board.tiles.keys():
                    break
                curr_tile = self.board.tiles[curr_pos]
                if curr_tile.color == self.color:
                    flip_pos_all += flip_pos_one
                    break
                flip_pos_one.append(curr_tile.tile_pos)
        return flip_pos_all

    def flip(self, positions):
        """
        flip the tiles on board and update tile color count
        :param positions: list of to-be-flipped positions
        :return:
        """
        for pos in positions:
            self.board.tiles[pos].color = self.color
            if self.color == const.BLACK:
                self.board.wnum -= 1
                self.board.bnum += 1
            else:
                self.board.wnum += 1
                self.board.bnum -= 1

    def drop_tile(self, tile_pos, flip_pos):
        """
        add tile to board and flip tiles
        :param tile_pos: intended position for the new tile
        :param flip_pos: list of to-be-flipped positions
        :return:
        """
        self.board.add_tile(self.color, tile_pos)
        self.flip(flip_pos)
