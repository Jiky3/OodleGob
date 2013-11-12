import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.*;

abstract public class AbstractActor
{
    private ImageObserver observer;

    private int vX;
    private int vY;
    
    //maximum vX
    private int maxVX;
    private int maxVY;
    
    private int aX;
    private int aY;
    
    //position of the lower-left corner of image
    private int xPos;
    private int yPos; //height above ground
    
    private int prevXPos;
    private int prevYPos;
    
    private boolean removeMe;
    
    private Image img;
    
    /**
     * Creates a new abstract actor object
     * @param imageName the name of the image of the actor
     * @param x the horizontal position from the left side of the window
     * @param y the vertical distance from the ground
     * @param vXMax the maximum horiztonal speed
     * @param vYMax the maximum vertical speed
     * @param obs the image observer
     */
    public AbstractActor(String imageName, int x, int y, int vXMax, int vYMax, ImageObserver obs)
    {
        img = null;
        try
        {
            img = ImageIO.read(new File(imageName));
        }
        
        catch(Exception e)
        {
            //e.printStackTrace();
        }
        
        maxVX = vXMax;
        maxVY = vYMax;
        xPos = x;
        yPos = y;
        prevXPos = x;
        prevYPos = y;
        aX = 0;
        aY = 0;
        vX = 0;
        vY = 0;
        observer = obs;
        removeMe = false;
    }
    
    /**
     * Returns the horizontal speed
     * @return the horizontal speed
     */
    public int getVX() {return vX;}
    
    /**
     * Returns the horizontal position
     * @return the horizontal position
     */
    public int getXPos() {return xPos;}
    
    /**
     * Returns the vertical speed
     * @return the vertical speed
     */
    public int getVY() {return vY;}
    
    /**
     * Returns the height from the ground
     * @return the height from the ground
     */
    public int getYPos() {return yPos;}
    
    /**
     * Returns the horizontal acceleration
     * @return the horizontal acceleration
     */
    public int getAX() {return aX;}    
    
    /**
     * Returns the vertical acceleration
     * @return the vertical acceleration
     */
    public int getAY() {return aY;}    
    
    /**
     * Gets the maximum horizontal speed
     * @return the maximum horizontal speed
     */
    public int getMaxVX() {return maxVX;}
    
    /**
     * Gets the maximum vertical speed
     * @return the maximum vertical speed
     */
    public int getMaxVY() {return maxVY;}
    
    /**
     * Returns the previous horizontal position
     * @return the previous horizontal position
     */
    public int getPrevXPos() {return prevXPos;}
    
    /**
     * Returns the previous vertical position
     * @return the previous vertical position
     */
    public int getPrevYPos() {return prevYPos;}
    
    /**
     * Determines if the actor should be removed from the grid
     * @return true if should be removed, false if should not
     */
    public boolean shouldRemove() {return removeMe;}
    
    /**
     * Returns the width of the actor
     * @return the width of the actor
     */
    public int getWidth()
    {
        return img.getWidth(observer);
    }
    
    /**
     * Returns the height of the actor
     * @return the height of the actor
     */
    public int getHeight()
    {
        return img.getHeight(observer);
    }
    
    /**
     * Returns the image of the actor
     * @return the image
     */
    public Image getImage() {return img;}
    
    /**
     * Returns the image observer
     * @return the image observer
     */
    public ImageObserver getImageObserver() {return observer;}
    
    /**
     * Sets the horizontal speed to v
     * @param v the new speed
     */
    public void setVX(int v)
    {
        vX = v;
    }
    
    /**
     * Sets the vertical speed to v
     * @param v the new speed
     */
    public void setVY(int v)
    {
       vY = v;
    }

    /**
     * Sets the horizontal position to x
     * @param x the new x position
     */
    public void setXPos(int x){xPos = x;}
    
    /**
     * Sets the vertical position to y
     * @param y the new height from the ground
     */
    public void setYPos(int y){yPos = y;}
    
    /**
     * Sets the new coordinates to x,y
     * @param x the new horizontal position
     * @param y the new vertical position
     */
    public void setPos (int x, int y) {xPos = x; yPos = y;}
    
    /**
     * Sets the horizontal acceleration to a
     * @param a the new horizontal acceleration
     */
    public void setAX(int a) {aX = a;}    
    
    /**
     * Sets the vertical acceleration to a
     * @param a the new vertical acceleration
     */
    public void setAY(int a) {aY = a;}

    /**
     * Sets the maximum horizontal velocity to v
     * @param v the new maximum horizontal velocity
     */
    public void setMaxVX(int v) {maxVX = v;}
    
    /**
     * Sets the maximum vertical velocity to v
     * @param v the new maximum vertical velocity
     */
    public void setMaxVY(int v) {maxVY = v;}
    
    /**
     * Sets the previous horizontal position to x
     * @param x the new horizontal position to be considered previous
     */
    public void setPrevXPos(int x) {prevXPos = x;}
    
    /**
     * Sets the previous horizontal position to y
     * @param y the new previous vertical position
     */
    public void setPrevYPos(int y) {prevYPos = y;}
    
    /**
     * Sets the value of removeMe to boo
     * @param boo the new value of removeMe
     */
    public void shouldRemove(boolean boo) {removeMe = boo;}
    
    /**
     * Sets a new image
     * @param imageName the name of the new image
     */
    public void setImage(String imageName)
    {
        try
        {
            img = ImageIO.read(new File(imageName));
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the image
     * @param image the new image
     */
    public void setImage(Image image)
    {
        img = image;
    }
    
    /**
     * Adds x to the x position
     * @param x the amount to be added to the distance from the left side of the window
     */
    public void addToXPos(int x) {xPos += x;}
    
    /**
     * Adds y to the y position
     * @param y the amount to be added to the distance from the height from the ground
     */
    public void addToYPos(int y) {yPos += y;}

    /**
     * Adds v to the vertical speed
     * @param v the amount to be added to the vertical speed
     */
    public void addToVY(int v)
    {
        vY += v;
        if (vY > maxVY)
        {
            vY = maxVY;
        }
        
        if (vY < -1 * maxVY)
        {
            vY = -1 * maxVY;
        }
    }
    
    /**
     * Adds v to the horizontal speed
     * @param v the amount to be added to the horizontal speed
     */
    public void addToVX(int v)
    {
        vX += v;
        if (vX > maxVX)
        {
            vX = maxVX;
        }
        
        if (vX < -1 * maxVX)
        {
            vX = -1 * maxVX;
        }
    }
    
    /**
     * Adds v to the vertical speed, ignoring the maximum speed
     * @param v the amount to be added to the vertical speed
     */
    public void addToVXIgnoreMax(int v) {vX += v;}
    
    /**
     * Adds v to the horizontal speed, ignoring the maximum speed
     * @param v the amount to be added to the horizontal speed
     */
    public void addToVYIgnoreMax(int v) {vY += v;}
    
    /**
     * Updates the state of the actor
     */
    public void updateState()
    {
        if(shouldRemove()) return;
        update();
    }
    
    /**
     * Updates the actor
     */
    abstract public void update();
    
    /**
     * Paints the graphics
     * @param g the graphics
     * @param levelX the x level
     * @param levelY the y level
     * @param sceneHeight the height of the window
     * @param gHeight the height of the graphics
     */
    public void paint(Graphics g, int levelX, int levelY, int sceneHeight, int gHeight)
    {
        int xCoord = xPos - levelX;
        int yCoord = levelY - yPos - sceneHeight + gHeight - getHeight();
        g.drawImage(img, xCoord, yCoord, observer);
    }
}