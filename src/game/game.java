package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class game extends Applet implements Runnable {

	tower t = new tower();
	enemy e = new enemy();
	bullet b = new bullet();

	Graphics g;
	// アプレットサイズ
	int current_width, current_height;
	// 裏イメージデータ
	Image offscreenImg;
	// 裏Graphics
	Graphics offscreenG;

	// ゲームの絶対時間
	int game_time = 0;

	// 最初のゴールド
	int gold = 10000;
	int tower_type;
	int tower_coordinate_x, tower_coordinate_y;

	boolean title = true;
	boolean tower_sell_check;
	boolean tower_buy_check = false;
	boolean clear_check = false;
	boolean gclear;

	// マップ上タワーの存在を判断
	boolean tower_create_check[][] = new boolean[10][10];

	Thread th;

	Image image_background, image_topmenu, img_title, image_delete;
	Image image_enemy_normal, image_enemy_elite;
	Image img_tower_guard, img_tower_tri, img_tower_single;

	public void init() {

		this.setSize(700, 500);

		// アプレットサイズの取得
		current_width = getSize().width;
		current_height = getSize().height;

		// 裏イメージ（画面）の作成
		offscreenImg = createImage(current_width, current_height);

		// 裏Graphicsの取得
		offscreenG = offscreenImg.getGraphics();

		// 画像の読み込み
		
		//ゲーム用
		img_title = getImage(getDocumentBase(), "title.jpg");
		image_background = getImage(getDocumentBase(), "bg.jpg");
		image_topmenu = getImage(getDocumentBase(), "top1.jpeg");
		image_delete = getImage(getDocumentBase(), "delate.jpg");
		
		//タワー類
		img_tower_single = getImage(getDocumentBase(), "nor.png");
		img_tower_guard = getImage(getDocumentBase(), "gd.png");
		img_tower_tri = getImage(getDocumentBase(), "tri.png");
		
		//敵類
		image_enemy_normal = getImage(getDocumentBase(), "z1.png");
		image_enemy_elite = getImage(getDocumentBase(), "z2.png");
		
		// マウスの動き
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < 4; i++) {
					if (e.getX() >= 80 + i * 43 && e.getX() <= 80 + (i + 1) * 43 && e.getY() >= 0 && e.getY() <= 60) {
						// 購入判断
						if (gold >= t.tower_ability_list[i][0])

							if (i == 3) { // タワー売ります
								tower_sell_check = true;
								tower_type = i;
							} else {
								tower_buy_check = true;
								tower_type = i;
							}
					}
				}
				if (e.getX() >= 120 && e.getX() <= 453 && e.getY() >= 80 && e.getY() <= 310 && tower_buy_check
						|| e.getX() >= 120 && e.getX() <= 453 && e.getY() >= 80 && e.getY() <= 310
								&& tower_sell_check) {
					tower_coordinate_x = (int) Math.ceil((e.getX() - 120) / 37);
					tower_coordinate_y = (int) Math.ceil((e.getY() - 95) / 46);

					// remove操作
					if (tower_sell_check) {
						for (int i = 0; i < t.tower_list.size(); i++) {
							if (t.tower_list.get(i)[1] == tower_coordinate_x
									&& t.tower_list.get(i)[2] == tower_coordinate_y) {
								if (tower_create_check[tower_coordinate_x][tower_coordinate_y]) {
									gold = (int) (gold + t.tower_ability_list[t.tower_list.get(i)[0]][0] * 0.5);
									t.kill(i);
									tower_create_check[tower_coordinate_x][tower_coordinate_y] = false;
								}
							}
						}
						tower_sell_check = false;
					} else if (tower_create_check[tower_coordinate_x][tower_coordinate_y] != true) {
						// 購入操作
						gold = gold - t.tower_ability_list[tower_type][0];
						tower_create_check[tower_coordinate_x][tower_coordinate_y] = true;

						t.create(tower_type, tower_coordinate_x, tower_coordinate_y, game_time);
						tower_buy_check = false;
						repaint();
					}
				}
			}
		});
		// キーボードイベント処理
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					title = false;
					repaint();
				}
			}
		});

	}

	public void start() {
		if (th == null) {
			th = new Thread(this);
			th.start();
		}
	}

	public void stop() {
		if (th != null) {
			th = null;
		}
	}

	public void run() {
		while (th != null) {
			if (title == false) {
				// ゲーム開始
				
				e.sortie(game_time);
				b.attack(game_time, t.tower_list);
				b.damage(game_time, e.enemy_list);
				e.eat(game_time, t.tower_list);
				e.over_check(game_time, e.enemy_list);
				game_time = game_time + 1;
				repaint();
				sleep(100);
			} else if (title || e.gameover_check) {
				repaint();
				sleep(100);
			}
		}
	}

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	public void paint(Graphics g) {
		// 描画処理
		Graphics2D g2 = (Graphics2D) g;
		if (title) {

			g.drawImage(img_title, 0, 0, null);
			Font font1 = new Font("Arial", Font.PLAIN, 50);
			g2.setFont(font1);
			g.drawString("press enter key", 150, 375);

		} else if (e.gameover_check) {
			// ゲームオーバー
			Font font1 = new Font("Arial", Font.PLAIN, 50);
			g2.setFont(font1);
			g.drawString("zombies eat your brain", 100, 200);
			g.drawString("Game Over", 100, 250);
			stop();
		} else if (gclear) {
			g.fillRect(80, 90, 50, 50);
		} else {
			g2.clearRect(0, 0, getWidth(), getHeight());
			g.drawImage(image_background, 0, 0, null);
			g.drawImage(image_topmenu, 80, 0, null);
			g.drawImage(image_delete, 210, 0, null);
			g.setColor(Color.black);
			Font font1 = new Font("Arial", Font.PLAIN, 50);
			g2.setFont(font1);
			g.drawString(gold + "", 260, 45);
		}
		
		// 確認アイコン
		if (tower_sell_check) {
			g.drawImage(image_delete, 0, 0, null);
		}
		if (tower_buy_check) {
			if (tower_type == 0) {
				g.drawImage(img_tower_single, 0, 0, null);
			} else if (tower_type == 1) {
				g.drawImage(img_tower_tri, 0, 0, null);
			} else if (tower_type == 2) {
				g.drawImage(img_tower_guard, 0, 0, null);
			}
		}

		// towerの描画
		for (int i = 0; i < t.tower_list.size(); i++) {
			int tp = t.tower_list.get(i)[0];
			int px = t.tower_list.get(i)[1];
			int py = t.tower_list.get(i)[2];
			if (tp == 0) {
				g.drawImage(img_tower_single, 120 + px * 37, 95 + py * 46, null);
			} else if (tp == 1) {
				g.drawImage(img_tower_tri, 120 + px * 37, 95 + py * 46, null);
			} else if (tp == 2) {
				g.drawImage(img_tower_guard, 120 + px * 37, 95 + py * 46, null);
			}

		}
		// bullet描画
		for (int i = 0; i < b.bullet_list.size(); i++) {
			int b_posx = b.bullet_list.get(i)[1];
			int b_posy = b.bullet_list.get(i)[2];
			int b_type = b.bullet_list.get(i)[0];
			int excat_time = b.bullet_list.get(i)[4];

			int r_time = game_time - excat_time;

			if (b_type == 0) {
				g.setColor(Color.black);
				g.fillRect(140 + b_posx * 37 + 10 * r_time, 95 + b_posy * 46 + 10, 10, 10);

			}
			if (b_type == 1) {
				g.setColor(Color.red);
				g.fillRect(140 + b_posx * 37 + 10 * r_time, 95 + b_posy * 46 + 10, 10, 10);
			}

		}
		// enemy描画
		for (int i = 0; i < e.enemy_list.size(); i++) {
			// enemylist [0] type [1] 行目 [2]出撃タイム [4]hp
			int type = e.enemy_list.get(i)[0];
			int line = e.enemy_list.get(i)[1];
			int sortie_time = e.enemy_list.get(i)[2];
			int px = game_time - sortie_time;
			if (game_time >= sortie_time) {
				if (type == 0) {
					g.drawImage(image_enemy_normal, 473 - px, 95 + line * 46, null);
				}
				if (type == 1) {
					g.drawImage(image_enemy_elite, 473 - px, 95 + line * 46, null);
				}
			}
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

}
