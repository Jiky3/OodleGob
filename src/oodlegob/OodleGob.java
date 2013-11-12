import java.util.Random;
import java.awt.Color;

import java.awt.image.ImageObserver;
import java.awt.event.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;

/**
 * A OodleGob class that extends AbstractActor and implements KeyListener
 */
public class OodleGob extends AbstractActor implements KeyListener
{
    public static final int R1 = KeyEvent.VK_RIGHT;
    public static final int R2 = KeyEvent.VK_KP_RIGHT;
    public static final int L1 = KeyEvent.VK_LEFT;
    public static final int L2 = KeyEvent.VK_KP_LEFT;
    public static final int U1 = KeyEvent.VK_UP;
    public static final int U2 = KeyEvent.VK_KP_UP;
    public static final int P = KeyEvent.VK_P;

    private int dir;
    public static final int RIGHT = 1;
    public static final int LEFT = -1;

    private SolidActor top;
    private SolidActor bott;
    private SolidActor left;
    private SolidActor right;

    private boolean rightKey;
    private boolean leftKey;
    private boolean upKey;

    public static final int FRICTION = 1;
    public final int ACCELERATION = 1;
    public final int V_JUMP = 10;

    private int stepsTaken;
    private int prevStepsTaken;
    private Image rightFoot;
    private Image leftFoot;
    private Image Eating;
    private Image still;
    private boolean currentFoot; //true for right, false for left
    private boolean isEating;
    private boolean keyP;

    private int offSet = 0;
    private Color color;
    private Random random = new Random();
    private boolean alive;
    
    /**
     * Creates a new OodleGob with no maximum vertical speed
     * @param obs the image observer
     */
    public OodleGob(ImageObserver observer)
    {
        super("Pics/Greendlegob.png", -SideWorld.BORDER, 0, 6, Integer.MAX_VALUE, observer);

        try
        {
            rightFoot = ImageIO.read(new File("Pics/Greendlegob.png"));
            leftFoot = ImageIO.read(new File("Pics/Orangedlegob.png"));
            Eating = ImageIO.read(new File("Pics/Bludlegob.png"));
            still = ImageIO.read(new File("Pics/Redlegob.png"));
        }

        catch(Exception e) {}

        stepsTaken = 0;
        prevStepsTaken = 0;

        upKey = false;
        leftKey = false;
        rightKey = false;

        right = null;
        left = null;
        top = null;
        bott = null;

        currentFoot = true;
        isEating = false;
        keyP = false;
        
        alive = true;
        offSet = random.nextInt(3);
        color = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
    }

