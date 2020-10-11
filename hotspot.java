public class hotspot 
{
    // attributes
    private int ID;
    private float xCoord;
    private float yCoord;
    private hotspot destination;

    // constructor
    public hotspot(int i, float x, float y)
    {
        this.ID = i;
        this.xCoord = x;
        this.yCoord = y;
        this.destination = null;
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

    public hotspot getDestination()
    {
        return this.destination;
    }

    // mutators
    public void setDestination(hotspot d)
    {
        this.destination = d;
    }
}
