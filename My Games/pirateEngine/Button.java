import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Button
{
    private int x, y, width, height, gameState;
    private boolean pressed;

    public Button(int xx, int yy, int w, int h, int gS){
        x = xx;
        y = yy;
        width = w;
        height = h;
        gameState = gS;
    }

    public void draw(Graphics g){
        // Base of Button
        if(gameState == 1 || gameState == 3)
        g.setColor(Color.yellow);
        if(gameState == 2 || gameState == 4)
        g.setColor(Color.blue);
        g.drawRect(x,y,width,height);
    }

    public int getGameState(){return gameState;}

    public int getX(){return x;}

    public int getY(){return y;}

    public int getWidth(){return width;}

    public int getHeight(){return height;}
}