    /**
     * Updates the state of the OodleGob
     */
    public void update()
    {
        setPrevXPos(getXPos());
        setPrevYPos(getYPos());
        prevStepsTaken = stepsTaken;

        boolean updateX = true;
        boolean updateY = true;

        if (isAirborn())
        {
            setAX(0);
            setAY(SideWorld.G);
        }
        else if(upKey)
        {
            setVY(V_JUMP);
            setAY(SideWorld.G);
            setAX(0);
        }

        //deal with friction on the ground
        if(!isAirborn() && !rightKey && !leftKey && bott == null)
        {
            if (getVX() < -FRICTION)
            {
                setAX(FRICTION);
            }

            else if (getVX() > FRICTION)
            {
                setAX(-FRICTION);
            }

            else
            {
                setAX(0);
                setVX(0);
            }
        }

        //deal with friction on a moving block
        if(!isAirborn() && !rightKey && !leftKey && bott != null)
        {
            if (-bott.getVX() + getVX() < -FRICTION)
            {
                setAX(FRICTION);
            }

            else if (-bott.getVX() + getVX() > FRICTION)
            {
                setAX(-FRICTION);
            }

            else
            {
                setAX(0);
                setVX(bott.getVX());
            }
        }

        if(rightKey && getVX() < 0)
        {
            setAX(ACCELERATION + FRICTION);
        }
        else if(rightKey)
        {
            setAX(ACCELERATION);
        }

        if(leftKey && getVX() > 0)
        {
            setAX(-ACCELERATION - FRICTION);
        }
        else if(leftKey)
        {
            setAX(-ACCELERATION);
        }

        addToVX(getAX());
        addToXPos(getVX());
        addToVY(getAY());
        addToYPos(getVY());

        if(left != null)
        {
            if(getXPos() <= left.getXPos() + left.getWidth() && getVX() < left.getVX())
            {
                setXPos(left.getXPos() + left.getWidth());
                setVX(left.getVX());
                setAX(left.getAX());
                updateX = false;
            }
        }

        if(right != null)
        {
            if(getXPos() >= right.getXPos() - getWidth() && getVX() > right.getVX())
            {
                setXPos(right.getXPos() - getWidth());
                setVX(right.getVX());
                setAX(right.getAX());
                updateX = false;
            }
        }

        if(top != null)
        {
            if(getYPos() >= top.getYPos() - getHeight() && getVY() > top.getVY())
            {
                setYPos(top.getYPos() - getHeight());
                setVY(top.getVY());
                setAY(top.getAY());
                updateY = false;
            }
        }

        if(bott != null)
        {
            if(getYPos() <= bott.getYPos() + bott.getHeight() && getVY() < bott.getVY())
            {
                setYPos(bott.getYPos() + bott.getHeight());
                setVY(bott.getVY());
                setAY(bott.getAY());
                updateY = false;
            }
        }

        if(updateX)
        {
            if(!isAirborn())
            {
                if(getBottom() != null)
                    stepsTaken += Math.abs(getVX() - getBottom().getVX());

                else
                    stepsTaken += Math.abs(getVX());
            }
        }

        if(updateY)
        {
            if(getYPos() <= 0 && getVY() < 0)
            {
                setVY(0);
                setAY(0);
                setYPos(0);
            }
        }

        if(keyP && canEat())
        {
            isEating = true;
        }
        else isEating = false;

        chooseImage();
    }

    /**
     * Paints the OodleGob
     * @param g the graphics
     * @param levelX the x level
     * @param levelY the y level
     * @param sceneHeight the height of the scene
     * @param gHeight the height of the graphics
     */
    public void paint(Graphics g, int levelX, int levelY, int sceneHeight, int gHeight)
    {        
        if(dir == RIGHT)
            super.paint(g, levelX, levelY, sceneHeight, gHeight);
        else
        {
            int xCoord = getXPos() - levelX;
            int yCoord = levelY - getYPos() - sceneHeight + gHeight - getHeight();
            g.drawImage (getImage(), 
                xCoord + getWidth(), yCoord, xCoord, yCoord + getHeight(),
                0, 0, getWidth(), getHeight(),
                getImageObserver());
        }
    }

    /**
     * Chooses image of the OodleGob
     */
    private void chooseImage()
    {
        if(isEating)
        {
            setImage(Eating);
            return;
        }

        if(canEat() || isAirborn())
        {
            setImage(still);
            return;
        }

        if(stepsTaken/50 > prevStepsTaken/50)
        {
            if(currentFoot)
            {
                setImage(leftFoot);
            }
            else
            {
                setImage(rightFoot);
            }
            currentFoot = !currentFoot;
        }
    }

