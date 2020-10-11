public class hotspot 
{
    // attributes
    private int ID;
    private float xCoord;
    private float yCoord;
    private hotspot next;
    private hotspot previous;

    // constructors
    public hotspot()
    {
        this.ID = 0;
        this.xCoord = 0;
        this.yCoord = 0;
        this.next = null;
        this.previous = null;
    }
    public hotspot(int i, float x, float y)
    {
        this.ID = i;
        this.xCoord = x;
        this.yCoord = y;
        this.next = null;
        this.previous = null;
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

    public hotspot getNext()
    {
        return this.next;
    }

    public hotspot getPrevious()
    {
        return this.previous;
    }


    // mutators
    public void setNext(hotspot d)
    {
        this.next = d;
    }

    public void setPrevious(hotspot h)
    {
        this.previous = h;
    }
}
