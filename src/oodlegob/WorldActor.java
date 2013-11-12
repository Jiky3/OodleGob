import java.awt.image.ImageObserver;

/**
 * A solid actor that extends AbstractActor
 */
abstract public class WorldActor extends SolidActor
{
    /**
     * Creates a new SolidActor
     * @param imageName the name of the image
     * @param x the horizontal position
     * @param y the height from the ground
     * @param vXMax the maximum horizontal speed
     * @param vYMax the maximum vertical speed
     * @param obs the image observer
     */
    public WorldActor(String imageName, int x, int y, int vXMax, int vYMax, ImageObserver obs)
    {
        super(imageName, x, y, vXMax, vYMax, obs);
    }
}
