// TITLE: 					Algorithms Assignment
// COURSE: 					COMP2230
// AUTHOR: 					Moosa Hassan
// STUDENT NUMBER: 			3331532
// DATE: 					24/10/2020 
// DESCRIPTION: 			taken by the input file, stores coordinates, id and other hotspots this one is connected to

public class hotspot 
{
    // attributes
    private final int ID;
    private final float xCoord;
    private final float yCoord;
    private hotspot next;
    private hotspot previous;

    // constructors
    public hotspot() {
        this.ID = 0;
        this.xCoord = 0;
        this.yCoord = 0;
        this.next = null;
        this.previous = null;
    }

    public hotspot(final int i, final float x, final float y) {
        this.ID = i;
        this.xCoord = x;
        this.yCoord = y;
        this.next = null;
        this.previous = null;
    }

    // accessors
    public int getID() {
        return this.ID;
    }

    public float getX() {
        return this.xCoord;
    }

    public float getY() {
        return this.yCoord;
    }

    public hotspot getNext() {
        return this.next;
    }

    public hotspot getPrevious() {
        return this.previous;
    }

    // mutators
    public void setNext(final hotspot d) {
        this.next = d;
    }

    public void setPrevious(final hotspot h)
    {
        this.previous = h;
    }
}
