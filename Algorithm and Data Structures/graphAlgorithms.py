class Edge:
    """
    Edge class to represent an edge in a graph
    """

    def __init__(self, u: int, v: int, w: int) -> None:
        """
        Constructor to create an Edge object that links two vertices
        :param u: vertex on one end
        :param v: vertex on the other end
        :param w: weight of the edge
        :complexity: O(1)
        """
        self.u = u
        self.v = v
        self.w = w

    def __str__(self) -> str:
        """
        Method to print out details of the Edge object
        :return: details of the Edge object
        :complexity: O(1)
        """
        return str(self.u) + "," + str(self.v) + "," + str(self.w)


class Vertex:
    """
    Vertex class to represent a vertex in a graph
    """

    def __init__(self, vertex_id: int) -> None:
        """
        Constructor to create a Vertex object
        :param vertex_id: unique id to represent a vertex
        :complexity: O(1)
        """
        self.id = vertex_id
        self.edges = []

        self.discovered = False
        self.distance = float('inf')
        self.route = []

        self.value = float('-inf')
        self.current_liquid = float('-inf')

    def add_edge(self, edge: Edge) -> None:
        """
        Method to add an Edge between two vertices to link them together
        :param edge: the Edge object that links them together
        :complexity: O(1)
        """
        self.edges.append(edge)

    def __str__(self) -> str:
        """
        Method to print out the details of a vertex
        :return: details of a vertex
        """
        return "Vertex " + str(self.id)


class Graph:
    """
    Graph class to represent a graph
    """

    def __init__(self, vertex_count: int) -> None:
        """
        Constructor to create a Graph object
        :param vertex_count: number of vertices in the graph
        :complexity: O(V) where V is the number of vertices in the graph
        """
        self.vertices = [None] * vertex_count
        self.delivery = False
        self.next_value = [float('-inf')] * vertex_count
        self.next_liquid = [None] * vertex_count
        self.max = float('-inf')
        for i in range(vertex_count):
            self.vertices[i] = Vertex(i)

    def add_edges(self, argv_edges: list, directed: bool = True) -> None:
        """
        Method to add the list of Edges into the graph
        :param argv_edges: list containing all the Edges
        :param directed: boolean to check if graph is directed, if graph is undirected, create edges both ways
        :complexity: O(n) where n is the length of argv_edges
        """
        for edge in argv_edges:
            u = edge[0]
            v = edge[1]
            w = edge[2]
            current_edge = Edge(u, v, w)
            current_vertex = self.vertices[u]
            current_vertex.add_edge(current_edge)

            if not directed:
                current_edge = Edge(v, u, w)
                current_vertex = self.vertices[v]
                current_vertex.add_edge(current_edge)

    def opt_delivery(self, n: int, roads: list, start: Vertex, end: Vertex, delivery: list) -> tuple:
        """
        Method to find a route with the least cost travelling from one city to another
        :param n: total number of cities
        :param roads: list containing roads that connects the cities
        :param start: starting city
        :param end: destination city
        :param delivery: list containing pickup city, delivery city and the amount of money that will be made
        :return: a tuple that contains the cost of travelling from start to end and a list containing the route
        :complexity: O(Rlog(N)) where R is the total number of roads,
                                      N is the total number of cities
        """
        # initialisation
        discovered = Heap()
        self.add_edges(roads, False)
        self.vertices[start].distance = 0
        self.vertices[start].route = [self.vertices[start].id]
        discovered.add(self.vertices[start])

        # while there are still vertices in the min heap
        while len(discovered.array) > 1:

            # pop the smallest element from the min heap
            u = discovered.pop()
            u.discovered = True

            # loop through all edges in the current vertex
            for edge in u.edges:

                v = self.vertices[edge.v]

                # check if the current route fulfills the delivery
                if not self.delivery and delivery[0] in u.route and delivery[1] == v.id:
                    delivery_edge = edge.w - delivery[2]
                    self.delivery = True
                else:
                    delivery_edge = edge.w

                # dijkstra algorithm to find the path with the least cost
                if not v.discovered:

                    v.distance = u.distance + delivery_edge
                    v.route = u.route[:]
                    v.route.append(v.id)

                    discovered.add(v)
                    v.discovered = True

                # else if a vertex is updated, push it back into the heap
                else:

                    if v.distance > u.distance + delivery_edge:
                        v.distance = u.distance + delivery_edge
                        v.route = u.route[:]
                        v.route.append(v.id)

                        discovered.add(v)

        return self.vertices[end].distance, self.vertices[end].route

    def best_trades(self, argv_prices: list, argv_starting_liquid: int, argv_max_trades: int,
                    argv_townspeople: list) -> int:
        """
        Method to return the best way to trade with the local people and maximise the value of the provided liquid
        :param argv_prices: list containing prices of each type of liquid
        :param argv_starting_liquid: provided starting liquid
        :param argv_max_trades: maximum number of trades allowed
        :param argv_townspeople: list of lists containing the trades offered by the towns people
        :return: best value after performing at most argv_max_trades trades
        :complexity: O(TM) where T is the total number of trades available,
                                 M maximum number of trades allowed
        """
        # initialisation
        self.add_edges(self.get_edges(argv_townspeople), True)
        self.vertices[argv_starting_liquid].current_liquid = 1
        self.vertices[argv_starting_liquid].value = self.vertices[argv_starting_liquid].current_liquid * argv_prices[
            argv_starting_liquid]
        self.vertices[argv_starting_liquid].discovered = True
        self.next_value[argv_starting_liquid] = self.vertices[argv_starting_liquid].value
        self.next_liquid[argv_starting_liquid] = self.vertices[argv_starting_liquid].current_liquid
        discovered = [self.vertices[argv_starting_liquid]]

        # if no trades are allowed, return the value of the current liquid
        if argv_max_trades == 0:
            return self.vertices[argv_starting_liquid].value

        # run bellman-ford algorithm for argv_max_trades number of times
        for _ in range(0, argv_max_trades):

            # loop through all vertices and their edges in the graph
            for vertex in discovered:
                u = vertex
                for edge in vertex.edges:
                    v = self.vertices[edge.v]

                    # optimisation mechanism to not unnecessarily run the algorithm on undiscovered vertex
                    if not v.discovered:
                        v.discovered = True
                        discovered.append(v)

                    # if the trade provides a better value, store the value and volume of the liquid
                    if u.current_liquid * edge.w * argv_prices[edge.v] > v.value:
                        if u.current_liquid * edge.w * argv_prices[edge.v] > self.next_value[v.id]:
                            self.next_value[v.id] = u.current_liquid * edge.w * argv_prices[edge.v]
                            self.next_liquid[v.id] = u.current_liquid * edge.w

                    # if the vertex is discovered for the first time, initialise the values
                    elif self.next_value[v.id] == float('-inf') and self.next_liquid[v.id] is None:
                        self.next_value[v.id] = v.value
                        self.next_liquid[v.id] = v.current_liquid

            # save the stored best values and volume of liquid into each vertices
            for vertex_id in range(0, len(self.next_liquid)):
                self.vertices[vertex_id].current_liquid = self.next_liquid[vertex_id]
                self.vertices[vertex_id].value = self.next_value[vertex_id]

                # store the vertex with the best value overall
                if self.vertices[vertex_id].value > self.max:
                    self.max = self.vertices[vertex_id].value

        return self.max

    def get_edges(self, argv_list: list) -> list:
        """
        Method to preprocess argv_townspeople to turn it into a simple list
        :param argv_list: list of lists containing the trades offered by the towns people
        :return: list containing the trades offered by the towns people
        :complexity: O(n) where n is the number of elements in argv_list
        """
        output = []
        for x in argv_list:
            for y in x:
                output.append(y)
        return output


