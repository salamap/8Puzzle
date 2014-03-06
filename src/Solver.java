public class Solver {
	private Node goalState = null;
	
	private class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private final Node prev;
        private final int priority;
		
        public Node(Board newBoard, Node previous) {
        	this.board = newBoard;
        	this.prev = previous;
        	if (previous == null) this.moves = 0;
        	else this.moves = previous.moves + 1;
        	this.priority = newBoard.manhattan() + this.moves;
        }
        
        @Override
		public int compareTo(Node that) {
			// TODO Auto-generated method stub
        	if (this.priority > that.priority) return 1;
        	if (this.priority < that.priority) return -1;
			return 0;
		}
	}
	
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
        if (initial.isGoal())
            goalState = new Node(initial, null);
        else
            goalState = startSolving(initial, initial.twin());
    }
	
	private Node startSolving(Board initial, Board twin) {
        Node min;
        Node minTwin;
        MinPQ<Node> gameTree = new MinPQ<Node>();
        MinPQ<Node> twinGameTree = new MinPQ<Node>();
        gameTree.insert(new Node(initial, null));
        twinGameTree.insert(new Node(twin, null));
        while (true) {
        	min = gameTree.delMin();
        	minTwin = twinGameTree.delMin();
        	if (min.board.isGoal()) {
        		break;
        	}
        	if (minTwin.board.isGoal()) {
        		min = null;
        		break;
        	}
            saveNeighbors(min, gameTree);
            saveNeighbors(minTwin, twinGameTree);
        }
        return min;
	}
    
	private void saveNeighbors(Node min, MinPQ<Node> currTree) {
        for (Board n: min.board.neighbors()) {
        	if (min.prev == null) {
        		currTree.insert(new Node(n, min));
        	}
        	else {
                if (!n.equals(min.prev.board))
                    currTree.insert(new Node(n, min));
        	}
        }
	}
	
	public boolean isSolvable() {
    	// is the initial board solvable?
    	return goalState != null;
    }
	
    public int moves() {
    	// min number of moves to solve initial board; -1 if no solution
    	if (goalState == null) return -1;
    	return goalState.moves;
    }
    
    public Iterable<Board> solution() {
    	// sequence of boards in a shortest solution; null if no solution
    	if (!this.isSolvable()) return null;
    	
    	Stack<Board> solutionStack = new Stack<Board>();
        for (Node curr = goalState; curr != null; curr = curr.prev) {
        	solutionStack.push(curr.board);
    	}
    	return solutionStack;
    }
	
    public static void main(String[] args) {
	    // create initial board from file
	    In in = new In(args[0]);
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	} 
}