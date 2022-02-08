from dot import Dot


class Dots:
    """A collection of dots."""

    def __init__(self, WIDTH, HEIGHT,
                 LEFT_VERT, RIGHT_VERT,
                 TOP_HORIZ, BOTTOM_HORIZ):
        self.WIDTH = WIDTH
        self.HEIGHT = HEIGHT
        self.TH = TOP_HORIZ
        self.BH = BOTTOM_HORIZ
        self.LV = LEFT_VERT
        self.RV = RIGHT_VERT
        self.SPACING = 75
        self.EAT_DIST = 53
        # Initialize four rows of dots, based on spacing and width of the maze
        self.top_row = [Dot(self.SPACING * i, self.TH)
                        for i in range(self.WIDTH//self.SPACING + 1)]
        self.bottom_row = [Dot(self.SPACING * i, self.BH)
                           for i in range(self.WIDTH//self.SPACING + 1)]
        self.left_col = [Dot(self.LV, self.SPACING * i)
                         for i in range(self.HEIGHT//self.SPACING + 1)]
        self.right_col = [Dot(self.RV, self.SPACING * i)
                          for i in range(self.HEIGHT//self.SPACING + 1)]

    def display(self):
        """Calls each dot's display method"""
        for i in range(0, len(self.top_row)):
            self.top_row[i].display()
        for i in range(0, len(self.bottom_row)):
            self.bottom_row[i].display()
        for i in range(0, len(self.left_col)):
            self.left_col[i].display()
        for i in range(0, len(self.right_col)):
            self.right_col[i].display()

    def judge_eat(self, x, y,  dot_list):
        remove_dots = []  # put the should-be-removed dot into a list
        for one_dot in dot_list:
            if (abs(one_dot.x-x) <= self.EAT_DIST
                    and abs(one_dot.y-y) <= self.EAT_DIST):
                # if it drops in the shadow of pacman, consider it would
                # be eaten, and append it to the remove_dots list
                # can't use remove() or del on site because it will mess
                # up with the list index and iteration process
                remove_dots.append(one_dot)
        return list(filter(lambda x: x not in remove_dots, dot_list))

    # TODO:
    # PROBLEM 3: implement dot eating
    # BEGIN CODE CHANGES

    def eat(self, x, y):  # You might want/need to pass arguments here.
        self.top_row = dot_list = self.judge_eat(x, y, self.top_row)
        self.bottom_row = dot_list = self.judge_eat(
            x, y, self.bottom_row)
        self.left_col = dot_list = self.judge_eat(x, y, self.left_col)
        self.right_col = dot_list = self.judge_eat(
            x, y, self.right_col)

        # END CODE CHANGES

    def dots_left(self):
        """Returns the number of remaing dots in the collection"""
        return (len(self.top_row) +
                len(self.bottom_row) +
                len(self.left_col) +
                len(self.right_col))
