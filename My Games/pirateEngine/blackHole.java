import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class blackHole extends RainbowGun
{   
    private ArrayList<ZBullet> bullet;
    private int ammo;
    private String gunName;
    private Color c;

    public blackHole(ArrayList<ZBullet> bullets)
    {
        super(bullets);
        gunName = "Just a Black Hole Gun Nothing More";
        bullet = new ArrayList<ZBullet>();
        bullet = bullets;
        ammo = 10;
    }

    public void bulletCreator(int x, int y, double mouseX, double mouseY, ArrayList<ZBullet> bullets)
    {
        bullet = bullets;
        c = Color.black;
        double bobX = x - mouseX;
        double bobY = y - mouseY;
        ZBullet bull = new ZBullet(0,0,(int)mouseX,(int)mouseY,5,10,c,this,0,0);
        bullet.add(bull);
        ammo --;
    }

    public void addAmmo(int plus)
    {
        ammo+=plus;
    }

    public int getAmmo() {return ammo;}

    public ArrayList<ZBullet> getBullet(){return bullet;}

    public String getGun(){return gunName;}
}
