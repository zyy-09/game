package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public class tower extends Applet{
	
	//towertlist [0] type  [1][2] posxy(コマ目) [3] hp [4]生成タイム
	ArrayList<int[]> tower_list = new ArrayList<int[]>();
	int tower_ability_list[][]={{100,1,5},{325,5,10},{50,0,100},{0,0,0}};
	//建てます
	void create(int type ,int posx,int posy,int time){
		
		int hp = tower_ability_list[type][2];
		
		int tower[] = {type,posx,posy,hp,time};
		
		tower_list.add(tower);
	}
	//食われる
	void eated(int j, ArrayList<int[]> tower_list){
		int hp = tower_list.get(j)[3];
		tower_list.get(j)[3] = tower_list.get(j)[3] -1 ;
		if (tower_list.get(j)[3] == 0){
			tower_list.remove(j);
		}
	}
	//消します
	void kill(int k){
		tower_list.remove(k);
	}
	


}
