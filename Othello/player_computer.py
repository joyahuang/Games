from player import Player


class Computer(Player):
    def simple_ai(self):
        """
        calculate one step ahead and find best outcome
        :return: best white tile increment
        """
        # find all unoccupied positions
        positions = self.empty_squares()
        max_wnum_increment = 0
        max_flip_pos = []
        max_pos = ()
        for pos in positions:
            # iterate through all posible positions
            flip_pos = self.calc_flip(
                pos)
            # get to-be-flipped position list
            if len(flip_pos) > max_wnum_increment:
                # find the best white increment result
                max_wnum_increment = len(flip_pos)
                max_flip_pos = flip_pos
                max_pos = pos
        return max_flip_pos, max_pos

    def make_move(self):
        """
        find the best move using simple ai
        and drop the tile
        :return:
        """
        max_flip_pos, max_pos = self.simple_ai()
        self.drop_tile(max_pos, max_flip_pos)
        return True
