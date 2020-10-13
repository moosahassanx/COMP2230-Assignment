// TITLE: 					Algorithms Assignment
// COURSE: 					COMP2230
// AUTHOR: 					Moosa Hassan
// STUDENT NUMBER: 			3331532
// DATE: 					24/10/2020 
// DESCRIPTION: 			used to store and display information about coordinates and the id of the station

public class station 
{
    // attributes
    private double xCoord;
    private double yCoord;

    // constructors
    public station()
    {
        this.xCoord = 0;
        this.yCoord = 0;
    }
    public station(final double x, final double y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    // accessors
    public double getX() {
        return this.xCoord;
    }

    public double getY() {
        return this.yCoord;
    }

    // mutators
    public void setX(final double x) {
        this.xCoord = x;
    }

    public void setY(final double y)
    {
        this.yCoord = y;
    }
}
