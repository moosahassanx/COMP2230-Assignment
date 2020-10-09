import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

class kcluster 
{
    public static void main(String[] args) throws FileNotFoundException
    {
        // error report
        if(args[0] == null || args[1] == null)
        {
            // not enough parameters
            System.out.println("USAGE: java kcluster fileName.txt Integer");
            return;
        }
        else if(!(args[0].contains(".txt")))
        {
            // first parameter was not a string with .txt extension was not inputted
            System.out.println("USAGE: java kcluster fileName.txt Integer");
            return;
        }
        else if(!(args[1].matches(".*\\d.*")))
        {
            // second parameter was not an int
            System.out.println("USAGE: java kcluster fileName.txt Integer");
            return;
        }

        // fetching data from parameters
        Scanner file = new Scanner(new File(args[0]));          // ID, x-coord, y-coord
        int fireStations = Integer.parseInt(args[1]);

        ArrayList<hotspot> hotspotList = new ArrayList<hotspot>();

        try
        {
            while(file.hasNext())
            {
                String newText = file.nextLine();

                String[] splitStr = newText.split(",");
                int id = Integer.parseInt(splitStr[0]);             // retrieving ID
                float xCoord = Float.parseFloat(splitStr[1]);       // retrieving x-coordinate
                float yCoord = Float.parseFloat(splitStr[2]);       // retrieving y-coordinate
                
                // creating object and adding to list
                hotspot hotspotObject = new hotspot(id, xCoord, yCoord);
                hotspotList.add(hotspotObject);
            }
        }
        catch(Exception e)
        {
            System.out.println("File reading error.");
        }

        // output
        System.out.println("Hello and welcome to Kruskal’s Clustering! \n");
        System.out.println("The weighted graph of hotspots: \n");

        graph(hotspotList);         // graphing the lines of hotspots

        System.out.println("There are " + hotspotList.size() + " hotspots.");
        System.out.println("You have requested " + fireStations + " temporary fire stations. \n");

        locateStations(hotspotList, fireStations);

        // for (int i = 0; i < fireStations; i++) 
        // {
        //     System.out.println("Station " + i + 1 + ": ");
        //     System.out.println("Coordinates: (" + 2.0 + ", " + 2.67 + ")");                 // TODO: replace 2.0 and 2.67
        //     System.out.println("Hotspots: {" + 1 + ", " + 2 + ", " + 3 + "} \n");           // TODO: replace 1, 2 and 3
        // }

        System.out.println("Inter-clustering distance: " + 5.00 + "\n");                    // TODO: replace 5

        System.out.println("Thank you for using Kruskal’s Clustering. Bye.");

    }

    public static void graph(ArrayList<hotspot> hList) 
    {
        // staying element
        for(int i = 0; i < hList.size(); i++)
        {
            // iterating element
            for(int j = 0; j < hList.size(); j++)
            {
                // euclidean algorithm divided into 3 sections
                float xCalc = ((hList.get(i).getX() - hList.get(j).getX()) * (hList.get(i).getX() - hList.get(j).getX()));
                float yCalc = ((hList.get(i).getY() - hList.get(j).getY()) * (hList.get(i).getY() - hList.get(j).getY()));
                double answer = Math.sqrt(xCalc + yCalc);
                
                // displaying output
                if(answer == 0)
                {
                    System.out.print("0 ");
                }
                else
                {
                    DecimalFormat df = new DecimalFormat("###.##");
                    System.out.print(df.format(answer));
                    System.out.print(" ");
                }
            }
            System.out.println();       // spacing
        }
        System.out.println();   // spacing
    }

    public static void locateStations(ArrayList<hotspot> hList, int stations)
    {
        //
    }
    
}