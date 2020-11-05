from typing import List, Dict

from dgraph import DirectedGraph
from cnf_parser import construct_implication_graph
from kosaraju import get_strongly_connected_components

from markkk.time import timeitprint

Vertex = int
StronglyConnectedComponent = List[Vertex]

@timeitprint
def solve(cnf: str) -> List[bool]:
    """
    Linear-time 2-SAT Solver

    Args:
        cnf: a filepath that leads to a 2-SAT CNF file

    Returns:
        None if CNF is not satisfiable, or
        A satisfiable solution represented by boolean truth assignment in sequence [x1, x2, x3, ..., xn].
    """
    # parsing CNF file costs O(n)
    # constructing implication graph costs O(n)
    graph: DirectedGraph = construct_implication_graph(cnf)
    scc_assignment: Dict[Vertex, Vertex] = get_strongly_connected_components(graph)
    scc: Dict[Vertex, StronglyConnectedComponent] = {}

    # Checking if satisfiable costs O(n)
    for vertex in scc_assignment.keys():
        root = scc_assignment[vertex]
        if root in scc:
            scc[root].append(vertex)
        else:
            scc[root] = [vertex]

        if -vertex in scc[root]:
            print("UNSATISFIABLE")
            return

    # At this point, we know that the 2-SAT is satisfiable
    print("SATISFIABLE")

    # To find a satisfiable truth assignment
    # Building condensation graph based on strongly connected components
    # costs O(n)
    condensed_graph = DirectedGraph()
    for vertex in scc_assignment.keys():
        self_root = scc_assignment[vertex]
        for out_neighbor in graph.out_neighbors(vertex):
            out_root = scc_assignment[out_neighbor]
            if self_root == out_root:
                continue
            condensed_graph.add_edge(self_root, out_root)

    # Topologically sorting costs O(n)
    reversed_topological_order: List = condensed_graph.topological_sort()[::-1]

    # Create truth assignment costs O(n)
    truth_assignment = {}
    for root in reversed_topological_order:
        if root not in truth_assignment:
            for literal in scc[root]:
                truth_assignment[literal] = 1
                truth_assignment[-literal] = 0

    # Construct solution in order costs O(n)
    solution = [
        truth_assignment[i] for i in sorted(list(truth_assignment.keys())) if i > 0
    ]

    # print(solution)
    return solution


if __name__ == "__main__":
    import sys

    sys.setrecursionlimit(10000)

    solve("CNFs/sample_1k_SAT.cnf")
    solve("CNFs/sample_1k_UNSAT.cnf")
