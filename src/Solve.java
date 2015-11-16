import java.util.Scanner;

public class Solve {
	static boolean doPrint = false;
	
	public static boolean checkValid(Board board) {
		
		
		for(int i = 1; i<=board.hexes; i++) {
			if(board.cnstrArray[i][0]==0) {
				
			} else {
				boolean flag1 = true;
				boolean flag2 = true;
				if(board.cnstrArray[i][0]!=board.hexes) {
					for(int a : board.adjacent) {
						if(board.sizeToHexArray[a+board.intToHexArray[i-1]]!=-1) {
							if(board.grid[a+board.intToHexArray[i-1]]==board.cnstrArray[i][0]+1||0==board.cnstrArray[board.sizeToHexArray[a+board.intToHexArray[i-1]]][board.cnstrArray[i][0]+1]) {
								flag1 = false;
								break;
							}
						}
						
					}
				} else {
					flag1 = false;
					break;
				}
				
				if(flag1) {
					return false;
				}
				
				if(board.cnstrArray[i][0]!=1) {
					for(int a : board.adjacent) {
						if(board.sizeToHexArray[a+board.intToHexArray[i-1]]!=-1) {
							if(board.grid[a+board.intToHexArray[i-1]]==board.cnstrArray[i][0]-1||0==board.cnstrArray[board.sizeToHexArray[a+board.intToHexArray[i-1]]][board.cnstrArray[i][0]-1]) {
								flag2 = false;
								break;
							}
						}
					}
				} else {
					flag2 = false;
					break;
				}
				
				if(flag2) {
					return false;
				}
			}
		}
		if(doPrint){System.out.println("ITS FINE");}
		return true;
	}
	
	public static boolean checkSolved(Board board, int n, int sqr) {
		if(n == 1) {
			sqr = -1;
			for(int i = 1; i<=board.hexes; i++) {
				if(board.cnstrArray[i][0]==0) {
					return false;
				}
				if(board.cnstrArray[i][0] == 1) {
					sqr = i;
				}
			}
			
			if(sqr == -1) {
				return false;
			}
			
			for(int i = 0; i<board.size; i++) {
				if(board.sizeToHexArray[i] == sqr) {
					sqr = i;
					break;
				}
			}
		}
		//boolean test = false;
		for(int i : board.adjacent) {
			if(board.grid[i+sqr]==n+1) {
				if(n+1 == board.hexes) {
					board.state = 1;
					return true;
				} else {
					return checkSolved(board,n+1,sqr+i);
				}
				
			}
		}
		return false;
	}
	
	public static Board DFS_Propagate_Singletons(Board board) {
		
		
		if(!Solve.checkValid(board)) {
			if(doPrint){System.out.println("BROKE");}
			return board;
		}
		
		
		MassPropagate(board);
		
		Solve.checkSolved(board,1,-1);
		if(board.state==1) {
			if(doPrint){System.out.println("ITS DONE");}
			return board;
		}
		
		
		int min = 99;
		int countrow = -1;
		int countcol = -1;
		int typefind = 0;
		int type = -1;
		
		for(int i = 1; i<=board.hexes;i++) {
			countrow = 0;
			countcol = 0;
			
			for(int q = 1; q<=board.hexes; q++) {
				if(board.cnstrArray[q][i]==0) {
					countcol++;
				}
			}
			
			if(countcol < min && countcol!= 0) {
				min = countcol;
				typefind = 1;
				type = i;
				if(min==1) {
					break;
				}
			}
			if(board.cnstrArray[i][0]==0) {
				for(int q = 1; q<=board.hexes; q++) {
					if(board.cnstrArray[i][q]==0) {
						countrow++;
					}
				}
				if(countrow < min && countrow != 0) {
					min = countrow;
					type = i;
					typefind = 0;
					if(min==1) {
						break;
					}
				}	
			}
		}
		
		if(min==99||min==0||min==-1) {
			if(doPrint){System.out.println("Darn it :)");}
			return board;
		}
		
		
		Move[] options = new Move[min];
		int movekey = 0;
		
		if(doPrint){System.out.print("\nFINDIND " + min + " OPTIONS: ");}
		
		if(typefind == 0) {
			if(doPrint){System.out.print("TYPE 0 ");}
			for(int i = 1; i<=board.hexes; i++) {
				if(board.cnstrArray[type][i]==0) {
					if(doPrint){System.out.print("(" + (type-1) + "," + i + ") ");}
					options[movekey] = new Move(board.intToHexArray[type-1],i);
					movekey++;
				}
			}
			
			
		} else if(typefind == 1) {
			if(doPrint){System.out.print("TYPE 1 ");}
			for(int i = 1; i<=board.hexes; i++) {
				if(board.cnstrArray[i][type]==0) {
					if(doPrint){System.out.print("(" + i+ "," + type + ") ");}
					options[movekey] = new Move(board.intToHexArray[i-1],type);
					movekey++;
				}
			}
		}
		
		
		//int holdmoves;
		
		if(min==1) {
			board.SingletonPropAdd(options[0]);
			Solve.checkSolved(board, 1, -1);
			if(board.state == 1) {
				if(doPrint){System.out.println("FOUND SOLVED BOARD");}
				return board;
			}
			return DFS_Propagate_Singletons(board);
		}
		
		
		for(int i = 0; i<options.length; i++) {
			//holdmoves = board.movesMade;
			Board nBoard = board.deepCopy();
			nBoard.SingletonPropAdd(options[i]);
			if(doPrint){nBoard.print();}
			nBoard = DFS_Propagate_Singletons(nBoard);
			Solve.checkSolved(nBoard, 1, -1);
			if(nBoard.state == 1) {
				if(doPrint){System.out.println("FOUND SOLVED BOARD");}
				return nBoard;
			}
		}
		return(board);
		
	}
	
