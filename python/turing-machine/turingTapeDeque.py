from turingTape import TuringTape
from collections import deque

class TuringTapeDeque(TuringTape):

    def __init__(self, blank, initial_tape=None, start_index=0):
        if initial_tape is None:
            self.tape = deque(blank)
        else:
            self.tape = deque(initial_tape)
        self.blank = blank
        self.head_index = start_index

    def move_left(self):
        if self.head_index == 0:
            self.tape.appendleft(self.blank)
        else:
            self.head_index -= 1
