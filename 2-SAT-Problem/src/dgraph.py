from typing import List

Vertex = int


class DirectedGraph:
    def __init__(self):
        self.graph = {}

    def add_edge(self, vertex_a: Vertex, vertex_b: Vertex):
        """
        Add new edge to graph, and keep track of in-neighbor edges for each vertex.
        Time complexity: O(n), n = number of edges of vertex_a

        Handles:
            new vertices
            duplicated edges
        """
        if self.graph.get(vertex_a):
            if vertex_b not in self.graph[vertex_a]["out"]:
                self.graph[vertex_a]["out"].append(vertex_b)
        else:
            self.graph[vertex_a] = {"out": [vertex_b], "in": []}

        if self.graph.get(vertex_b):
            if vertex_a not in self.graph[vertex_b]["in"]:
                self.graph[vertex_b]["in"].append(vertex_a)
        else:
            self.graph[vertex_b] = {"out": [], "in": [vertex_a]}

    def all_vertices(self):
        """ O(1) """
        return list(self.graph.keys())

    def out_neighbors(self, vertex: Vertex) -> List[Vertex]:
        """
        Get all outwards edges of the given vertex.
        Time complexity: O(1)

        Returns:
            A list of vertices pointed by the outwards edges.
        """
        if self.graph.get(vertex):
            return self.graph[vertex]["out"]
        else:
            return []

    def in_neighbors(self, vertex: Vertex) -> List[Vertex]:
        """
        Get all inwards edges of the given vertex.
        Time complexity: O(1)

        Returns:
            A list of vertices that has edge pointing to the given vertex.
        """
        if self.graph.get(vertex):
            return self.graph[vertex]["in"]
        else:
            return []

    def topological_sort(self) -> List[Vertex]:
        """ 
        Topological ordering based on DFS assuming the graph is a DAG.

        Returns:
            A list of vertices in topological order.
        """
        unvisited = list(self.graph.keys())
        order = []

        def visit(graph: self, vertex, unvisited, order):
            for out_neighbor in graph.out_neighbors(vertex):
                if out_neighbor in unvisited:
                    visit(graph, out_neighbor, unvisited, order)
            unvisited.remove(vertex)
            order.insert(0, vertex)

        while len(unvisited) != 0:
            start_vertex = unvisited[0]
            visit(self, start_vertex, unvisited, order)

        return order
