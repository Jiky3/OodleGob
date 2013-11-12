import java.util.Random;

public class Cat
{
    private boolean alive;
    private int offset;
    private int spawn;
    
    public Cat()
    {
        alive = true;
        Random r = new Random();
        offset = r.nextInt(3)+1;
        spawn = 1;
    }
    
    public void die()
    {
        alive = false;
        Spawn();
    }
    
    public void Spawn()
    {
        //Write latter
    }
    
    public void ChangeSpawnCount(int i)
    {
        spawn = i;
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public int getOffSet()
    {
        return offset;
    }
    
    public int getSpawnCount()
    {
        return spawn;
    }
}