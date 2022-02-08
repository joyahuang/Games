from player_human import Human
from player_computer import Computer
from board import Board
import time
import const
import re


class GameController:
    def __init__(self, BOARD_SIZE, NUM_OF_SQUARE):
        self.message = ""  # results, warnings
        self.status = ""  # next color, current score
        self.board = Board(NUM_OF_SQUARE, BOARD_SIZE)
        self.human = Human(const.BLACK, self.board)
        self.computer = Computer(const.WHITE, self.board)
        self.player_finish_time = 0  # record player move finished time
        self.curr_color = self.human.color  # default human go first
        self.has_ended = False  # default has not ended

    def colorname(self):
        """
        color number to color name
        :return:
        """
        return "Black" if self.curr_color == const.BLACK else "White"

    def switch_player(self):
        """
        switch between black and white
        change the status and message
        :return:
        """
        self.curr_color = 1-self.curr_color
        self.status = ("Next: "+self.colorname()+", Black " +
                       str(self.board.bnum) + ", White "+str(self.board.wnum))
        self.message = ""

    def player_make_move(self, x, y):
        """
        human make a move
        :param x: x coordinate of user mouse click
        :param y: y coordinate of user mouse click
        :return:
        """
        if self.curr_color == self.computer.color:
            # if it's computer's turn and user clicks
            self.message = "Not your turn!"
            return
        elif self.human.make_move(x, y):
            # if human player has successfully make a move
            self.player_finish_time = time.time()
            self.switch_player()
        else:
            # if human player clicks on an occupied square
            self.message = "Invalid move!"

    def computer_make_move(self):
        """
        computer make a move
        :return:
        """
        if self.computer.make_move():
            self.switch_player()

    def display(self):
        """
        display the text and board
        :return:
        """
        fill(const.WHITE)
        textSize(const.TEXT_SIZE)
        text(self.status, 0, const.BOARD_SIZE+const.TEXT_SIZE*2)
        text(self.message, 0, const.BOARD_SIZE+const.TEXT_SIZE)
        self.board.display()

    def judge_winner(self):
        """
        count the number of black and white tiles
        :return: winner
        """
        if self.board.wnum > self.board.bnum:
            return "Computer"
        elif self.board.wnum < self.board.bnum:
            return "You"
        return "TIE"

    def is_over(self):
        """
        judge if there are still legal moves
        :return:
        """
        if self.has_ended:
            # if has ended, prompt save score
            self.save_score()
            return
        if (not self.human.has_legal() and
                not self.computer.has_legal()):
            # game is over if there is no legal moves for both parties
            winner = self.judge_winner()
            self.message = "Game Over! Winner is "+winner
            self.has_ended = True
        elif (self.curr_color == self.human.color and
              not self.human.has_legal()):
            # human has no legal move, change to computer
            self.message = "You don't have a valid move"
            self.switch_player()
        elif (self.curr_color == self.computer.color and
              not self.computer.has_legal()):
            # computer has no legal move, change to human
            self.message = "Computer doesn't have a valid move"
            self.switch_player()

    def save_score(self):
        """
        final step of game: read old scores and save new score
        :return:
        """
        name = self.input("Please enter your name")
        with open('./scores.txt', 'r') as f:
            old_scores = f.read()
        maxscore = self.find_maxscore(old_scores)
        if self.board.bnum > maxscore:
            with open('./scores.txt', 'w+') as f:
                f.write(name+" "+str(self.board.bnum)+"\n")
        else:
            with open('./scores.txt', 'a+') as f:
                f.write(name+" "+str(self.board.bnum)+"\n")
        exit()

    def find_maxscore(self, old_scores):
        """
        find the max score of old scores
        :param old_scores: old records on scores.txt
        :return: highest score of old records
        """
        maxscore = 0
        for score in re.findall(r'(\d+)\n', old_scores):
            maxscore = max(int(score), maxscore)
        return maxscore

    def input(self, message=''):
        from javax.swing import JOptionPane
        return JOptionPane.showInputDialog(frame, message)

    def update(self):
        """
        in every update: judge if the game is over and display graph
        :return:
        """
        self.is_over()
        self.display()
