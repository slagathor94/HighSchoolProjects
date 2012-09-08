import java.awt.*;

public class Platform extends Wall{
	private int x,y,h,w,vx,vy,count,step,drawX,drawY;
	private int[] zstarsx, zstarsy, zstarsd, zstarsc;
	private Rectangle box;
	
	public Platform(int xx, int yy, int width, int height, int vX, int vY, int steps){
		super(xx,yy,width,height);
		x = xx;
		y = yy;
		w = width;
		h = height;
		count = 0;
		vx = vX;
		vy = vY;
		step = steps;
		box = new Rectangle(x,y,w,h);
		zstarsx = new int[50*w/30];
        zstarsy = new int[50*w/30];
        zstarsd = new int[50*w/30];
        zstarsc = new int[50*w/30];
        for(int i = 0;i<zstarsx.length;i++)
        {
            zstarsx[i] = (int)(Math.random()*w);
            zstarsy[i] = (int)(Math.random()*h);
            zstarsd[i] = (int)(Math.random()*1+1);
            zstarsc[i] = (int)(Math.random()*1+2);
        }
	}
	
	public void draw(Graphics2D g2){
	    Color c = new Color(106,97,60);
		g2.setColor(c);
		g2.fillRect(drawX, drawY, w, h);
		c = new Color(56,47,20);
		g2.setColor(c);
		for(int i = 0; i<zstarsx.length;i++){
            if(zstarsc[i] == 1)
                g2.setColor(c);
            else
                g2.setColor(Color.black);
            g2.fillRect(zstarsx[i]+drawX,zstarsy[i]+drawY,zstarsd[i],zstarsd[i]);
        }
	}
	
	public void move(int scrollX, int scrollY){
	    if(count == step){
            count = 0;
            vx = -vx;
            vy = -vy;
        }
        else{
            x+=vx;
            y+=vy;
            box.translate(vx,vy);
        }
		drawX = x - scrollX;
		drawY = y - scrollY;
		count++;
	}
	
	public Color getColor(){return Color.cyan;}
	public void screenSlide(int bobX){x+=bobX; box.translate(bobX,0);}
	public Rectangle getWall(){return box;}
	public int getX(){return x;}
	public int getY(){return y;}
	public int getVY(){return vy;}
	public int getVX(){return vx;}
	public int getHeight(){return h;}
	public int getWidth(){return w;}
}