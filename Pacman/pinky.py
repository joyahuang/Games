from game_character import GameCharacter
from eyes import Eyes


# The Pinky class extends GameCharacter, so methods defined in GameCharacter
# are inherited by objects of class Pinky.
class Pinky(GameCharacter):
    def __init__(self, maze, pacman, game_controller):
        self.CHAR_WIDTH = 100
        self.CHAR_HEIGHT = 100
        self.maze = maze
        self.pacman = pacman
        self.gc = game_controller
        self.x = maze.WIDTH/2
        self.y = maze.TOP_HORIZ
        self.velocity = 2
        self.x_add = self.velocity
        self.y_add = 0
        self.eyes = Eyes()
        self.looking = (0, 0)
        self.WIN_PROXIMITY = 80
        self.WALL_TOLERANCE = 2

    def draw_self(self, x, y):
        """Draw Pinky to the screen"""
        noStroke()
        fill(1.0, 0.5, 0.6)
        ellipse(x, y, 100, 100)
        bottom_half = createShape()
        bottom_half.beginShape()
        bottom_half.vertex(x, y)
        bottom_half.vertex(x+100, y)
        bottom_half.vertex(x+100, y+50)
        bottom_half.vertex(x+50, y+25)
        bottom_half.vertex(x, y+50)
        bottom_half.endShape()
        shape(bottom_half, -50, 0)

        self.eyes.display(x, y - 15, self.looking)

    def update(self):
        """Carry out necessary updates for each frame before
        drawing to screen"""
        # Check if Pinky is at an intersection
        # add the equal sign in the comparison because it would
        # freeze the pinky without the equal sign
        on_vert = (
            (abs(self.x - self.maze.LEFT_VERT) < self.WALL_TOLERANCE) or
            (abs(self.x - self.maze.RIGHT_VERT) < self.WALL_TOLERANCE)
        )
        on_horz = (
            (abs(self.y - self.maze.TOP_HORIZ) < self.WALL_TOLERANCE) or
            (abs(self.y - self.maze.BOTTOM_HORIZ) < self.WALL_TOLERANCE)
        )
        # Check whether Pinky is up or down/left or right of Pacman
        up_down_part = self.pacman.y - self.y
        left_right_part = self.pacman.x - self.x

        # Update Pinky's eyes to look at Pacman
        self.update_eyes(up_down_part, left_right_part)

        # If Pinky gets close to Pacman, tell the GameController
        # that Pinky wins
        if (abs(up_down_part) < self.WIN_PROXIMITY and
                abs(left_right_part) < self.WIN_PROXIMITY):
            self.gc.pinky_wins = True

        # TODO:
        # PROBLEM 2: Make Pinky chase Pacman!
        # Study the code above and below these lines to understand how
        # Pinky's movements are calculated, and how Pinky's position with
        # respect to Pacman is represented.
        # Pinky should decide at each intersection whether to go left, right
        # up or down depending on which direction Pacman is further away in.
        # START CODE CHANGES
        # use simplest path finding logic, pinky-go-through-walls not considered
        if on_vert and on_horz:
            # if the pinky is at intersection, it should find nearest route
            if abs(up_down_part) < abs(left_right_part):
                # go with the long edge
                if left_right_part > 0:
                    self.x_add = self.velocity
                else:
                    self.x_add = -self.velocity
                # x_add and y_add can't both be non-zero, because if the pinky move
                # diagonal, it will shift out of the road or freeze
                self.y_add = 0
            else:
                if up_down_part > 0:
                    self.y_add = self.velocity
                else:
                    self.y_add = -self.velocity
                self.x_add = 0
        elif on_vert:
            # if pinky can move vertically
            if up_down_part > 0 and abs(left_right_part) == 0:
                # pacman is on the down direction and on the same road
                self.y_add = self.velocity
            elif up_down_part < 0 and abs(left_right_part) == 0:
                # pacman is on the up direction and on the same road
                self.y_add = -self.velocity
            # else: pacman not on the same road, just go with old direction
        elif on_horz:
            # if pinky can move horizontally
            if left_right_part > 0 and abs(up_down_part) == 0:
                # pacman is on the right direction and on the same road
                self.x_add = self.velocity
            elif left_right_part < 0 and abs(up_down_part) == 0:
                # pacman is on the left direction and on the same road
                self.x_add = -self.velocity
            # else pacman not on the same road, just go with old direction
        # END CODE CHANGES
        # If the player wins, stop Pinky moving
        if self.gc.player_wins or self.gc.pinky_wins:
            self.x_add = 0
            self.y_add = 0

        # Move Pinky
        self.x = self.x + self.x_add
        self.y = self.y + self.y_add

    def update_eyes(self, up_down_part, left_right_part):
        """Set self.looking value based on position of Pinky w/r/t Pacman"""
        if up_down_part and abs(up_down_part) > 5:
            y = up_down_part/abs(up_down_part)
        else:
            y = 0
        if left_right_part and abs(left_right_part) > 5:
            x = left_right_part/abs(left_right_part)
        else:
            x = 0
        self.looking = (x, y)