class Heap:
    """
    Heap class that represents the min heap data type
    """
    def __init__(self) -> None:
        """
        Constructor to create a Heap object
        :complexity: O(1)
        """
        self.array = [None]

    def rise(self, k: int) -> None:
        """
        Rise element at index k to its correct position
        :complexity: O(logN) where N is the number of elements in the heap
        """
        while k > 1 and self.array[k].distance < self.array[k // 2].distance:
            self.swap(k, k // 2)
            k = k // 2

    def add(self, element) -> bool:
        """
        Swaps elements while rising
        :complexity: O(logN) where N is the number of elements in the heap
        """
        self.array.append(element)
        self.rise(len(self.array) - 1)

    def smallest_child(self, k):
        """
        Returns the index of the smallest child of k.
        :complexity: O(1)
        """
        if 2 * k == len(self.array) - 1 or self.array[2 * k].distance < self.array[2 * k + 1].distance:
            return 2 * k
        else:
            return 2 * k + 1

    def sink(self, k: int) -> None:
        """
        Make the element at index k sink to the correct position
        :complexity: O(logN) where N is the number of elements in the heap
        """
        while 2 * k <= len(self.array) - 1:
            child = self.smallest_child(k)
            if self.array[k].distance <= self.array[child].distance:
                break
            self.swap(child, k)
            k = child

    def pop(self):
        """
        Method to return the smallest element in the heap
        :complexity: O(1)
        :return: smallest element in the heap
        """
        self.swap(1, len(self.array) - 1)
        output = self.array[-1]
        output_distance = output.distance
        self.array[-1].distance = float('inf')
        self.sink(1)
        self.array.pop()
        output.distance = output_distance
        return output

    def swap(self, x, y):
        """
        Method to swap the position of two elements
        :complexity: O(1)
        """
        self.array[x], self.array[y] = self.array[y], self.array[x]


def opt_delivery(n: int, roads: list, start: Vertex, end: Vertex, delivery: list):
    """
    Function to find a route with the least cost travelling from one city to another
    :param n: total number of cities
    :param roads: list containing roads that connects the cities
    :param start: starting city
    :param end: destination city
    :param delivery: list containing pickup city, delivery city and the amount of money that will be made
    :return: a tuple that contains the cost of travelling from start to end and a list containing the route
    :complexity: O(Rlog(N)) where R is the total number of roads,
                                  N is the total number of cities
    """
    graph = Graph(n)
    return graph.opt_delivery(n, roads, start, end, delivery)


def best_trades(prices, starting_liquid, max_trades, townspeople):
    """
    Method to return the best way to trade with the local people and maximise the value of the provided liquid
    :param prices: list containing prices of each type of liquid
    :param starting_liquid: provided starting liquid
    :param max_trades: maximum number of trades allowed
    :param townspeople: list of lists containing the trades offered by the towns people
    :return: best value after performing at most max_trades trades
    :complexity: O(TM) where T is the total number of trades available,
                             M maximum number of trades allowed
    """

    graph = Graph(len(prices))
    return graph.best_trades(prices, starting_liquid, max_trades, townspeople)

