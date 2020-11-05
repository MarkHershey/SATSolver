# 2-Satisfiable Problem 

## Background Knowledge

- [Boolean satisfiability problem](https://en.wikipedia.org/wiki/Boolean_satisfiability_problem)
    - [Conjunctive Normal Form (CNF)](https://en.wikipedia.org/wiki/Conjunctive_normal_form)
    - [2-SAT problem](https://en.wikipedia.org/wiki/2-satisfiability) is [NL-complete](https://en.wikipedia.org/wiki/NL-complete)
    - [3-SAT](https://en.wikipedia.org/wiki/Boolean_satisfiability_problem#3-satisfiability) is [NP-complete](https://en.wikipedia.org/wiki/NP-completeness)
    - [Implication graph](https://en.wikipedia.org/wiki/Implication_graph) for solving 2-SAT
- [Graph Theory](https://en.wikipedia.org/wiki/Graph_theory)
    - [Directed Graph](https://en.wikipedia.org/wiki/Directed_graph)
    - [DFS](https://en.wikipedia.org/wiki/Depth-first_search)-based linear-time algorithms for strong connectivity analysis
        - [Tarjan's strongly connected components algorithm](https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm)
        - [Path-based strong component algorithm](https://en.wikipedia.org/wiki/Path-based_strong_component_algorithm)
        - [Kosaraju's algorithm](https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm)
    - [Strongly Connected Components (SCC)](https://en.wikipedia.org/wiki/Strongly_connected_component#Definitions)
    - Graph Condensation based on SCC produces a [Directed Acyclic Graph (DAG)](https://en.wikipedia.org/wiki/Directed_acyclic_graph)
        - [Building condensation graph](https://cp-algorithms.com/graph/strongly-connected-components.html)
    - [Topological Sorting](https://en.wikipedia.org/wiki/Topological_sorting) can be done on a DAG.
- [Time Complexity for Python Operations](https://wiki.python.org/moin/TimeComplexity)

## Implementation Details

1. Parse CNF file
2. Construct Implication Graph using _special_ Directed Graph
3. Perform Strong Connectivity Analysis on the Implication Graph using BFS two times (Kosaraju's algorithm)
4. Check satisfiability
    - Necessary and sufficient condition: **A 2-SAT CNF formula is satisfiable if and only if there is no variable that belongs to the same strongly connected component as its negation.**
5. Get a satisfiable truth assignment
    1. Construct the condensation graph (G^SCC) based on Strongly Connected Components (SCC). 
        - A smaller graph that has one vertex for each strongly connected component, and an edge from component i to component j whenever the implication graph contains an edge uv such that u belongs to component i and v belongs to component j.
    2. Topologically order the vertices of the condensation graph (G^SCC).
    3. Reverse traversal of the topological ordered vertices (which is the root vertex of all strongly connected components). Set all the literals in the component to be `True`, and all the negation of the literals to be `False`.


#### Directed Graph operations used by Kosaraju's Algorithm

- enumerate the vertices of the graph
- store data per vertex
- enumerate the out-neighbors of a vertex
- enumerate the in-neighbors of a vertex
- an ordered list L of graph vertices, that will grow to contain each vertex once.

## Randomizing Algorithm 2-SAT Solver

https://people.seas.harvard.edu/~cs125/fall14/lec19.pdf