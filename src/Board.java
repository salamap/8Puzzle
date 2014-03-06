import java.util.Arrays;

public class Board {
	private final int[][] board;;
	private final int dimension;
	private int cachedManhattan = -1;
	
	public Board(int[][] blocks) {
		this.dimension = blocks.length;
		this.board = new int [this.dimension][this.dimension];
		for(int row = 0; row < this.dimension; row++) {
			for (int col = 0; col < this.dimension; col++) {
				this.board[row][col] = blocks[row][col];
			}
		}
		
	}
	
    public int dimension() {
    	// board dimension N
    	return this.dimension;
    }
    
    public int hamming() {
    	int hamming = 0;      // number of blocks out of place
    	int expectedVal = 0;  // expected value at current block
    	for (int row = 0; row < this.dimension; row++) {
    		for (int col = 0; col < this.dimension; col++) {
    			if (this.board[row][col] != ++expectedVal) hamming++;
    		}
    	}
    	// subtract 1 for the block that contains 0
    	return hamming - 1;
    }
    
    public int manhattan() {
    	if (this.cachedManhattan >= 0) {
    		return this.cachedManhattan;
    	}
    	
    	int currManhattan = 0;  // sum of Manhattan distances between blocks and goal
    	for (int row = 0; row < this.dimension; row++) {
    		for (int col = 0; col < this.dimension; col++) {
    			int currBlockVal = this.board[row][col];
    			if (currBlockVal == 0) continue;  // 0 is not at (0,0)
    			int goalRow = (currBlockVal - 1) / this.dimension;
    			int goalCol = (currBlockVal - 1) % this.dimension;
    			currManhattan += Math.abs(goalRow - row) + Math.abs(goalCol - col);
    		}
    	}
    	this.cachedManhattan = currManhattan;
    	return this.cachedManhattan;
    }
    
    public boolean isGoal() {
    	// is this board the goal board?
    	return this.hamming() == 0;
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
    	int row;
    	Board twin = new Board(copyArray(this.board));
    	boolean containsZero;
    	
    	do {
    		containsZero = false;
    	    row = StdRandom.uniform(twin.dimension);
    	    for(int col = 0; col < twin.dimension; col++) {
    	    	if (twin.board[row][col] == 0) {
    	    		containsZero = true;
    	    		break;
    	    	}
    	    }
    	}while(containsZero);
    	
    	int col1 = twin.dimension / twin.dimension;
    	int col2 = twin.dimension % twin.dimension;
    	
    	int temp = twin.board[row][col1 ];
    	twin.board[row][col1 ] = twin.board[row][col2];
    	twin.board[row][col2] = temp;
    	
    	return twin;
    }
    
    public boolean equals(Object y) {
    	// does this board equal y?
    	if (this == y) return true;
    	if (y == null) return false;
    	if (this.getClass() != y.getClass()) return false;
    	Board that = (Board) y;
    	if (this.dimension != that.dimension) return false;
    	for(int row = 0; row < this.dimension; row++) {
    		if(!Arrays.equals(this.board[row], that.board[row])) return false;
    	}
    	return true;
    }
    
    public Iterable<Board> neighbors() {
    	Queue<Board> neighborQ = new Queue<Board>();
    	int zRow = 0;  // row that contains zero
    	int zCol = 0;  // col that contains zero
    	
    	foundZero:
    	for (int row = 0; row < this.dimension; row++) {
    		for (int col = 0; col < this.dimension; col++) {
    			if(this.board[row][col] == 0) {
    				zRow = row;
    				zCol = col;
    				break foundZero;
    			}
    		}
    	}
    	
        if (zRow > 0) {
        	Board n1 = new Board(copyArray(this.board));
        	n1.board[zRow][zCol] = n1.board[zRow - 1][zCol];
        	n1.board[zRow - 1][zCol] = 0;
            neighborQ.enqueue(n1);
        }
        if (zRow < this.dimension - 1) {
        	Board n2 = new Board(copyArray(this.board));
        	n2.board[zRow][zCol] = n2.board[zRow + 1][zCol];
        	n2.board[zRow + 1][zCol] = 0;
            neighborQ.enqueue(n2);
        }
        if (zCol > 0) {
        	Board n3 = new Board(copyArray(this.board));
        	n3.board[zRow][zCol] = n3.board[zRow][zCol - 1];
        	n3.board[zRow][zCol - 1] = 0;
            neighborQ.enqueue(n3);
        }
        if (zCol < this.dimension - 1) {
        	Board n4 = new Board(copyArray(this.board));
        	n4.board[zRow][zCol] = n4.board[zRow][zCol + 1];
        	n4.board[zRow][zCol + 1] = 0;
            neighborQ.enqueue(n4);
        }
        return neighborQ;
    	// all neighboring boards
    }
    
    public String toString() {
    	// string representation of the board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(this.dimension + "\n");
        for (int row = 0; row < this.dimension; row++) {
            for (int col = 0; col < this.dimension; col++) {
                s.append(String.format("%2d ", this.board[row][col]));
            }
            s.append("\n");
        }
        return s.toString();
    }
	
    private int[][] copyArray(int[][] original) {
        int len = original.length;
        int[][] copy = new int[len][len];
        for (int row = 0; row < len; row++)
            for (int col = 0; col < len; col++)
                copy[row][col] = original[row][col];
        return copy;
    }
    
    public static void main(String[] args) {
//		// TODO Auto-generated method stub
    	int[][] testArray = {{8,1,3},{4,0,2},{7,6,5}};
    	Board testBoard = new Board(testArray);
    	// Test hamming val
    	StdOut.println("Hamming: " + testBoard.hamming());
    	// test manhattan val
    	StdOut.println("Manhattan: " + testBoard.manhattan());
    	// test toString()
    	StdOut.println("Here is a twin");
    	StdOut.print(testBoard.twin().toString());
    	// test neighbors()
    	StdOut.println("Here are the neighbors");
    	for(Board n : testBoard.neighbors()) {
    		StdOut.print(n.toString());
    		StdOut.println(n.isGoal());
    	}
    	StdOut.println("Is goal: " + testBoard.isGoal());
    	StdOut.println();
    	
    	int[][] goalArray = {{1,2,3},{4,5,6},{7,8,0}};
    	Board goalBoard = new Board(goalArray);
    	StdOut.print(goalBoard.toString());
    	StdOut.println("Is goal: " + goalBoard.isGoal());
	}

}
