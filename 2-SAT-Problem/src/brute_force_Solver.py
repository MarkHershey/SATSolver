from typing import List, Tuple, Dict
import random
from cnf_parser import parse_cnf_to_list


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


def randomize_solve(cnf: str):
    truth_assignment = {}
    formula: List[Tuple] = parse_cnf_to_list(cnf)

    # initialise truth assignment by setting all variable to false
    for clause in formula:
        for literal in clause:
            if literal not in truth_assignment:
                if literal > 0:
                    truth_assignment[literal] = 0
                    truth_assignment[-literal] = 1
                else:
                    truth_assignment[literal] = 1
                    truth_assignment[-literal] = 0

    while True:
        picked_literal = check_assignment(formula, truth_assignment)
        if picked_literal == None:
            break
        else:
            flip_assignment(truth_assignment, picked_literal)

    solution = [
        truth_assignment[i] for i in sorted(list(truth_assignment.keys())) if i > 0
    ]
    print(solution)
    return solution


if __name__ == "__main__":
    randomize_solve("/home/mark/CODE/fuckSATSolver/2-SAT-Problem/src/sample.cnf")
