from game_controller import GameController
import time
import const


def setup():
    size(const.BOARD_SIZE, const.BOARD_SIZE+const.TEXT_SIZE*2)
    colorMode(RGB, 1)


gc = GameController(const.BOARD_SIZE, const.NUM_OF_SQUARE)


def draw():
    background(0)
    gc.update()
    time_diff = time.time()-gc.player_finish_time
    if (gc.curr_color == const.WHITE) and (time_diff > const.COMPUTER_TIMEOUT):
        gc.computer_make_move()


def mouseClicked(event):
    gc.player_make_move(mouseX, mouseY)