    /**
     * Tests if OodleGob is in contact with a solid actor
     * @param a the actor to be tested
     * @return true if the OodleGob is in range of an actor,
    false if OodleGob is not in range of an actor
     */
    private boolean contacts(SolidActor a)
    {
        //if right edge is within range of a
        if(getXPos() + getVX() + getAX() + getWidth() >= a.getXPos() && 
        getXPos() + getVX() + getAX() + getWidth() <= a.getXPos() + a.getWidth())
        {
            //if bottom right corner is contained by a
            if(getYPos() + getVY() + getAY() <= a.getYPos() + a.getHeight() &&
            getYPos() + getVY() + getAY() >= a.getYPos())
            {
                return true;
            }

            //if top right corner is contained by a
            if(getYPos() + getVY() + getAY() + getHeight() <= a.getYPos() + a.getHeight() &&
            getYPos() + getVY() + getAY() + getHeight() >= a.getYPos())
            {
                return true;
            }

            //if right edge is entirely contained by a
            if(getYPos() + getVY() + getAY() + getHeight() <= a.getYPos() + a.getHeight() &&
            getYPos() + getVY() + getAY() >= a.getYPos())
            {
                return true;
            }

            //if this contains a's entire left edge
            if(getYPos() + getVY() + getAY() + getHeight() >= a.getYPos() + a.getHeight() &&
            getYPos() + getVY() + getAY() <= a.getYPos())
            {
                return true;
            }
        }

        //if left edge is within range of a
        if(getXPos() + getVX() + getAX() >= a.getXPos() && 
        getXPos() + getVX() + getAX() <= a.getXPos() + a.getWidth())
        {
            //if bottom left corner is contained by a
            if(getYPos() + getVY() + getAY() <= a.getYPos() + a.getHeight() &&
            getYPos() + getVY() + getAY() >= a.getYPos())
            {
                return true;
            }

            //if top left corner is contained by a
            if(getYPos() + getVY() + getAY() + getHeight() <= a.getYPos() + a.getHeight() &&
            getYPos() + getVY() + getAY() + getHeight() >= a.getYPos())
            {
                return true;
            }

            //if left edge is entirely contained by a
            if(getYPos() + getVY() + getAY() >= a.getYPos() &&
            getYPos() + getVY() + getAY() + getHeight() <= a.getYPos() + a.getHeight())
            {
                return true;
            }

            //if this contains a's entire right edge
            if(getYPos() + getVY() + getAY() <= a.getYPos() &&
            getYPos() + getVY() + getAY() + getHeight() >= a.getYPos() + a.getHeight())
            {
                return true;
            }
        }

        //if bottom and top edges are within the range of a
        if (getXPos() + getVX() + getAX() >= a.getXPos() && 
        getXPos() + getVX() + getAX() + getWidth() <= a.getXPos() + a.getWidth())
        {
            //if bottom edge of this is entirely contained by a
            if(getYPos() + getVY() + getAY() >= a.getYPos() &&
            getYPos() + getVY() + getAY() <= a.getYPos() + a.getHeight())
            {
                return true;
            }

            //if bottom edge of this is entirely contained by a
            if(getYPos() + getVY() + getAY() + getHeight() >= a.getYPos() &&
            getYPos() + getVY() + getAY() + getHeight() <= a.getYPos() + a.getHeight())
            {
                return true;
            }
        }

        //if bottom and top edges of a are within the range of this
        if (getXPos() + getVX() + getAX() <= a.getXPos() && 
        getXPos() + getVX() + getAX() + getWidth() >= a.getXPos() + a.getWidth())
        {
            //if bottom edge of a is entirely contained by this
            if(getYPos() + getVY() + getAY() >= a.getYPos() &&
            getYPos() + getVY() + getAY() + getHeight() <= a.getYPos())
            {
                return true;
            }

            //if top edge of a is entirely contained by this
            if(getYPos() + getVY() + getAY() + getHeight() >= a.getYPos() + a.getHeight() &&
            getYPos() + getVY() + getAY() <= a.getYPos() + a.getHeight())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Updates the state of contact with an actor
     * @param a the actor to be tested
     */
    public void updateActorContact(SolidActor a)
    {
        if(a.shouldRemove())
        {
            if(top == a) top = null;
            if(bott == a) bott = null;
            if(left == a) left = null;
            if(right == a) right = null;
            return;
        }

        if(contacts(a))
        {
            if(getXPos() >= a.getPrevXPos() + a.getWidth() ||
            getPrevXPos() >= a.getXPos() + a.getWidth() ||
            getPrevXPos() >= a.getPrevXPos() + a.getWidth())
            {
                left = a;
                if(top == a) top = null;
                if(bott == a) bott = null;
                if(right == a) right = null;
                return;
            }

            if(getXPos() + getWidth() <= a.getPrevXPos() ||
            getPrevXPos() + getWidth() <= a.getXPos() ||
            getPrevXPos() + getWidth() <= a.getPrevXPos())
            {
                right = a;
                if(top == a) top = null;
                if(left == a) left = null;
                if(bott == a) bott = null;
                return;
            }

            if(getYPos() >= a.getPrevYPos() + a.getHeight() ||
            getPrevYPos() >= a.getYPos() + a.getHeight() ||
            getPrevYPos() >= a.getPrevYPos() + a.getHeight())
            {
                bott = a;
                if(top == a) top = null;
                if(left == a) left = null;
                if(right == a) right = null;
                return;
            }

            if(getYPos() + getHeight() <= a.getPrevYPos() ||
            getPrevYPos() + getHeight() <= a.getYPos() ||
            getPrevYPos() + getHeight() <= a.getPrevYPos())
            {
                top = a;
                if(bott == a) bott = null;
                if(left == a) left = null;
                if(right == a) right = null;
                return;
            }
        }

        if(top == a) top = null;
        if(bott == a) bott = null;
        if(left == a) left = null;
        if(right == a) right = null;
    }

    /**
     * Enters the key pressed
     * @param e the key event
     */
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();

        if(code == P)
        {
            keyP = true;
        }

        if (code == R1 || code == R2)
        {
            rightKey = true;
            dir = RIGHT;
        }

        if (code == L1 || code == L2)
        {
            leftKey = true;
            dir = LEFT;
        }

        if (code == U1 || code == U2)
        {
            if(!isAirborn() && !upKey)
            {
                upKey = true;
            }

            else
            {
                upKey = false;
            }
        }
    }

    /**
     * Releases the key
     * @param e the key to be released
     */
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();

        if(code == P)
        {
            keyP = false;
        }

        if(code == R1 || code == R2)
        {
            rightKey = false;
        }

        if (code == L1 || code == L2)
        {
            leftKey = false;
        }

        if (code == U1 || code == U2)
        {
            upKey = false;
        }
    }

