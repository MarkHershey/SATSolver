from pathlib import Path
from typing import List, Tuple

from dgraph import DirectedGraph

Literal = int
Clause = Tuple[Literal]


def construct_implication_graph(cnf: str) -> DirectedGraph:
    cnf = Path(cnf)
    assert cnf.is_file()

    formula: List[Clause] = parse_cnf_to_list(cnf)

    implication_graph = DirectedGraph()
    for clause in formula:
        vertex_a, vertex_b = clause
        implication_graph.add_edge(-vertex_a, vertex_b)
        implication_graph.add_edge(-vertex_b, vertex_a)

    return implication_graph


def parse_cnf_to_list(cnf: str) -> List[Clause]:
    cnf = Path(cnf)
    assert cnf.is_file()
    with cnf.open() as f:
        content = f.readlines()

    formula: List[Clause] = []
    clause = []
    for line in content:
        line = line.strip()
        if line.startswith("c"):
            continue
        if line.startswith("p"):
            continue

        tokens = line.split()
        for token in tokens:
            token = token.strip()
            if token != "":
                literal = int(token)
            else:
                continue

            if literal == 0:
                if len(clause) == 2:
                    formula.append(tuple(clause))
                    clause = []
                else:
                    print(f"WARNING: caluse size != 2; Error clause: {clause}")
            else:
                clause.append(literal)

    return formula


if __name__ == "__main__":
    from pprint import pprint

    g = construct_implication_graph("CNFs/sample_simple_SAT.cnf")
    pprint(g.graph)
