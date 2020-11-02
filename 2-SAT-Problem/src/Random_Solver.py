import random
from typing import List, Tuple, Dict

from cnf_parser import parse_cnf_to_list

from markkk.time import timeitprint

def check_assignment(formula: List[Tuple], truth_assignment: Dict):
    for clause in formula:
        a, b = clause
        if not (truth_assignment[a] or truth_assignment[b]):
            return random.choice(clause)
    return


def flip_assignment(truth_assignment: Dict, literal):
    if truth_assignment[literal] == 0:
        truth_assignment[literal] = 1
        truth_assignment[-literal] = 0
    else:
        truth_assignment[literal] = 0
        truth_assignment[-literal] = 1

@timeitprint
def randomize_solve(cnf: str):
    number_of_variable = 0
    truth_assignment = {}
    formula: List[Tuple] = parse_cnf_to_list(cnf)

    # initialise truth assignment by setting all variable to false
    for clause in formula:
        for literal in clause:
            if literal not in truth_assignment and -literal not in truth_assignment:
                number_of_variable += 1
            if literal not in truth_assignment:
                if literal > 0:
                    truth_assignment[literal] = 0
                    truth_assignment[-literal] = 1
                else:
                    truth_assignment[literal] = 1
                    truth_assignment[-literal] = 0
    
    max_steps = 100 * number_of_variable ** 2

    while max_steps >= 0:
        max_steps -= 1
        picked_literal = check_assignment(formula, truth_assignment)
        if picked_literal == None:
            print("SATISFIABLE")
            solution = [truth_assignment[i] for i in sorted(list(truth_assignment.keys())) if i > 0]
            # print(solution)
            return solution
        else:
            flip_assignment(truth_assignment, picked_literal)

    print("UNSATISFIABLE")
    return


if __name__ == "__main__":
    randomize_solve("sample_1k_SAT.cnf")
    randomize_solve("sample_1k_UNSAT.cnf")
