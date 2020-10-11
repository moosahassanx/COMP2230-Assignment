import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


// INTER-CLUSTERING: distance between P3 and P4 = 5
// INTRA-CLUSTERING: distance between P1 and P4 = 9.22

// CENTROID: average x-coordinates of all the points in the cluster (and same thing with y)

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
            System.out.println("File reading error: " + e);
        }

        // output
        System.out.println("Hello and welcome to Kruskal’s Clustering! \n");
        System.out.println("The weighted graph of hotspots: \n");

        printGraph(hotspotList); // graphing the lines of hotspots

        System.out.println("There are " + hotspotList.size() + " hotspots.");
        System.out.println("You have requested " + fireStations + " temporary fire stations. \n");

        locateStations(hotspotList, fireStations);      // TODO: do firestation coding

        System.out.println("Inter-clustering distance: " + 5.00 + "\n"); // TODO: replace 5

        System.out.println("Thank you for using Kruskal’s Clustering. Bye.");

    }

    public static void printGraph(ArrayList<hotspot> hList)
    {
        // staying element
        for (int i = 0; i < hList.size(); i++) 
        {
            // iterating element
            for (int j = 0; j < hList.size(); j++) 
            {
                // euclidean algorithm divided into 3 sections
                float xCalc = ((hList.get(i).getX() - hList.get(j).getX()) * (hList.get(i).getX() - hList.get(j).getX()));
                float yCalc = ((hList.get(i).getY() - hList.get(j).getY()) * (hList.get(i).getY() - hList.get(j).getY()));
                double answer = Math.sqrt(xCalc + yCalc);

                // displaying output
                if (answer == 0) 
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
            System.out.println(); // spacing
        }
        System.out.println(); // spacing
    }

    public static void locateStations(ArrayList<hotspot> hList, int stations)
    {
        ArrayList<station> stationList = new ArrayList<station>();

        // adding blank stations to stationlist
        for(int i = 0; i < stations; i++)
        {
            stationList.add(new station());
        }

        makeClusters(hList, stations);

        // TODO: find out where the which hotspots are in which cluster
        // TODO: find out where the firestations would be located in that cluster

        // for each station
        for (int i = 0; i < stations; i++)
        {
            System.out.println("Station " + (i + 1) + ": ");
            System.out.println("Coordinates: (" + stationList.get(i).getX() + ", " + stationList.get(i).getY() + ")");
            System.out.println("Hotspots: " + hotspotPrinter(stationList.get(i)) + "\n");
        }
    }

    public static void makeClusters(ArrayList<hotspot> hList, int stations)
    {
        ArrayList<edges> edgeList = collectEdges(hList);
        ArrayList<edges> MSTedges =  kruskals(edgeList, hList, stations);

        System.out.println("Tree Construction: ");
        for(int i = 0; i < MSTedges.size(); i++)
        {
            MSTedges.get(i).printer();
        }

        // transfer edges data to hotspots
        for(int i = 0; i < MSTedges.size(); i++)
        {
            for(int j = 0; j < hList.size(); j++)
            {
                if(MSTedges.get(i).getSrc() == hList.get(j).getID())
                {
                    int destIndex = MSTedges.get(i).getDest() - 1;
                    hList.get(j).setDestination(hList.get(destIndex));
                }
            }
        }


        // define clusters using that data on the hotspots
        ArrayList<ArrayList<hotspot> > clusterOfEdges =  recTest(hList, stations);

        for(hotspot h : hList)
        {
            if(h.getDestination() == null)
            {
                System.out.println(h.getID() + " -> null");
            }
            else
            {
                System.out.println(h.getID() + " -> " + h.getDestination().getID());
            }
        }

        for(int i = 0; i < stations; i++)
        {
            //
        }



        /*
        //TODO: COME BACK TO THIS
        ArrayList<ArrayList<edges> > clusterOfEdges =  recTest(MSTedges, stations);

        // big array iterator
        for(int i = 0; i < clusterOfEdges.size(); i++)
        {
            // little array iterator
            for(int j = 0; j < clusterOfEdges.get(i).size(); j++)
            {
                //
            }
        }
        */

    }

    public static ArrayList<ArrayList<hotspot> > recTest(ArrayList<hotspot> eList, int clusters)
    {
        ArrayList<ArrayList<hotspot> > clusterOfEdges = new ArrayList<ArrayList<hotspot> >();

        //
        for(int i = 0; i < clusters; i++)
        {
            ArrayList<hotspot> miniList = new ArrayList<hotspot>();

            miniList = makeMiniTree(eList);

            clusterOfEdges.add(miniList);
        }

        return clusterOfEdges;
        
    }

    public static ArrayList<hotspot> makeMiniTree(ArrayList<hotspot> bigTree)
    {
        ArrayList<hotspot> lilTree = new ArrayList<hotspot>();

        return lilTree;
    }

    public static String hotspotPrinter(station s)
    {
        String output = "";

        //

        return output;
    }

    public static ArrayList<edges> collectEdges(ArrayList<hotspot> hList) 
    {
        ArrayList<edges> edgeList = new ArrayList<edges>();

        // staying element
        for (int i = 0; i < hList.size(); i++) 
        {
            // iterating element
            for (int j = 0; j < hList.size(); j++) 
            {
                // euclidean algorithm divided into 3 sections
                float xCalc = ((hList.get(i).getX() - hList.get(j).getX()) * (hList.get(i).getX() - hList.get(j).getX()));
                float yCalc = ((hList.get(i).getY() - hList.get(j).getY()) * (hList.get(i).getY() - hList.get(j).getY()));
                double answer = Math.sqrt(xCalc + yCalc);

                // add edge to list
                if(answer != 0)
                {
                    edges edgesObj = new edges(i, j, answer);
                    edgeList.add(edgesObj);
                }                
            }
        }

        return edgeList;
    }

    public static ArrayList<edges> kruskals(ArrayList<edges> eList, ArrayList<hotspot> hList, int clusters)
    {
        ArrayList<edges> result = new ArrayList<edges>();

        int e = 0;      // indexing for result[]
        int i = 0;      // indexing for sorted edges
        int edgesNeeded = hList.size() - clusters;

        // sorting edges
        sortByWeight(eList);

        subset[] subsets = new subset[hList.size()];

        // creating subset array size based on hList.size()
        for(i = 0; i < hList.size(); i++)
        {
            subsets[i] = new subset();
        }

        // create hList.size() subsets with single elements
        for(int v = 0; v < hList.size(); v++)
        {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0;          // index used to pick next edge

        // number of edges to be taken is equal to hList.size()-1
        while(e != edgesNeeded)
        {
            // pick the smallest edge and then increment the index for next iteration
            edges nextEdge = new edges();
            nextEdge = eList.get(i++);

            int x = find(subsets, nextEdge.getSrc());
            int y = find(subsets, nextEdge.getDest());

            // if this edge doesnt form a cycle, add and increment
            if(x != y)
            {
                result.add(nextEdge);
                e++;
                Union(subsets, x, y);
            }
        }

        // for id recognizing with hotspots
        for (edges r : result) {
            r.setSrc(r.getSrc() + 1);
            r.setDest(r.getDest() + 1);
        }

        return result;
    }

    private static void Union(subset[] subsets, int x, int y) 
    {
        int xRoot = find(subsets, x);
        int yRoot= find(subsets, y);

        // attach smaller tree under root of high rank tree
        if(subsets[xRoot].rank < subsets[yRoot].rank)
        {
            subsets[xRoot].parent = yRoot;
        }
        else if(subsets[xRoot].rank > subsets[yRoot].rank)
        {
            subsets[yRoot].parent = xRoot;
        }
        // ranks are the same, make 1 as root and increment its rank by 1
        else
        {
            subsets[yRoot].parent = xRoot;
            subsets[xRoot].rank++;
        }
    }

    static int find(subset subsets[], int i)
    {
        // find root and make root as parent of i
        if(subsets[i].parent != i)
        {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }

        return subsets[i].parent;
    }

    // comparator methods
    public static ArrayList<edges> sortByWeight(ArrayList<edges> eList) 
    {
        Collections.sort(eList, Comparator.comparing(edges::getWeight).thenComparing(edges::getWeight));
        return eList;
    }

    public static ArrayList<edges> sortBySrc(ArrayList<edges> eList)
    {
        Collections.sort(eList, Comparator.comparing(edges::getSrc).thenComparing(edges::getSrc));
        return eList;
    }
    
}