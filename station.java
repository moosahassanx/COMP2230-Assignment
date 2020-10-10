public class station {
    // attributes
    private double xCoord;
    private double yCoord;

    // constructors
    public station()
    {
        this.xCoord = 0;
        this.yCoord = 0;
    }
    public station(double x, double y)
    {
        this.xCoord = x;
        this.yCoord = y;
    }

    // accessors
    public double getX()
    {
        return this.xCoord;
    }

    public double getY()
    {
        return this.yCoord;
    }

    // mutators
    public void setX(double x)
    {
        this.xCoord = x;
    }

    public void setY(double y)
    {
        this.yCoord = y;
    }
}