	public static Board MassPropagate(Board board) {
		Solve.checkSolved(board, 1, -1);
		if(board.state==1) {
			return board;
		}
		if(board.movesMade >= board.hexes) {
			if(doPrint){System.out.println("Too many moves");}
			return board;
		}
		
		int min = 99;
		int countrow = -1;
		int countcol = -1;
		int typefind = -1;
		int type = -1;
		
		for(int i = 1; i<=board.hexes;i++) {
			countrow = 0;
			countcol = 0;
			
			for(int q = 1; q<=board.hexes; q++) {
				if(board.cnstrArray[q][i]==0) {
					countcol++;
				}
			}
			
			if(countcol < min && countcol!= 0) {
				min = countcol;
				typefind = 1;
				type = i;
				if(min==1) {
					break;
				}
			}
			if(board.cnstrArray[i][0]==0) {
				for(int q = 1; q<=board.hexes; q++) {
					if(board.cnstrArray[i][q]==0) {
						countrow++;
					}
				}
				if(countrow < min && countrow != 0) {
					min = countrow;
					type = i;
					typefind = 0;
					if(min==1) {
						break;
					}
				}	
			}
		}
		
		if(min==1) {
			Move move = new Move(-1,-1);
			if(typefind == 0) {
				for(int i = 1; i<=board.hexes; i++) {
					if(board.cnstrArray[type][i]==0) {
						move = new Move(board.intToHexArray[type-1],i);
						break;
					}
					
				}
				board.SingletonPropAdd(move);
				return MassPropagate(board);
			} else if(typefind == 1){
				for(int i = 1; i<=board.hexes; i++) {
					if(board.cnstrArray[i][type]==0) {
						move = new Move(board.intToHexArray[i-1],type);
						break;
					}
				}
				board.SingletonPropAdd(move);
				return MassPropagate(board);
			}
		}
		
		boolean upflag;
		boolean downflag;
		boolean redoflag = false;
			for(int i = 1; i<=board.hexes;i++) {
				for(int q = 1; q<=board.hexes; q++) {
					upflag = true;
					downflag = true;
					for(int a : board.adjacent) {
						if(board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]]][0]!=0||board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]]][i]!=0) {
							upflag = false;
							downflag = false;
							break;
						}
						
						if(i!=board.hexes) {
							if(board.sizeToHexArray[board.intToHexArray[q-1]+a]!=-1) {
								if(board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]+a]][i+1]==0||board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]+a]][0]==i+1) {
									upflag = false;
								}
							}
						} else {
							upflag = false;
						}
						
						if(i!=1) {
							if(board.sizeToHexArray[board.intToHexArray[q-1]+a]!=-1) {
								if(board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]+a]][i-1]==0||board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]+a]][0]==i-1) {
									downflag = false;
								}
							}
						} else {
							downflag = false;
						}
						
					}
					
					if(upflag) {
						board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]]][i]++;
						redoflag = true;
					}
					
					if(downflag) {
						board.cnstrArray[board.sizeToHexArray[board.intToHexArray[q-1]]][i]++;
						redoflag = true;
					}
				}
			}
		
		
		if(redoflag) {
			return MassPropagate(board);
		}
		return board;
	}
}
