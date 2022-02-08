from player import Player


class Human(Player):
    def locate_tile(self, x, y):
        """
        find the square that is clicked
        :param x: x coord of mouse
        :param y: y coord of mouse
        :return: center coord of the square
        """
        x_num = x//self.board.square_size+1
        y_num = y//self.board.square_size+1
        return (x_num, y_num)

    def make_move(self, x, y):
        """
        player make a move
        :param x: x coord of mouse
        :param y: y coord of mouse
        :return: boolean, True if it's a valid move
        """
        tile_pos = self.locate_tile(x, y)
        if tile_pos in self.board.tiles.keys():
            # if tile is in an occupied square
            return False
        # calculate flip positions and drop the tile
        flip_pos = self.calc_flip(
            tile_pos)
        if(len(flip_pos) == 0):
            # if can flip 0 white tiles, illegal
            return False
        self.drop_tile(tile_pos, flip_pos)
        return True