    /**
     * Tests if OodleGob is airborn
     * @param true if in air, false if not
     */
    public boolean isAirborn()
    {
        return getYPos() > 0 && bott == null;
    }

    /**
     * Gets the top
     * @return the solid actor on top of OodleGob
     */
    public SolidActor getTop() {return top;}

    /**
     * Gets the bottom actor
     * @return the solid actor below OodleGob
     */
    public SolidActor getBottom() {return bott;}

    /**
     * Gets the actor to the left of OodleGob
     * @return the actor to the left of OodleGob
     */
    public SolidActor getLeft() {return left;}

    /**
     * Gets the actor to the right of OodleGob
     * @return the actor to the right of OodleGob
     */
    public SolidActor getRight() {return right;}

    /**
     * Gets the direction of OodleGob
     * @return the direction
     */
    public int getDirection() {return dir;}

    /**
     * Determines if OodleGob can Eat
     * @return true if can Eat, false if can't
     */
    public boolean canEat()
    {
        if(isAirborn()) return false;
        if(bott != null && getVX() == bott.getVX() && getVY() == bott.getVY()
        && getAY() == bott.getAY() && getAX() == bott.getAX()) return true;
        if(left != null && getVX() == left.getVX() && getAX() == left.getAX()) return true;
        if(right != null && getVX() == right.getVX()  && getAX() == right.getAX()) return true;
        if(getVX() == 0 && getVY() == 0 && getAX() == 0 && getAY() == 0) return true;

        return false;
    }

    /**
     * Determines if OodleGob is Eating
     * @return true if is Eating, false if not
     */
    public boolean isEating() {return isEating;}

    /**
     * Changes the Eating state to Eating
     * @param Eating the value of whether OodleGob is Eating
     */
    public void isEating(boolean Eating) {isEating = Eating;}

    /**
     * Dear Svetty,
     * Poopie
     * Love,
     * TomJak
     */
    public void keyTyped(KeyEvent e){}
    
    public void die()
    {
        alive = false;
    }

    public int getOffSet()
    {
        return offSet;
    }

    public boolean alive()
    {
        return alive;
    }
}