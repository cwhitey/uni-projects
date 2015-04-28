import xml.etree.ElementTree as ET
from turingTape import TuringTape
from turingTapeDeque import TuringTapeDeque
from turingTapeString import TuringTapeString


def print_attributes(initial_state, tape):
    """Format and print Turing Machine attributes"""
    print "initial state=", initial_state
    print "initial tape=", tape.to_string()


def print_state_transition(transition):
    """NOT IN USE
    Format and print attributes of the transition"""
    print "\tseensym: ", transition.get("seensym")
    print "\twritesym: ", transition.get("writesym")
    print "\tnewstate: ", transition.get("newstate")
    print "\tmove: ", transition.get("move"), "\n"


def print_state_attributes(state, step_no, tape):
    """Format and print attributes of the state"""
    print "steps = ", step_no
    print "state = ", state.get("name")
    print "tape = ", tape.to_string()


def is_final(state, final_states):
    """Check if state is one of the final states"""
    return state.get("name") in final_states


def get_state(name, states):
    """Filter out all states except the one matching 'name'
    Returns None if it could not fine the requested state."""
    result = filter(lambda x: x.get("name") == name, states)
    if len(result) == 0:
        result = None
    else:
        result = result[0]
    return result


def match_on_transition(head, transitions):
    """Filter out anything except the transition matching the head of the tape"""
    return filter(lambda x: x.get("seensym") == head, transitions)[0]


def apply_transition(transition, tape):
    """Apply transition details to tape"""
    tape.write(transition.get("writesym"))
    if transition.get("move") == "L":
        tape.move_left()
    else:
        tape.move_right()
    return transition.get("newstate")


def run_states(states, tape, final_states, initial_state, silence=False):
    """Starting from initial_state, run through to transitions across the states, manipulating the tape as we go.
    Will halt with answer 'no' if it could not find a state referenced in the Turing Machine definition."""
    state = get_state(initial_state, states)
    step_no = 0
    answer = "yes"
    while (answer == "yes") and (not is_final(state, final_states)):
        transition = match_on_transition(tape.read(), state)
        state = get_state(apply_transition(transition, tape), states)
        if state is None:
            answer = "no"
        else:
            step_no += 1
            if not silence:
                print_state_attributes(state, step_no, tape)
                print ""
    print "halted with answer", answer
    print "machine state at halt: "
    print_state_attributes(state,step_no,tape)


def run_turing(filename, silence=False, tapeType="builtin"):
    """Parse xml file 'filename' definition of turing machine, and simulate it"""
    tree = ET.parse(open(filename))
    root = tree.getroot()
    blank = ""
    initial_state = 0
    alphabet, initial_tape, final_states = [], [], []
    state_root, tape = None, None
    for child in root:
        tag = child.tag
        if tag == "alphabet":
            alphabet = list(child.text)
        if tag == "initialtape":
            initial_tape = list(child.text)
        if tag == "blank":
            blank = child.get("char")
        if tag == "initialstate":
            initial_state = child.get("name")
        if tag == "finalstates":
            for state_child in child:
                final_states.append(state_child.get("name"))
        if tag == "states":
            state_root = child
    if tapeType == "deque":
        tape = TuringTapeDeque(blank, initial_tape)
    elif tapeType == "string":
        tape = TuringTapeString(blank, initial_tape)
    else:
        tape = TuringTape(blank, initial_tape)
    if not silence:
        print_attributes(initial_state, tape)
        print
    run_states(state_root, tape, final_states, initial_state, silence)
