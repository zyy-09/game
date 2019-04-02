package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class bullet extends Applet {
	//bulletlist [0] type [1][2]posxy [3]shottime
	ArrayList<int[]> bullet_list = new ArrayList<int[]>();
	int game_time;
	enemy e = new enemy();
	
	
	void attack(int time,ArrayList<int[]> tower_list) {
		
		
		for(int i = 0;i < tower_list.size();i++){
			int type = tower_list.get(i)[0];
			int bullet_posx = tower_list.get(i)[1];
			int bullet_posy = tower_list.get(i)[2];
			int shot_time = tower_list.get(i)[4];
			game_time = time;
			int r_time = game_time - shot_time;
			
			if(type==2)continue;
			
			int colddown40 = (time - shot_time-1)% 40;
			int colddown25 = (time - shot_time-1)% 25;
			
			game_time = time;
			
			if(type==0&&(colddown40%40==0 || colddown40 == 0)){
				int a[]={type,bullet_posx,bullet_posy,colddown40,game_time};
				bullet_list.add(a);
			}
			if(type==1&&(colddown25%25==0 || colddown25 == 0)){
				int a[]={type,bullet_posx,bullet_posy,colddown25,game_time};
				bullet_list.add(a);
				}
			}
		}
	void damage(int time,ArrayList<int[]> enemy_list){
		int game_time = time;
		for(int i = 0;i <bullet_list.size();i++){
			//bulletlist [0] type [1][2]posxy [3]shottime
			int bullet_posx = bullet_list.get(i)[1];
			int bullet_posy = bullet_list.get(i)[2];
			int excat_time = bullet_list.get(i)[4];
			int r_time = game_time - excat_time;
			int bullet_pox = 140+bullet_posx*37+10*r_time;
			for(int j = 0;j<enemy_list.size();j++){
				//enemylist [0] type [1] 行目 [2]出撃タイム [3]hp
				int line = enemy_list.get(j)[1];
				int sortie_time = enemy_list.get(j)[2];
				int hp = enemy_list.get(j)[3];
				int exact_time = game_time -sortie_time;
				int enemy_posx = 473 - (game_time - sortie_time);
				
				if(bullet_posy == line && exact_time >= 0 && enemy_posx - bullet_pox <=10 && bullet_pox - enemy_posx <= 10 ){
					bullet_list.remove(i);
					e.check(j,enemy_list);
				}
			}
			if (140+bullet_posx*37+10*r_time>473){
				bullet_list.remove(i);
			}
		}
		
	}
}
