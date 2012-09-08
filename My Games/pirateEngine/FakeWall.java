import java.awt.*;

public class FakeWall extends Wall{
    private int x,y,h,w,vx,vy,count,drawX,drawY;
    private Rectangle box;

    public FakeWall(int xx, int yy, int width, int height){
        super(xx,yy,width,height);
        x = xx;
        y = yy;
        w = width;
        h = height;
        vy = 0;
        vx = 0;
        count = 0;
        box = new Rectangle(x,y,w,h);
    }

    public void draw(Graphics2D g){
        int columns = w/30;
        int rows = h/15;
        int width = 30;
        int height = 15;
        Color c = new Color(106,97,70);
        if(count % 70 != 0)
        for(int i = 0; i <= columns;i++){
            for(int k = 0; k < rows; k++){
                if(k%2 == 0){
                    if(i != columns){
                        g.setColor(c);
                        g.fillRect(drawX+i*width,drawY+k*height,width,height);
                        g.setColor(Color.black);
                        g.drawRect(drawX+i*width,drawY+k*height,width,height);
                    }
                }
                else{
                    if(i == columns){
                        g.setColor(c);
                        g.fillRect(drawX+i*width-width/2,drawY+k*height,width-width/2,height);
                        g.setColor(Color.black);
                        g.drawRect(drawX+i*width-width/2,drawY+k*height,width-width/2,height);
                    }
                    else if(i == 0){
                        g.setColor(c);
                        g.fillRect(drawX+i*width,drawY+k*height,width-width/2,height);
                        g.setColor(Color.black);
                        g.drawRect(drawX+i*width,drawY+k*height,width-width/2,height);
                    }
                    else{
                        g.setColor(c);
                        g.fillRect(drawX+i*width-width/2,drawY+k*height,width,height);
                        g.setColor(Color.black);
                        g.drawRect(drawX+i*width-width/2,drawY+k*height,width,height);
                    }
                }
            }
        }
    }

    public void move(int scrollX, int scrollY){
        drawX = x - scrollX;
        drawY = y - scrollY;
        count++;
    }

    public Color getColor(){return Color.red;}

    public void screenSlide(int bobX){x+=bobX; box.translate(bobX,0);}

    public Rectangle getWall(){return box;}

    public int getX(){return x;}

    public int getY(){return y;}

    public int getVY(){return vy;}

    public int getVX(){return vx;}

    public int getHeight(){return h;}

    public int getWidth(){return w;}
}