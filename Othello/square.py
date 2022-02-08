R = 0
G = 0.5
B = 0
STROKEWEIGHT = 2
"""
Built this class because I want to try mouse hover effect
Will do that later.
"""


class Square:
    def __init__(self, up_left_x, up_left_y, size):
        self.up_left_x = up_left_x
        self.up_left_y = up_left_y
        self.size = size

    def display(self):
        stroke(0)
        strokeWeight(STROKEWEIGHT)
        fill(R, G, B)
        rect(self.up_left_x, self.up_left_y, self.size, self.size)
