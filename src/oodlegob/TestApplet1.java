import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;

//Anything that had to do with double buffering came from
//http://www.javacooperation.gmxhome.de/BallBewegungEng.html

public class TestApplet1 extends Applet implements Runnable
{
    OodleGob gob = new OodleGob(this);
    SideWorld w = new SideWorld("Pics/background.gif", "Pics/BlueMatrix.gif", 221, gob);
    Block block = new Block("Pics/Electric_cube.png", 200, 0, this);
    Block block2 = new Block("Pics/Electric_cube.png", 230, 30, this);
    Block block3 = new Block("Pics/Electric_cube.png", 260, 60, this);
    Block block4 = new Block("Pics/Electric_cube.png", 290, 90, this);
    Block block5 = new Block("Pics/Electric_cube.png", 320, 120, this);
    Block block6 = new Block("Pics/Electric_cube.png", 350, 150, this);
  
    Block block8 = new Block("Pics/Electric_cube.png", 1040, 440, this);
    Block block9 = new Block("Pics/Electric_cube.png", 1180, 330, this);
    Block block10 = new Block("Pics/Electric_cube.png", 1320, 220, this);
    Block block11 = new Block("Pics/Electric_cube.png", 1460, 110, this);
    Block block12 = new Block("Pics/Electric_cube.png", 1600, 0, this);
    
    MewServer MS = new MewServer("Pics/MewServer.png", 1800, 0, this);
    
    Image dbImage;
    Graphics dbg;
    
    public void init()
    {
        w.addActor(block);
        w.addActor(block2);
        w.addActor(block3);
        w.addActor(block4);
        w.addActor(block5);
        w.addActor(block6);
        //w.addActor(block7);
        w.addActor(block8);
        w.addActor(block9);
        w.addActor(block10);
        w.addActor(block11);
        w.addActor(block12);
        
        w.addActor(MS);
        
        addKeyListener(gob);
        setSize(w.getSize());
    }

    public void start()
    {
        Thread th = new Thread(this);
        th.start();
    } 

    public void stop() {} 

    public void destroy() {} 

    public void run()
    {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while (true)
        {
            repaint();
            try
            {
                Thread.sleep(20);
            }
            
            catch(InterruptedException e) {e.printStackTrace();}
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    } 
    
    public void update (Graphics g)
    {
        if (dbImage == null)
        {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }
        
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
        dbg.setColor(getForeground());
        paint(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    public void paint (Graphics g)
    {
        w.paint(g);
    } 
}
