from typing import Dict, List, Tuple

from dgraph import DirectedGraph


Vertex = int


def get_strongly_connected_components(graph: DirectedGraph) -> Dict[Vertex, Vertex]:
    """
    Kosaraju's algorithm, time complexity: O(n)

    Args:
        graph: A DirectedGraph object

    Returns:
        a dictionary that maps each vertex to the root vertex of its strongly connected component
    """
    # key: vertex, value: the root vertex of a strongly connected component
    scc_assignment: Dict[Vertex, Vertex] = {}
    visited = []
    L: List = []

    if not isinstance(graph, DirectedGraph):
        return

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

    # O(n), n = number of vertices
    for vertex in graph.all_vertices():
        visit(graph, vertex, visited, L)

    # O(n), n = number of vertices
    for vertex in L:
        assign(graph, scc_assignment, vertex, vertex)

    return scc_assignment
