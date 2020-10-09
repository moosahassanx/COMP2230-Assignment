public class hotspot 
{
    // attributes
    private int ID;
    private float xCoord;
    private float yCoord;

    // constructor
    public hotspot(int i, float x, float y)
    {
        this.ID = i;
        this.xCoord = x;
        this.yCoord = y;
    }

    // accessors
    public int getID()
    {
        return this.ID;
    }
    
    public float getX()
    {
        return this.xCoord;
    }

    public float getY()
    {
        return this.yCoord;
    }
}
