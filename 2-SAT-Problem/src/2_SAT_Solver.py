from cnf_parser import construct_implication_graph
from dgraph import DirectedGraph
from kosaraju import get_strongly_connected_components
from typing import List, Dict


def solve(cnf: str):
    graph: DirectedGraph = construct_implication_graph(cnf)
    scc_assignment: Dict = get_strongly_connected_components(graph)
    scc = {}

    for vertex in scc_assignment.keys():
        root = scc_assignment[vertex]
        if root in scc:
            scc[root].append(vertex)
        else:
            scc[root] = [vertex]

        if -vertex in scc[root]:
            print("UNSATISFIABLE")
            return

    print("SATISFIABLE")
    condensed_graph = DirectedGraph()
    for vertex in scc_assignment.keys():
        self_root = scc_assignment[vertex]
        if self_root == vertex:
            continue
        for out_neighbor in graph.out_neighbors(vertex):
            out_root = scc_assignment[out_neighbor]
            if self_root == out_root:
                continue
            condensed_graph.add_edge(self_root, out_root)

    reversed_topological_order: List = condensed_graph.topological_sort()[::-1]

    truth_assignment = {}
    for root in reversed_topological_order:
        if root not in truth_assignment:
            for literal in scc[root]:
                truth_assignment[literal] = 1
                truth_assignment[-literal] = 0

    solution = [
        truth_assignment[i] for i in sorted(list(truth_assignment.keys())) if i > 0
    ]
    print(solution)
    return solution


if __name__ == "__main__":
    import sys

    sys.setrecursionlimit(10000)

    solve("/home/mark/CODE/fuckSATSolver/2-SAT-Problem/src/sample.cnf")
