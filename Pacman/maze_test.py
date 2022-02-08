from maze import Maze
from game_controller import GameController


def test_constructor():
    g = GameController(600, 400)
    m = Maze(600, 400, 150, 450,
             100, 300, g)
    assert m.LEFT_VERT == 150
    assert m.RIGHT_VERT == 450
    assert m.TOP_HORIZ == 100
    assert m.BOTTOM_HORIZ == 300
    assert m.WIDTH == 600
    assert m.HEIGHT == 400
    assert m.gc is g
    assert m.dots.dots_left() == ((m.dots.WIDTH//m.dots.SPACING + 1) * 2 +
                                  (m.dots.HEIGHT//m.dots.SPACING + 1) * 2)


def test_eat_dots():
    g = GameController(600, 600)
    m = Maze(600, 600, 150, 450,
             150, 450, g)
    # eate an intersection dot
    m.eat_dots(450, 450)
    assert len(m.dots.bottom_row) == 8
    assert len(m.dots.right_col) == 8
    assert m.dots.dots_left() == 34
    # eat a non-intersection dot
    m.eat_dots(375, 450)
    assert len(m.dots.bottom_row) == 7
    assert len(m.dots.right_col) == 8
    assert m.dots.dots_left() == 33
