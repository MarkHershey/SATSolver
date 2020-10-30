class DirectedGraph:
    def __init__(self):
        self.graph = {}

    def add_edge(self, vertex_a, vertex_b):
        # print(f"Adding {vertex_a} -> {vertex_b}")
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
        return list(self.graph.keys())

    def out_neighbors(self, vertex) -> list:
        if self.graph.get(vertex):
            return self.graph[vertex]["out"]
        else:
            return []

    def in_neighbors(self, vertex) -> list:
        if self.graph.get(vertex):
            return self.graph[vertex]["in"]
        else:
            return []

    def topological_sort(self) -> list:
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
