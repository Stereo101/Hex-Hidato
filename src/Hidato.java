
public class Hidato {
	public static void main(String[] args) { 
		
		String blank3 = "blank3~3~..../...../....../......./....../...../....";
		String blank4 = "blank4~4~...../....../......./......../........./......../......./....../.....";
		String medium138221 = "medium 138221~3~27,.30,33,/....34,/...12,../.22,..10,.37,/21,...6,8,/19,..1,./....";
		String easy = "Easy~2~.15,13,/.16,../19,..../.5,9,./1,6,.";
		String easySolved = "EasySolved~2~17,15,13,/18,16,14,12,/19,3,4,10,11,/2,5,9,8,/1,6,7,";
		String blank5 = "Blank5~2~";
		
		
		String master138221 = "master138221~5~41,.34,.31,./.39,36,..29,./.43,....../.46,....25,../........18,./51,...62,....../.53,.91,70,72,5,2,1,./........./..87,..75,../.81,....11,/.80,.78,..";
		String master138226 = "master138226~5~.59,..../.....11,./.....13,../..42,....../......52,1,../..39,..48,..5,../68,.71,.34,...6,./....33,..28,./.91,..77,.29,./....84,../......";
		Board hidato = new Board();
		hidato.setup(master138221);
		hidato.print();
		hidato.printCnstr();
		hidato = Solve.DFS_Propagate_Singletons(hidato);
		if(Solve.checkValid(hidato)) {
			System.out.println("Valid");
		} else {
			System.out.println("Not Valid");
		}
		
		hidato.print();
		hidato.printCnstr();
		
	
	}
}
