class Node:
    """
    Node class that represents a node in a Prefix trie for sequenceDatabase
    """

    def __init__(self) -> None:
        """
        Constructor for Node class
        :complexity: O(1)
        """
        self.link = [None] * 5
        self.data = None
        self.count = 0
        self.max = self
        self.temp = None
        self.next = 5


class SequenceDatabase:
    """
    SequenceDatabase class to store multiple DNA sequences
    """

    def __init__(self) -> None:
        """
        Constructor for SequenceDatabase class
        :complexity: O(1)
        """
        self.root = Node()

    def addSequence(self, s: str) -> None:
        """
        Function to store s into the database represented by the instance of SequenceDatabase
        :param s: String to be stored into the database
        :return: None
        :complexity: O(n) where n is the length of s
        """
        self.addSequenceAux(self.root, s, 0)

    def addSequenceAux(self, current: Node, s: str, charIndex: int):
        """
        Auxiliary function to store s into SequenceDatabase by utilising recursion
        :param current: current Node the pointer is at
        :param s: String to be stored into the database
        :param charIndex: index of the character in s the pointer is at
        :return: recently added Node
        :complexity: O(n) where n is the length of s
        """
        # if pointer for s is inside the length of s
        if charIndex < len(s):
            arrayIndex = ord(s[charIndex]) - ord("A") + 1
            if current.link[arrayIndex] is None:
                current.link[arrayIndex] = Node()
            # call recursion on the next character, store the newly added Node in temp
            current.temp = self.addSequenceAux(current.link[arrayIndex], s, charIndex + 1)
            # if the newly added Node has the count, use the lexicographically smaller one as current.max
            if current.max.count == current.temp.count:
                if arrayIndex <= current.next:
                    current.max = current.temp
                    current.next = arrayIndex
            # if the newly added Node has higher count, use it as current.max
            elif current.max.count < current.temp.count:
                current.max = current.temp
                current.next = arrayIndex
            # return current.max for other calls to compare with their current.max
            return current.max

        # end condition for recursion
        else:
            # increment counter for each addition of the data
            if current.link[0] is None:
                current.link[0] = Node()
            current.link[0].data = s
            current.link[0].count += 1
            # update max
            if current.link[0].count >= current.max.count:
                current.max = current.link[0]
                current.next = 0
            return current.link[0]

    def query(self, q: str) -> str:
        """
        Function to return a String that has q as a prefix and have a higher frequency in the database than any other
        string with q as a prefix. If two or more strings with prefix q are tied for most frequent,
        return the lexicographically least of them
        :param q: prefix of a String given
        :return: String that has q as a prefix and have a higher frequency in the database than any other
                 string with q as a prefix
        :complexity: O(n) where n is the length of q
        """
        return self.queryAux(self.root, q, 0)

    def queryAux(self, current: Node, s: str, charIndex: int) -> str:
        """
        Auxiliary function to return a String that has q as a prefix and have a higher frequency in the database than
        any other string with q as a prefix by using recursion
        :param current: current Node the pointer is at
        :param s: prefix of a String given
        :param charIndex: index of the character in s the pointer is at
        :return: String that has q as a prefix and have a higher frequency in the database than any other
                 string with q as a prefix
        :complexity: O(n) where n is the length of q
        """
        if charIndex < len(s):
            arrayIndex = ord(s[charIndex]) - ord("A") + 1
            if current is not None:
                return self.queryAux(current.link[arrayIndex], s, charIndex + 1)
        else:
            if current is not None and current.max is not None:
                return current.max.data
            else:
                return


class TreeNode:
    """
    TreeNode class that represents a node in a Suffix trie for OrfFinder
    """
    def __init__(self) -> None:
        """
        Constructor for TreeNode class
        :complexity: O(1)
        """
        self.link = [None] * 5
        self.depth = 0
        self.leafNode = self
        self.visitor = []
        self.visitor_pos = []


