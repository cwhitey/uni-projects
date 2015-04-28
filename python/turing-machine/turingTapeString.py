from turingTape import TuringTape


class TuringTapeString(TuringTape):

    def __init__(self, blank, initial_tape=None, start_index=0):
        # print "InitialTape: " + ''.join(initial_tape)
        if initial_tape is None:
            self.tape = blank
        else:
            self.tape = ''.join(initial_tape)
        self.blank = blank
        self.head_index = start_index

    def move_left(self):
        if self.head_index == 0:
            self.tape = self.blank + self.tape
        else:
            self.head_index -= 1

    def move_right(self):
        if self.head_index == (len(self.tape) - 1):
            self.tape = self.tape + self.blank
        self.head_index += 1

    def write(self, symbol):
        self.tape = self.tape[:self.head_index] + symbol + self.tape[self.head_index + 1:]

    def read(self):
        # print self.tape
        # print self.head_index
        return self.tape[self.head_index]
