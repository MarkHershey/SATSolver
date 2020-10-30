# 2-satisfiable Problem 

Linear time algorithms for finding the strongly connected components:

- Tarjan's
- Path-based 
- Kosaraju's

Necessary and sufficient condition: **A 2-CNF formula is satisfiable if and only if there is no variable that belongs to the same strongly connected component as its negation.**

Solution: 
simply perform a strong connectivity analysis on the implication graph and check that each variable and its negation belong to different components.

1. Construct the implication graph
2. strong connectivity analysis (find its strongly connected components)
3. Check whether any strongly connected component contains both a variable and its negation. If so, report that the instance is not satisfiable and halt.
4. finding a satisfying assignment
    - Construct the condensation of the implication graph, a smaller graph that has one vertex for each strongly connected component, and an edge from component i to component j whenever the implication graph contains an edge uv such that u belongs to component i and v belongs to component j. The condensation is automatically a directed acyclic graph and, like the implication graph from which it was formed, it is skew-symmetric.
    - Topologically order the vertices of the condensation.
    - For each component in the reverse topological order, if its variables do not already have truth assignments, set all the literals in the component to be true. This also causes all of the literals in the complementary component to be set to false.



### Kosaraju's

primitive graph operations

- enumerate the vertices of the graph
- store data per vertex (if not in the graph data structure itself, then in some table that can use vertices as indices)
- enumerate the out-neighbours of a vertex
- enumerate the in-neighbours of a vertex
- an ordered list L of graph vertices, that will grow to contain each vertex once.