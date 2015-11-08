public class Board {
	int size;
	int width;
	int radius;
	int hexes;
	String name;
	int movesMade;
	int state;
	Move[] moveList;
	int[] grid;
	int[] intToHexArray;
	int[] sizeToHexArray;
	int[] adjacent = new int[6];
	int[][] adjacentCompArray;
	int[][] cnstrArray;
	
	
	//CONSTRUCTORS
	public Board(String str) {
		//this.setup(str);
	}
	
	public Board() {
	}
	
	//SETUP FROM STRING METHOD
	public void setup(String setup) {
		movesMade = 0;
		int radi = 0;
		String name = "";
		short offset = 0;
		
		while(setup.charAt(offset)!='~'){
			name += setup.charAt(offset);
			offset++;
		}
		offset++;
		this.name = name;
		
		while(setup.charAt(offset)!='~'){
			radi = ((int)(setup.charAt(offset)-'0') + radi*10);
			offset++;
		}
		offset++;
		this.radius = radi;
		hexes = (3*radi*(radi+1))+1;
		
		moveList = new Move[hexes];
		for(int i = 0; i<hexes; i++) {
			moveList[i] = new Move(-1,-1);
		}
		
		width = (radius*2 + 3);
		size = width*width;
		grid = new int[size];
		cnstrArray = new int[hexes+1][hexes+1];
		for(int i = 0; i<=hexes; i++) {
			cnstrArray[0][i]=i;
		}
		
		//DEFINE ADJACENT SQUARES
		adjacent[0] = 1;
		adjacent[1] = -1;
		adjacent[2] = width;
		adjacent[3] = -width;
		adjacent[4] = width - 1;
		adjacent[5] = 1 - width;
		
		adjacentCompArray = new int[hexes+1][hexes];
		
		
		
		//FILL IN MAIN GRID WITH 0's and EDGES
		int front = radius;
		int middle;
		int end = 0;
		for(int i = 0; i<width; i++) {
			grid[i] = -1;
		}
		
		for(int i = size-width; i<=size-1; i++) {
			grid[i] = -1;
		}
		
		for(int i = width; i<size-width; i++) {
			
			middle = width - 2 - front - end;
			grid[i] = -1;
			i++;
			
			for(int q = 0; q<front&&i<size; q++) {
				grid[i] = -1;
				i++;
			}
			
			for(int q = 0; q<=middle-1&&i<size; q++) {
				grid[i] = 0;
				i++;
			}
			
			for(int q = 0; q<end&&i<size; q++) {
				grid[i] = -1;
				i++;
			}
			
			grid[i] = -1;
			
			if(front > 0) {
				front--;
			} else {
				end++;
			}
			System.out.println();
		}
		
		//INDEX OPEN SPOTS ON BOARD
		intToHexArray = new int[hexes];
		for(int i = 0, q = 0; i<size; i++){
			if(grid[i] == 0) {
				intToHexArray[q] = i;
				q++;
			}
		}
		
		//INDEX SIZE TO OPEN SPOTS, RETURN -1 IF NOT OPEN SPOT
		sizeToHexArray = new int[size];
		for(int i = 0 , q = 1; i<size; i++) {
			if(grid[i] == 0) {
				sizeToHexArray[i] = q;
				q++;
			} else {
				sizeToHexArray[i] = -1;
			}
		}
		
		//MAKE MAP OF CONSTRAINT RELATIONS
		boolean flag = true;
		
		for(int m = 1, q = 0; m<=hexes;m++) {
			q = 0;
			for(int i = 1; i<=hexes; i++) {
				flag = true;
				for(int a : adjacent) {
					if(m==sizeToHexArray[intToHexArray[i-1]+a]) {
						flag = false;
						break;
					}
				}
				
				if(m==sizeToHexArray[intToHexArray[i-1]]) {
					flag = false;
				}
				
				if(flag) {
					adjacentCompArray[m][q] = i;
					q++;
				}
			}
		}
		
		
		//CREATE MOVES PER SETUP STRING, EXECUTE MOVES
		int num = 0;
		Move check = new Move(-1,-1);
		for(int i = 0; i+offset<setup.length();) {
			switch(setup.charAt(offset+i)) {
				case('.'): i++; break;
				case('/'): offset++; break;
				default:
					num = 0;
					while(setup.charAt(offset+i)!= ',') {
						num = (int)(setup.charAt(offset+i) - '0') + num*10;
						offset++;
					}
					check.square = intToHexArray[i];
					check.type = num;
					this.SingletonPropAdd(check);
					i++;
					break;
			}
		}
	}
	
	public void report() {
		System.out.println("\nRadius " + this.radius + "\nState " + this.state + "\nName " + this.name + "\nHexes " + this.hexes + "\nSize " + this.size + "\nWidth " + width);
	}
	
	public void printCnstr() {
		for(int i = 0; i<=hexes; i++) {
			System.out.print("#" + i + " 	");
			for(int q = 0; q<cnstrArray[i].length; q++) {
				System.out.printf("%3d",cnstrArray[i][q]);
			}
			System.out.println();
		}
	}
	
	public void print() {
		System.out.print("\nBOARD : " + name);
		for(int i = 0; i<size; i++) {
			if(i%width == 0) {
				System.out.print("\n");
			}
			
			if(grid[i] != 0 && grid[i]!=-1) {
				System.out.printf("%4d",grid[i]);
			} else if(grid[i]!=-1){
				System.out.printf("%4s", ".");
			} else {
				System.out.printf("%4s", "####");
			}
		}
		System.out.println();
	}
	
	public void SingletonPropAdd(Move move) {
		if(movesMade>=hexes) {
			System.out.println("ERROR, TO MANY MOVES");
			return;
		}
		moveList[movesMade].square = move.square;
		moveList[movesMade].type = move.type;
		movesMade++;
		
		int sqr = move.square;
		int type = move.type;
		grid[sqr] = type;
		
		int find = sizeToHexArray[sqr];
		
		for(int i = 1; i<=this.intToHexArray.length;i++) {
			cnstrArray[i][type]++;
		}
		for(int i = 1; i<cnstrArray[find].length; i++) {
			cnstrArray[find][i]++;
		}
		cnstrArray[find][0] = type;
		
		boolean flag;
		
		for(int i = 0; i<intToHexArray.length; i++) {
			flag = true;
			for(int q : adjacent) {
				if((i+1) == sizeToHexArray[sqr+q]) {
					flag = false;
					break;
				}
			}
			
			if((i+1) == sizeToHexArray[sqr]) {
				flag = false;
			}
			
			if(flag) {
				if(type!=hexes) {
					cnstrArray[i+1][type+1]++;
				}
				
				if(type!=1) {
					cnstrArray[i+1][type-1]++;
				}
			}
		}		
	}

	public void SingletonPropUndo() {
		
		movesMade--;
		int sqr = moveList[movesMade].square;
		int type = moveList[movesMade].type;
		
		moveList[movesMade].square = -1;
		moveList[movesMade].type=-1;
		
		
		grid[sqr] = 0;
		
		int find = sizeToHexArray[sqr];
		
		for(int i = 1; i<=this.intToHexArray.length;i++) {
			cnstrArray[i][type]--;
		}
		for(int i = 1; i<cnstrArray[find].length; i++) {
			cnstrArray[find][i]--;
		}
		cnstrArray[find][0] = 0;
		
		boolean flag;
		
		for(int i = 0; i<intToHexArray.length; i++) {
			flag = true;
			for(int q : adjacent) {
				if((i+1) == sizeToHexArray[sqr+q]) {
					flag = false;
					break;
				}
			}
			
			if((i+1) == sizeToHexArray[sqr]) {
				flag = false;
			}
			
			if(flag) {
				if(type!=hexes) {
					cnstrArray[i+1][type+1]--;
				}
				if(type!=1) {
					cnstrArray[i+1][type-1]--;
				}
			}
		}	
	}
	
	public Board deepCopy() {
		Board copyBoard = new Board();
		
		//SIMPLE VALUES
		copyBoard.state = this.state;
		copyBoard.size = this.size;
		copyBoard.width = this.width;
		copyBoard.radius = this.radius;
		copyBoard.hexes = this.hexes;
		copyBoard.movesMade = this.movesMade;
		copyBoard.name = this.name;
		
		//ARRAYS
		
		//int[] moveList;
		copyBoard.moveList = new Move[hexes];
		for(int i = 0; i<this.moveList.length; i++) {
			copyBoard.moveList[i] = this.moveList[i];
		}
		
		
		//int[] grid;
		copyBoard.grid = new int[size];
		for(int i = 0; i<this.grid.length; i++) {
			copyBoard.grid[i] = this.grid[i];
		}
		
		//int[] intToHexArray;
		copyBoard.intToHexArray = new int[hexes];
		for(int i = 0; i<this.intToHexArray.length; i++) {
			copyBoard.intToHexArray[i] = this.intToHexArray[i];
		}
		
		//int[] sizeToHexArray;
		copyBoard.sizeToHexArray = new int[size];
		for(int i = 0; i<this.sizeToHexArray.length; i++) {
			copyBoard.sizeToHexArray[i] = this.sizeToHexArray[i];
		}
		
		//int[] adjacent = new int[6];
		copyBoard.adjacent = new int[6];
		for(int i = 0; i<this.adjacent.length; i++) {
			copyBoard.adjacent[i] = this.adjacent[i];
		}
		
		//int[][] adjacentCompArray;
		copyBoard.adjacentCompArray = new int[hexes+1][hexes];
		for(int i = 0; i<hexes+1;i++){
			for(int q=0; q<hexes;q++){
				copyBoard.adjacentCompArray[i][q] = this.adjacentCompArray[i][q];
			}
		}
		
		//int[][]cnstrArray;
		copyBoard.cnstrArray = new int[hexes+1][hexes+1];
		for(int i = 0; i<hexes+1;i++){
			for(int q=0; q<hexes+1;q++){
				copyBoard.cnstrArray[i][q] = this.cnstrArray[i][q];
			}
		}
		
		return copyBoard;
		
	}
}
