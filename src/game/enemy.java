package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public class enemy extends Applet {
	// enemylist [0] type [1] 行目 [2]出撃タイム [3]hp
	ArrayList<int[]> enemy_list = new ArrayList<int[]>();
	tower t = new tower();
	gold gold = new gold();
	
	boolean gameover_check = false;
	boolean clear_check =false;
	int enemy_ablitity[][] = { {3,25},{7,50} };
	int game_gold;

	Image image_enemy_normal, image_enemy_elite;

    //出撃　
	void sortie(int time) {
		if (time == 50) {
			for (int i = 0; i < 15; i++) {
				int type = (int) (Math.random() * (1 - 0) + 0);
				int line = (int) (Math.random() * (5 - 0) + 0);
				int sortie_time = (int) (Math.random() * (750 - 50) + 50);
				int hp = enemy_ablitity[type][0];

				int a[] = { type, line, sortie_time, hp };
				enemy_list.add(a);
			}
		}
		if (time == 850) {
			for (int i = 0; i < 50; i++) {
				int type = (int) (Math.random() * (1 - 0) + 0);
				int line = (int) (Math.random() * (5 - 0) + 0);
				int sortie_time = (int) (Math.random() * (1600 - 850) + 50);
				int hp = enemy_ablitity[type][0];

				int a[] = { type, line, sortie_time, hp };
				enemy_list.add(a);
			}
		}
		if (time == 1800) {
			for (int i = 0; i < 100; i++) {
				int type = (int) (Math.random() * (1 - 0) + 0);
				int line = (int) (Math.random() * (5 - 0) + 0);
				int sortie_time = (int) (Math.random() * (4000 - 1800) + 50);
				int hp = enemy_ablitity[type][0];

				int a[] = { type, line, sortie_time, hp };
				enemy_list.add(a);
			}
		}
	}
	//体力のないゾンビを消します
	void check(int j, ArrayList<int[]> enemy_list) {
		enemy_list.get(j)[3] = enemy_list.get(j)[3] - 1;
		int type = enemy_list.get(j)[0];
		int reward_gold = enemy_ablitity[type][1];
		if (enemy_list.get(j)[3] == 0) {
			enemy_list.remove(j);
			gold.reward(reward_gold);
		}
	}
    //game_overのチェック
	void over_check(int time, ArrayList<int[]> enemy_list) {
		for (int i = 0; i < enemy_list.size(); i++) {
			int game_time = time;
			int sortie_time = enemy_list.get(i)[2];
			int enemy_posx = 473 - (game_time - sortie_time);
			if(enemy_posx<120){
				gameover_check = true;
			}
		}
	}
	
	void clear_check(int time, ArrayList<int[]> enemy_list) {
		if (time > 1800 && enemy_list.size()==0){
			clear_check = true;
		}
	}
	//タワーを食べます（攻撃）
	void eat(int time, ArrayList<int[]> tower_list) {

		for (int i = 0; i < enemy_list.size(); i++) {
			// enemylist [0] type [1] 行目 [2]出撃タイム [3]hp
			int game_time = time;
			int line = enemy_list.get(i)[1];
			int sortie_time = enemy_list.get(i)[2];
			int enemy_posx = 473 - (game_time - sortie_time);

			for (int j = 0; j < tower_list.size(); j++) {
				// towertlist [0] type [1][2] posxy(コマ目) [3] hp [4]生成タイム
				int tower_posx = tower_list.get(j)[1];
				int tower_pox = 120 + tower_posx * 37;
				int tower_posy = tower_list.get(j)[2];
				if (line == tower_posy && enemy_posx - tower_pox < 18 && tower_pox - enemy_posx < 0) {
					enemy_list.get(i)[2] = enemy_list.get(i)[2] + 1;
					t.eated(j, tower_list);

				}
			}
		}
	}

	void kill(int k) {
		enemy_list.remove(k);
	}
}
