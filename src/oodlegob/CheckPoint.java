import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.Graphics;

/**
 * Creates a stationary block that extends SolidActor
 */
public class CheckPoint extends WorldActor
{
    private static final int interact = 4;

    /**
     * Creates a new stationary block that sets the
     * maximum speed of the block to 0
     * @param imageName the name of the image
     * @param x the horizontal position
     * @param y the vertical position
     * @param obs the image observer
     */
    public CheckPoint(String imageName, int x, int y, ImageObserver obs)
    {
        super(imageName, x, y, 0, 0, obs);
    }
    
    public void update(){}
    
    public int interact()
    {
        return interact;
    }
}