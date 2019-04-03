package game;

public class gold {
	static int gold = 350;
	
	void gold_init(int g){
		gold = g;
	}
	
	void tower_sell(int j ){
		gold = gold + j;
	}
	
	void tower_buy(int j ){
		gold = gold - j;
	}
	
	static void reward(int g){
		
		gold = gold+g;
		
	}

}