class OrfFinder:
    """
    Class to store portion of DNA that is used as the blueprint for a protein
    """
    def __init__(self, genome: str) -> None:
        """
        Constructor of OrfFinder class that creates two suffix trie based on genome, one from front to back and
        another one from back to front
        :param genome: String used to create the suffix tries
        :return: None
        :complexity: O(n^2) where n is the length of genome
        """
        self.word = genome
        self.maxDepth = len(genome) + 1
        emoneg = genome[::-1]
        self.reverseTreeRoot = TreeNode()
        self.root = TreeNode()

        # create front to back suffix trie
        length = len(genome)
        self.addGenome(genome)
        for i in range(0, length + 1):
            genome = genome[1:]
            self.addGenome(genome)

        # create back to front suffix trie
        length = len(emoneg)
        self.addReverseTree(emoneg)
        for i in range(0, length + 1):
            emoneg = emoneg[1:]
            self.addReverseTree(emoneg)

    def addGenome(self, genome: str) -> None:
        """
        Function to add genome to into a suffix trie
        :param genome: String to be added into the suffix trie
        :return None:
        :complexity: O(n) where n is the length of genome
        """
        self.addGenomeAux(self.root, genome, 0)

    def addReverseTree(self, genome: str) -> None:
        """
        Function to add genome to into the reverse suffix trie
        :param genome: String to be added into the reverse suffix trie
        :return None:
        :complexity: O(n) where n is the length of genome
        """
        self.addGenomeAux(self.reverseTreeRoot, genome, 0)

    def addGenomeAux(self, current: TreeNode, s: str, charIndex: int):
        """
        Auxiliary function to add genome into a trie using recursion
        :param current: current Node the pointer is at
        :param s: String to be stored into the trie
        :param charIndex: index of the character in s the pointer is at
        :return: TreeNode with the highest depth
        :complexity: O(n) where n is the length of s
        """
        # if pointer for s is inside the length of s
        if charIndex < len(s):
            arrayIndex = ord(s[charIndex]) - ord("A") + 1
            if current.link[arrayIndex] is None:
                current.link[arrayIndex] = TreeNode()
                current.link[arrayIndex].depth = charIndex + 1

            # store any string that passes through this node, in other words store all the data of it's child
            current.visitor.append(s)
            # store the position of the first word of s with respect to the original word
            current.visitor_pos.append(self.maxDepth - 1 - (len(s)))

            # store the recently added Node
            temp = self.addGenomeAux(current.link[arrayIndex], s, charIndex + 1)

            # Store node with the higher depth and return it
            if temp.depth > current.leafNode.depth:
                current.leafNode = temp
            return current.leafNode
        # end condition for recursion
        else:
            # store the node and its depth
            if current.link[0] is None:
                current.link[0] = TreeNode()
                current.link[0].depth = charIndex + 1
                if current.link[0].depth > current.depth:
                    current.leafNode = current.link[0]
            return current.leafNode

    def find(self, start: str, end: str) -> list:
        """
        Function that returns a list of strings. This list contains all the substrings of genome which have start as a
        prefix and end as a suffix
        :param start: prefix required for the string
        :param end: suffix required for the string
        :return: A list that contains all the substrings of genome which have start as a prefix and end as a suffix
        :complexity: O(len(start)+len(end)+U) where U is the number of characters in the output list.
        """
        output = []
        end = end[::-1]
        startNode = self.getStart(start)
        endNode = self.getEnd(end)

        # if start and end exists
        if startNode is not None and endNode is not None:
            # loop through all strings in the child of the nodes
            for i in range(0, len(startNode.visitor)):
                startPos = startNode.visitor_pos[i]
                for j in range(0, len(endNode.visitor)):
                    endPos = self.maxDepth - 2 - endNode.visitor_pos[j]
                    # if its a valid string, append to output
                    if startPos < endPos and (startPos + len(start) - 1 < endPos) and (
                            endPos - len(end) + 1 > startPos + len(start) - 1):
                        output.append(self.word[startPos:endPos + 1])
            return output
        else:
            return []

    def getStart(self, start: str) -> TreeNode:
        """
        Function to return a Node with start as a prefix in the front-back tree
        :param start: prefix to be searched for
        :return: Node with start as a prefix
        :complexity: O(n) where n is the length of start
        """
        current = self.root
        for char in start:
            index = ord(char) - ord("A") + 1
            if current.link[index] is not None:
                current = current.link[index]
            else:
                return None
        return current

    def getEnd(self, start: str) -> TreeNode:
        """
        Function to return a Node with start as a prefix in the back-front tree
        :param start: prefix to be searched for
        :return: Node with start as a prefix
        :complexity: O(n) where n is the length of start
        """
        current = self.reverseTreeRoot
        for char in start:
            index = ord(char) - ord("A") + 1
            if current.link[index] is not None:
                current = current.link[index]
            else:
                return None
        return current
