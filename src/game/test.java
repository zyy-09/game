package game;

import java.applet.Applet;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class test extends Applet  implements Runnable {

	tower t = new tower();
	
	
	Thread th;
	Image image_background,image_topmenu,img_title,image_delete;
	
	 // アプレットサイズ
	int current_width, current_height;
	// 裏イメージデータ
	Image offscreenImg;
	// 裏Graphics
	Graphics offscreenG;
	  
	
	//ゲームの絶対時間
	int game_time = 0;
	
	boolean title = true; 
	boolean gameover_check =false;
	boolean hh;
	boolean tower_sell_check;
	boolean tower_buy_check = false;
	boolean clear_check=false;
	boolean gclear;
	
	public void init(){
		// アプレットサイズの取得
	    current_width = getSize().width;
	    current_height = getSize().height;
	    
	    // 裏イメージ（画面）の作成
	    offscreenImg = createImage(current_width, current_height);
	    
	    // 裏Graphicsの取得
	    offscreenG = offscreenImg.getGraphics();
	    
		// 画像の読み込み
	    img_title = getImage(getDocumentBase(), "title.jpg");
		image_background = getImage(getDocumentBase(), "bg.jpg");
		image_topmenu = getImage(getDocumentBase(), "top1.jpeg");
		image_delete= getImage(getDocumentBase(), "delate.jpg");
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
			if(title==false){
				//ゲーム開始
			game_time=game_time+1;
			
		
			repaint();
			sleep(100);
			}else if(title || gameover_check){
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
	
	public void paint(Graphics g){
	    // 描画処理
		Graphics2D g2 = (Graphics2D)g;
		if(title){
			g.drawImage(img_title,0,0,null);
			Font font1 = new Font("Arial", Font.PLAIN, 50);
		    g2.setFont(font1);
			g.drawString("press enter key", 150, 375);
		}else if (gameover_check){
			//ゲームオーバー
			Font font1 = new Font("Arial", Font.PLAIN, 50);
		    g2.setFont(font1);
			g.drawString("zombies eat your brain", 100, 200);
			g.drawString("Game Over", 100, 250);
		}else if(gclear){
			g.fillRect(80,90,50,50);
		}else{
			g2.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(image_background,0,0,null);
		g.drawImage(image_topmenu, 80, 0, null);
		g.drawImage(image_delete, 210, 0, null);
	  }
	}
	
}