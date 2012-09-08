import java.applet.*;
import java.util.ArrayList;

public class Gun
{   
    private ArrayList<Bullet> bullet;
    private String gunName;
    private boolean reloading;
    private int ammo, counter;

    public Gun(ArrayList<Bullet> bullets)
    {
        gunName = "Ye Old Revolver";
        bullet = new ArrayList<Bullet>();
        bullet = bullets;
        ammo = 6;
    }

    public void bulletCreator(int x, int y, boolean right, ArrayList<Bullet> bullets)
    {
        bullet = bullets;
        int vx  = 0;
        if(right)
            vx = 15;
        else
            vx = -15;
        if(!reloading){
            Bullet bull = new Bullet(vx,x,y,30,40);
            bullet.add(bull);
            ammo--;
            counter = 0;
        }
    }

    public void reload()
    {
        reloading = true;
        if(counter > 25){
            ammo++;
            counter = 0;
        }
        if(ammo == 6)
            reloading = false;
        counter++;
    }

    public void checkGun(boolean keyR){if(ammo == 0 || reloading || keyR) reload();}
    
    public boolean isReloading(){return reloading;}
    
    public int getAmmo() {return ammo;}

    public void counterPlus(){counter++;}

    public ArrayList<Bullet> getBullet(){return bullet;}

    public String getGun(){return gunName;}
}