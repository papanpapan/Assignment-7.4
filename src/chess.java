import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
public class chess {
	static final Pos[][] chessboard = new Pos[8][8];

	static Queue<Pos> q = new LinkedList<Pos>();
	
	public static void main(String[] args) {
		
		//Populate the chessboard with position values as unreachable
		populateChessBoard();
		
		//Assume the position for simplicity. In real world, accept the values using Scanner.
		Pos start = new Pos(0, 1, 0); // Position 0, 1 on the chessboard
		Pos end = new Pos(4, 4, Integer.MAX_VALUE);
		
		//Assign starting depth for the source as 0 (as this position is reachable in 0 moves)
		chessboard[0][1] = new Pos(start.x, start.y, 0);
		
		//Add start position to queue
		q.add(start);

		while (q.size() != 0) // While queue is not empty
		{
			Pos pos = q.poll(); //read and remove element from the queue
			
			//If this position is same as the end position, you found the destination
			if (end.equals(pos)) {
				// We found the Position. Now trace back from this position to get the actual shortest path 
				Iterable<Pos> path = getShortestPath(start, end);
				System.out.println("Minimum jumps required: " + pos.depth );
				System.out.println("Actual Path");
				System.out.println("("+pos.x + " " + pos.y+")");
				
				for(Pos position: path) {
					System.out.println("("+position.x + " " + position.y+")");
				}
				
				return;
			}
			else {
				// perform BFS on this Pos if it is not already visited
				bfs(pos, ++pos.depth);
			}
		}

		//This code is reached when the queue is empty and we still did not find the location.
		System.out.println("End position is not reachable for the knight");
	}

	//Breadth First Search 
	private static void bfs(Pos current, int depth) {

		// Start from -2 to +2 range and start marking each location on the board
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				
				Pos next = new Pos(current.x + i, current.y + j, depth);
			
				if(inRange(next.x, next.y)) {
					//Skip if next location is same as the location you came from in previous run
					if(current.equals(next)) continue;

					if (isValid(current, next)) {
						
						Pos position = chessboard[next.x][next.y] ;
						/* 
						 * Get the current position object at this location on chessboard. 
						 * If this location was reachable with a costlier depth, this iteration has given a shorter way to reach
						 */
						if (position.depth > depth) {
							chessboard[current.x + i][current.y + j] = new Pos(current.x, current.y, depth);
							q.add(next);
						}
					}
				}

			}

		}

	}

	private static boolean inRange(int x, int y) {
		return 0 <= x && x < 8 && 0 <= y && y < 8;
	}

	/*Check if this is a valid jump or position for Knight based on its current location */
	public static boolean isValid(Pos current, Pos next) {
		// Use Pythagoras theorem to ensure that a move makes a right-angled triangle with sides of 1 and 2. 1-squared + 2-squared is 5.
		int deltaR = next.x - current.x;
		int deltaC = next.y - current.y;
		return 5 == deltaR * deltaR + deltaC * deltaC;
	}

	/*Populate initial chessboard values*/
	private static void populateChessBoard() {
		for (int i = 0; i < chessboard.length; i++) {
			for (int j = 0; j < chessboard[0].length; j++) {
				chessboard[i][j] = new Pos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
			}
		}
	}
	
	/*Get the shortest Path and return iterable object */
	private static Iterable<Pos> getShortestPath(Pos start, Pos end) {
		
		Stack<Pos> path = new Stack<Pos>();
		
		Pos current = chessboard[end.x][end.y];
		while(! current.equals(start)) {
			path.add(current);
			current = chessboard[current.x][current.y];
		}
		path.add(new Pos(start.x, start.y, 0));
		return path;
	}

}

class Pos {

	public int x;
	public int y;
	public int depth;
	
	Pos(int x, int y, int depth) {
		this.x = x;
		this.y = y;
		this.depth = depth;
	}

	public boolean equals(Pos that) {
		return this.x == that.x && this.y == that.y;
	}

	public String toString() {
		return "("+this.x + " " + this.y+ " " + this.depth +")";
	}

}
