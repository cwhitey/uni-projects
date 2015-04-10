
class TuringTape:
    """This class represents a Turing Machine tape.

    The main motivation for creating this class was the encapsulate the complexity of moving the tape left or right,
    and constructing a string representation of the tape.

    In future, this class could also hold a turing machine's alphabet, and check the validity of each symbol written
    to the tape."""

    tape = []
    head_index = 0
    blank = None

    def __init__(self, blank, initial_tape=None, start_index=0):
        if initial_tape is None:
            self.tape = [blank]
        else:
            self.tape = initial_tape
        self.blank = blank
        self.head_index = start_index

    def get_blank(self):
        """Get the blank symbol"""
        return self.blank

    def move_left(self):
        """Move head left. Move to new blank element if we are at the 'first' symbol of the tape"""
        if self.head_index == 0:
            self.tape.insert(0, self.blank)
        else:
            self.head_index -= 1

    def move_right(self):
        """Move head right. Move to new blank element if we are at the 'last' symbol of the tape"""
        if self.head_index == (len(self.tape) - 1):
            self.tape.append(self.blank)
        self.head_index += 1

    def write(self, symbol):
        """Write symbol at current head position"""
        self.tape[self.head_index] = symbol

    def read(self):
        """Read what is at the current head position"""
        return self.tape[self.head_index]

    def to_string(self):
        """Return a string representation of the tape"""
        # return ["-> " + self.tape[i] if i == self.head_index else self.tape[i] for i in range(0, len(self.tape))]
        tape_string = ""
        for i in range(0, len(self.tape)):
            if i == self.head_index:
                tape_string += "*" + self.tape[i] + "*"
            else:
                tape_string += self.tape[i]
        return tape_string