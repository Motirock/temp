package game;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.Color;

import java.util.ArrayList;

import main.Rectangle;
import main.CollisionChecker;

public class Player {
    private GamePanel gp;

    public double vx, vy;
    public Rectangle hitbox = new Rectangle(0, 0, Math.random()*100+50, Math.random()*100+50);
    public boolean onFloor = false;

    //Make it so that you always have access to GamePanel, which lets you have movement, sound, etc.
    public Player(GamePanel gp) {
        this.gp = gp;
    }

    //Do logical updates, like movement,
    public void update(ArrayList<Rectangle> obstacles) {
        //Controls
        if (gp.keyH.upArrowIsPressed && onFloor) {
            //Jumping
            vy = -3;
        }
        if (gp.keyH.leftArrowIsPressed) {
            vx -= 0.01;
        }
        if (gp.keyH.rightArrowIsPressed) {
            vx += 0.01;
        }

        //Gravity
        vy += 0.01;

        //Movement
        hitbox.x += vx;
        hitbox.y += vy;
        vx *= 0.99;
        if (vx >= 1)
            vx = 1;
        else if (vx <= -1)
            vx = -1;

        //Collisions
        onFloor = false;
        for (Rectangle obstacle : obstacles) {
            if (CollisionChecker.doesCollideCenteredRects(hitbox, obstacle)) {
                handleCollision(hitbox, vx, vy, obstacle);
            }
        }

        //Bounds
        if (hitbox.x+hitbox.width/2.0 > 1600) {
            hitbox.x = 1600-hitbox.width/2.0;
            vx = 0;
        }
        if (hitbox.x-hitbox.width/2.0 < 0) {
            hitbox.x = 0+hitbox.width/2.0;
            vx = 0;
        }
        if (hitbox.y+hitbox.height/2.0 > 900) {
            hitbox.y = 900-hitbox.height/2.0;
            vy = 0;
            onFloor = true;
        }
        if (hitbox.y-hitbox.height/2.0 < 0) {
            hitbox.y = 0+hitbox.height/2.0;
            vy = 0;
        }
    }

    //Requires the player to have vx and vy (velocity)
    //Hitbox: hitbox of the player
    //Obstacle: hitbox of the obstacle/platform
    //This assumes the first rectangle is meant to move and the second one isn't
    public void handleCollision(Rectangle hitbox, double vx, double vy, Rectangle obstacle) {
        int checkCount = 0;
        while (CollisionChecker.doesCollideCenteredRects(hitbox, obstacle) && checkCount < 10) {
            checkCount++;

            double xDifference = hitbox.x-vx-obstacle.x;
            double yDifference = hitbox.y-vy-obstacle.y;

            double XA1 = hitbox.x-hitbox.width/2.0;
            double XA2 = hitbox.x+hitbox.width/2.0;
            double YA1 = hitbox.y-hitbox.height/2.0;
            double YA2 = hitbox.y+hitbox.height/2.0;
            double XB1 = obstacle.x-obstacle.width/2.0;
            double XB2 = obstacle.x+obstacle.width/2.0;
            double YB1 = obstacle.y-obstacle.height/2.0;
            double YB2 = obstacle.y+obstacle.height/2.0;
            double xOverlap = Math.max(0, Math.min(XA2, XB2) - Math.max(XA1, XB1));
            double yOverlap = Math.max(0, Math.min(YA2, YB2) - Math.max(YA1, YB1));

            boolean horizontalCollision = false, verticalCollision = false;
            if (vy == 0)
                horizontalCollision = true;
            else if (vx == 0)
                horizontalCollision = false;
            else if (Math.abs(xOverlap/vx/obstacle.width) < Math.abs(yOverlap/vy/obstacle.height)) {
                horizontalCollision = true;
            }
            verticalCollision = !horizontalCollision;

            if (horizontalCollision) {
                if (vx!= 0 && xDifference/vx < 0) {
                    if (vx > 0)
                        hitbox.x -= xOverlap+0.01;
                    else
                        hitbox.x += xOverlap+0.01;
                }
                vx = 0;
            }
            if (verticalCollision) {
                if (vy!= 0 && yDifference/vy < 0) {
                    if (vy > 0) {
                        hitbox.y -= yOverlap+0.01;
                        onFloor = true;
                    }
                    else
                        hitbox.y += yOverlap+0.01;
                }
                vy = 0;
            }
        }
    }

    public void draw(Graphics2D g2, double GS) {
        //Setting the color to be green
        g2.setColor(Color.GREEN);
        //Draws the rectangle
        g2.fillRect((int) ((hitbox.x-hitbox.width/2.0)*GS), (int) ((hitbox.y-hitbox.height/2.0)*GS), (int) (hitbox.width*GS), (int) (hitbox.height*GS));
    }
}
