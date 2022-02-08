import const as const


class Tile:
    def __init__(self, color, tile_pos):
        self.color = color
        self.tile_pos = tile_pos
        self.size = const.TILE_SCALE*const.SQUARE_SIZE

    def display(self, square_size):
        noStroke()
        fill(self.color)
        ellipse((self.tile_pos[0]-0.5)*square_size, (self.tile_pos[1]-0.5)
                * square_size, self.size, self.size)

    def __str__(self):
        return ("color: "+str(self.color) + ", x:"+str(self.tile_pos[0]) +
                ", y:"+str(self.tile_pos[1]))
