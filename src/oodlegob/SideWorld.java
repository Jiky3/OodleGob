import java.util.ArrayList;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.*;

/**
 * Creates a side world where abstract actors can interact
 */
public class SideWorld
{
    public static final int G = -1;
    
    //minimum distance from the edge of the screen
    //the main actor can go before the landscape starts scrolling
    public static final int BORDER = 125;
    public static final int BUFFER_CUSHION = 10;
    
    private ArrayList<AbstractActor> actors;
    private ArrayList<SolidActor> solids;
    private OodleGob main;
    private Image backImg; //background image
    private Image skyImg;
    private int gHeight; //Distance from ground to top of screen
    
    //where in the level the left edge of the screen is
    private int x;
    //where in the level the top edge of the screen is
    private int y;
    
    /**
     * Creates a new side world
     * @param backgroundImageName the name of the image of the background
     * @param skyImageName the name of the image of the sky
     * @param groundHeight the height of the ground on the screen
     * @param mainActor the main actor of the world
     */
    public SideWorld(String backgroundImageName, String skyImageName, 
                     int groundHeight, OodleGob mainActor)
    {
        backImg = null;
        try
        {
            backImg = ImageIO.read(new File(backgroundImageName));
            skyImg = ImageIO.read(new File(skyImageName));
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        gHeight = groundHeight;
        actors = new ArrayList<AbstractActor>();
        solids = new ArrayList<SolidActor>();
        main = mainActor;
        x = 0;
        y = backImg.getHeight(main.getImageObserver());
    }
    
    /**
     * Returns the dimensions of the sideworld
     * @return the dimension of sideworld
     */
    public Dimension getSize()
    {
        return new Dimension (backImg.getWidth(main.getImageObserver()),
                              backImg.getHeight(main.getImageObserver()));
    }
    
    /**
     * Adds an actor to the side world
     * @param a the actor to be added to the world
     */
    public void addActor(AbstractActor a)
    {
        actors.add(a);

        if(a instanceof SolidActor)
            solids.add((SolidActor) a);
    }
    
    /**
     * Updates the state of the world
     */
    private void update()
    {
        for (int i = 0; i < actors.size(); i++)
        {
            if(actors.get(i).shouldRemove())
            {
                actors.remove(i);
                i--;
            }
        }
        
        
        for (int i = 0; i < solids.size(); i++)
        {
            if(solids.get(i).shouldRemove())
            {
                solids.remove(i);
                i--;
            }
        }
        
        
        
        ImageObserver obs = main.getImageObserver();
        int width = backImg.getWidth(obs);
        int height = backImg.getHeight(obs);
        
        for (AbstractActor a : actors)
        {
            //if(isOnScreen(a))
                a.updateState();
        }
        
        
        //update mainActor and egdes of screen
        for(SolidActor s : solids)
        {
            if(isOnScreen(s))
            {
                main.updateActorContact(s);
            }
        }
        main.updateState();
        
        
        if(main.getXPos() < x + BORDER)
        {
            x = main.getXPos() - BORDER;
        }
        
        int rightEdge = x + backImg.getWidth(obs);
        if(main.getXPos() + main.getWidth() > rightEdge - BORDER)
        {
            x = main.getXPos() - width + BORDER + main.getWidth();
        }
        
        
        /*if(main.getYPos() <= 0)
        {
            y = height;
        }
        else
        {*/
        int bottEdge = y - height;
        int bott = height - gHeight;
        if(main.getYPos() + main.getHeight() - gHeight + height > y - BORDER)
        {
            y = main.getYPos() + main.getHeight() - gHeight + height + BORDER;
        }
    
        if(main.getYPos() - gHeight + height < y - height + bott)
        {
            y = main.getYPos() - gHeight + height + height - bott;
        }
        //}
        
        
        //y = main.getYPos() - gHeight + height + height - bott;
        //update everything else
        
    }
    
    /**
     * Tests if an actor is on the screen
     * @param a the actor to be tested
     * @return true if the actor is on the screen, false if not
     */
    private boolean isOnScreen(AbstractActor a)
    {
        int rightEdge = x + backImg.getWidth(main.getImageObserver());
        int height = backImg.getHeight(main.getImageObserver());
        
        if(a instanceof Block)
        {
            return (a.getXPos() + a.getWidth() >= x && 
                a.getXPos() <= rightEdge); 
        }
        
        return (a.getXPos() + a.getWidth() >= x && 
                a.getXPos() <= rightEdge && 
                a.getYPos() + a.getHeight() + height - gHeight >= y - height && 
                a.getYPos() + height - gHeight <= y); 
    }
    
    /**
     * Paints side world
     * @param g the graphics
     */
    public void paint(Graphics g)
    {
        update();
        ImageObserver observer = main.getImageObserver();
        int width = backImg.getWidth(observer);
        int height = backImg.getHeight(observer);
        int leftEdge = x % width;
        int topEdge = y % height;
        if(y < height)
        {           
            g.drawImage(backImg, -leftEdge, 0, observer);
            
            if(leftEdge + BORDER >= main.getVX() - main.getAX() - BUFFER_CUSHION)
            {
                g.drawImage(backImg, -leftEdge + width, 0, observer);
            }
            
            if(leftEdge - BORDER <= main.getVX() + main.getAX() + BUFFER_CUSHION)
            {
                g.drawImage(backImg, -leftEdge - width, 0, observer);
            }
        }
        
        else if (y < 2 * height)
        {
            g.drawImage(backImg, -leftEdge, topEdge, observer);
            g.drawImage(skyImg, -leftEdge, topEdge - height, observer);
            
            if(leftEdge + BORDER >= main.getVX() - main.getAX() - BUFFER_CUSHION)
            {
                g.drawImage(backImg, -leftEdge + width, topEdge, observer);
                g.drawImage(skyImg, -leftEdge + width, topEdge - height, observer);
            }
            
            if(leftEdge - BORDER <= main.getVX() + main.getAX() + BUFFER_CUSHION)
            {
                g.drawImage(backImg, -leftEdge - width, topEdge, observer);
                g.drawImage(skyImg, -leftEdge - width, topEdge - height, observer);
            }    
        }
        
        else
        {
            g.drawImage(skyImg, -leftEdge, topEdge, observer);
            g.drawImage(skyImg, -leftEdge, topEdge - height, observer);
            
            if(leftEdge + BORDER >= main.getVX() - main.getAX() - BUFFER_CUSHION)
            {
                g.drawImage(skyImg, -leftEdge + width, topEdge, observer);
                g.drawImage(skyImg, -leftEdge + width, topEdge - height, observer);
            }
            
            if(leftEdge - BORDER <= main.getVX() + main.getAX() + BUFFER_CUSHION)
            {
                g.drawImage(skyImg, -leftEdge - width, topEdge, observer);
                g.drawImage(skyImg, -leftEdge - width, topEdge - height, observer);
            }
        }
        
        for (AbstractActor a : actors)
        {
            if (isOnScreen(a))
                a.paint(g, x, y, height, gHeight);
        }
        main.paint(g, x, y, height, gHeight);
        
        
        //FOR DEBUGGING
        g.setColor(new Color(204, 85, 0));
        int y = 20;
        
        //g.drawString("Pee score: " + main.peeScore(), 5, y);
        //y+= 20;
        
        /*
        g.drawString("Top edge: " + this.y, 5, y);
        y+= 20;
        
        g.drawString("xPos: " + main.getXPos(), 5, y);
        y+= 20;
        
        g.drawString("yPos: " + main.getYPos(), 5, y);
        y+= 20;
        
        g.drawString("vX: " + main.getVX(), 5, y);
        y+= 20;
        
        g.drawString("vY: " + main.getVY(), 5, y);
        y+= 20;
        
        g.drawString("aX: " + main.getAX(), 5, y);
        y+= 20;
        
        g.drawString("aY: " + main.getAY(), 5, y);
        y+= 20;
        
        if(!solids.isEmpty() && solids.get(0) instanceof Hydrant)
        {
            g.drawString("numPees: " + ((Hydrant)solids.get(0)).getNumPees(), 5, y);
            y+= 20;
        }
        
        if(main.canPee())
        {
            g.drawString("Can pee", 5, y);
            y += 20;
        }
        
        if(main.getTop() != null)
        {
            g.drawString("Has top", 5, y);
            y += 20;
        }
        
        if(main.getBottom() != null)
        {
            g.drawString("Has bottom", 5, y);
            y += 20;
        }
        
        if(main.getLeft() != null)
        {
            g.drawString("Has left", 5, y);
            y += 20;
        }
        
        if(main.getRight() != null)
        {
            g.drawString("Has right", 5, y);
            y += 20;
        }
        */
    }
}