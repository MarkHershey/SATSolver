from dgraph import DirectedGraph
from typing import Dict, List, Tuple


def get_strongly_connected_components(graph: DirectedGraph) -> dict:
    """ Kosaraju's algorithm """
    scc_assignment = {}
    visited = []
    L: List = []

    def visit(graph: DirectedGraph, vertex, visited, L):
        if vertex not in visited:
            visited.append(vertex)
            for i in graph.out_neighbors(vertex):
                visit(graph, i, visited, L)
            L.insert(0, vertex)

    def assign(graph: DirectedGraph, scc_assignment: dict, vertex, root):
        if vertex not in scc_assignment:
            # assign vertex to the root of a strongly_connected_component
            scc_assignment[vertex] = root

            for in_neighbor in graph.in_neighbors(vertex):
                assign(graph, scc_assignment, in_neighbor, root)

    for vertex in graph.all_vertices():
        visit(graph, vertex, visited, L)

    for vertex in L:
        assign(graph, scc_assignment, vertex, vertex)

    return scc_assignment
