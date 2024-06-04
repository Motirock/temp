package game;

import java.util.ArrayList;

import main.GamePanel;
import main.Rectangle;

import java.awt.Color;
import java.awt.Graphics2D;

public class Game {
    private GamePanel gp;

    private long updates = 0;

    Player player;
    ArrayList<Rectangle> obstacles = new ArrayList<Rectangle>();
   
    public Game(GamePanel gp) {
        this.gp = gp;
        player = new Player(gp);

        for (int i = 0; i < 10; i++) {
            obstacles.add(new Rectangle(Math.random()*1200+200, Math.random()*700+200, Math.random()*100+100, Math.random()*100+100));
        }
    }
    
    public void update() {
        updates++;
        player.update(obstacles);
    }
   
    public void draw(Graphics2D g2, double GS) {
        player.draw(g2, GS);

        for (Rectangle obstacle : obstacles) {
            g2.setColor(Color.RED);
            g2.fillRect((int) ((obstacle.x-obstacle.width/2.0)*GS), (int) ((obstacle.y-obstacle.height/2.0)*GS), (int) (obstacle.width*GS), (int) (obstacle.height*GS));
        }
    }
}
