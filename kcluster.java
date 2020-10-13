// TITLE: 					Algorithms Assignment
// COURSE: 					COMP2230
// AUTHOR: 					Moosa Hassan
// STUDENT NUMBER: 			3331532
// DATE: 					24/10/2020 
// DESCRIPTION: 			main file - reads data and does essentially all of the calculations (kruskals, interclustering, ...)

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class kcluster 
{
    public static void main(final String[] args) throws FileNotFoundException {
        // error report
        if (args[0] == null || args[1] == null) {
            // not enough parameters
            System.out.println("USAGE: java kcluster fileName.txt Integer");
            return;
        } else if (!(args[0].contains(".txt"))) {
            // first parameter was not a string with .txt extension was not inputted
            System.out.println("USAGE: java kcluster fileName.txt Integer");
            return;
        } else if (!(args[1].matches(".*\\d.*"))) {
            // second parameter was not an int
            System.out.println("USAGE: java kcluster fileName.txt Integer");
            return;
        }

        // fetching data from parameters
        final Scanner file = new Scanner(new File(args[0])); // ID, x-coord, y-coord
        final int fireStations = Integer.parseInt(args[1]);

        final ArrayList<hotspot> hotspotList = new ArrayList<hotspot>();

        try {
            while (file.hasNext()) {
                final String newText = file.nextLine();

                final String[] splitStr = newText.split(",");
                final int id = Integer.parseInt(splitStr[0]); // retrieving ID
                final float xCoord = Float.parseFloat(splitStr[1]); // retrieving x-coordinate
                final float yCoord = Float.parseFloat(splitStr[2]); // retrieving y-coordinate

                // creating object and adding to list
                final hotspot hotspotObject = new hotspot(id, xCoord, yCoord);
                hotspotList.add(hotspotObject);
            }
        } catch (final Exception e) {
            System.out.println("File reading error: " + e);
        }

        // output
        System.out.println("Hello and welcome to Kruskal’s Clustering! \n");
        System.out.println("The weighted graph of hotspots: \n");

        printGraph(hotspotList); // graphing the lines of hotspots

        System.out.println("There are " + hotspotList.size() + " hotspots.");
        System.out.println("You have requested " + fireStations + " temporary fire stations. \n");

        locateStations(hotspotList, fireStations);

        System.out.println("Thank you for using Kruskal’s Clustering. Bye.");

    }

    public static void printGraph(final ArrayList<hotspot> hList) {
        // staying element
        for (int i = 0; i < hList.size(); i++) {
            // iterating element
            for (int j = 0; j < hList.size(); j++) {
                // euclidean algorithm divided into 3 sections
                final float xCalc = ((hList.get(i).getX() - hList.get(j).getX())
                        * (hList.get(i).getX() - hList.get(j).getX()));
                final float yCalc = ((hList.get(i).getY() - hList.get(j).getY())
                        * (hList.get(i).getY() - hList.get(j).getY()));
                final double answer = Math.sqrt(xCalc + yCalc);

                // displaying output
                if (answer == 0) {
                    System.out.print("0 ");
                } else {
                    final DecimalFormat df = new DecimalFormat("###.##");
                    System.out.print(df.format(answer));
                    System.out.print(" ");
                }
            }
            System.out.println(); // spacing
        }
        System.out.println(); // spacing
    }

    public static void locateStations(final ArrayList<hotspot> hList, final int stations) {
        final ArrayList<station> stationList = new ArrayList<station>();

        // adding blank stations to stationlist
        for (int i = 0; i < stations; i++) {
            stationList.add(new station());
        }

        makeClusters(hList, stations);
    }

    public static void makeClusters(ArrayList<hotspot> hList, final int stations) {
        final ArrayList<edges> edgeList = collectEdges(hList);
        final ArrayList<edges> MSTedges = kruskals(edgeList, hList, stations);

        // transfer edges data to hotspots
        for (int i = 0; i < MSTedges.size(); i++) {
            for (int j = 0; j < hList.size(); j++) {
                if (MSTedges.get(i).getSrc() == hList.get(j).getID()) {
                    final int destIndex = MSTedges.get(i).getDest() - 1;
                    hList.get(j).setNext(hList.get(destIndex));
                }
            }
        }
        for (int i = 0; i < MSTedges.size(); i++) {
            for (int j = 0; j < hList.size(); j++) {
                if (MSTedges.get(i).getDest() == hList.get(j).getID()) {
                    final int srcIndex = MSTedges.get(i).getSrc() - 1;
                    hList.get(j).setPrevious(hList.get(srcIndex));
                }
            }
        }

        // define clusters using that data on the hotspots
        hList = clusterMaker(hList, stations);

        // arraylists of arraylists
        final ArrayList<ArrayList<hotspot>> clusterArray = new ArrayList<ArrayList<hotspot>>();

        for (int i = 0; i < hList.size(); i++) {
            final ArrayList<hotspot> miniArray = new ArrayList<hotspot>();

            hotspot currentNode = hList.get(i);
            miniArray.add(currentNode);

            while (currentNode.getPrevious() != null) {
                final hotspot tempNode = currentNode.getPrevious();
                miniArray.add(tempNode);
                currentNode = tempNode;
            }

            clusterArray.add(miniArray);
        }

        for (int i = 0; i < stations; i++) {
            System.out.println("Station " + (i + 1) + ": ");
            System.out.println("Coordinates: (" + String.format("%4.2f", calculateXCentroid(clusterArray.get(i))) + ", "
                    + String.format("%4.2f", calculateYCentroid(clusterArray.get(i))) + ")");
            System.out.println("Hotspots: " + calculateHotspots(clusterArray.get(i)) + "\n");
        }

        System.out.println(
                "Inter-clustering distance: " + String.format("%4.2f", calculateIntercluster(clusterArray)) + "\n");

    }

    public static double calculateIntercluster(final ArrayList<ArrayList<hotspot>> clusterArray) {
        double min = Double.MAX_VALUE; // start with biggest number possible

        // iterate forwards
        for (int i = 0; i < clusterArray.size() - 1; i++) {
            // iterate backwards
            for (int j = clusterArray.size() - 1; j > 0; j--) {
                // get element from forward cluster
                for (int k = 0; k < clusterArray.get(i).size(); k++) {
                    // get element from backward cluster
                    for (int l = 0; l < clusterArray.get(j).size(); l++) {
                        if (i != j) {
                            // euclidean distance formula
                            final double answer = Euclidean(clusterArray.get(i).get(k).getX(),
                                    clusterArray.get(i).get(k).getY(), clusterArray.get(j).get(l).getX(),
                                    clusterArray.get(j).get(l).getY());

                            // comparing to add
                            if (answer < min) {
                                min = answer;
                            }
                        }
                    }
                }
            }
        }

        return min;
    }

    public static double Euclidean(final double x1, final double y1, final double x2, final double y2) {
        // euclidean algorithm divided into 3 sections
        final double xCalc = ((x2 - x1) * (x2 - x1));
        final double yCalc = ((y2 - y1) * (y2 - y1));
        final double answer = Math.sqrt(xCalc + yCalc);

        return answer;
    }

    public static String calculateHotspots(final ArrayList<hotspot> miniCluster) {
        String output = "{";

        sortById(miniCluster);

        // iterate through the cluster
        for (int i = 0; i < miniCluster.size(); i++) {
            // first iteration
            if (i == 0) {
                output += miniCluster.get(i).getID();
            }
            // every other iteration
            else {
                output += "," + miniCluster.get(i).getID();
            }
        }

        output += "}";

        return output;
    }

    public static double calculateXCentroid(final ArrayList<hotspot> miniCluster) {
        double total = 0;
        double result = 0;

        // iterate through cluster part
        for (int i = 0; i < miniCluster.size(); i++) {
            total += miniCluster.get(i).getX();
        }

        result = total / miniCluster.size();

        return result;
    }

    public static double calculateYCentroid(final ArrayList<hotspot> miniCluster) {
        double total = 0;
        double result = 0;

        // iterate through cluster part
        for (int i = 0; i < miniCluster.size(); i++) {
            total += miniCluster.get(i).getY();
        }

        result = total / miniCluster.size();

        return result;
    }

    public static ArrayList<hotspot> clusterMaker(final ArrayList<hotspot> hList, final int clusters) {
        final ArrayList<hotspot> result = new ArrayList<hotspot>();

        for (int j = 0; j < clusters; j++) {
            // find last node
            for (int i = 0; i < hList.size(); i++) {
                hotspot lastNode = hList.get(i);
                while (lastNode.getNext() != null) {
                    lastNode = lastNode.getNext();
                }

                // check for previous last nodes
                boolean doesExist = false;
                for (int k = 0; k < result.size(); k++) {
                    if (result.get(k) == lastNode) {
                        doesExist = true;
                    }
                }
                if (doesExist != true) {
                    // add node if unique
                    result.add(lastNode);
                    break;
                }
            }
        }

        return result;
    }

    public static ArrayList<edges> collectEdges(final ArrayList<hotspot> hList) {
        final ArrayList<edges> edgeList = new ArrayList<edges>();

        // staying element
        for (int i = 0; i < hList.size(); i++) {
            // iterating element
            for (int j = 0; j < hList.size(); j++) {
                // euclidean algorithm divided into 3 sections
                final float xCalc = ((hList.get(i).getX() - hList.get(j).getX())
                        * (hList.get(i).getX() - hList.get(j).getX()));
                final float yCalc = ((hList.get(i).getY() - hList.get(j).getY())
                        * (hList.get(i).getY() - hList.get(j).getY()));
                final double answer = Math.sqrt(xCalc + yCalc);

                // add edge to list
                if (answer != 0) {
                    final edges edgesObj = new edges(i, j, answer);
                    edgeList.add(edgesObj);
                }
            }
        }

        return edgeList;
    }

    public static ArrayList<edges> kruskals(final ArrayList<edges> eList, final ArrayList<hotspot> hList,
            final int clusters) {
        final ArrayList<edges> result = new ArrayList<edges>();

        int e = 0; // indexing for result[]
        int i = 0; // indexing for sorted edges
        final int edgesNeeded = hList.size() - clusters;

        // sorting edges
        sortByWeight(eList);

        final subset[] subsets = new subset[hList.size()];

        // creating subset array size based on hList.size()
        for (i = 0; i < hList.size(); i++) {
            subsets[i] = new subset();
        }

        // create hList.size() subsets with single elements
        for (int v = 0; v < hList.size(); v++) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0; // index used to pick next edge

        // number of edges to be taken is equal to hList.size()-1
        while (e != edgesNeeded) {
            // pick the smallest edge and then increment the index for next iteration
            edges nextEdge = new edges();
            nextEdge = eList.get(i++);

            final int x = find(subsets, nextEdge.getSrc());
            final int y = find(subsets, nextEdge.getDest());

            // if this edge doesnt form a cycle, add and increment
            if (x != y) {
                result.add(nextEdge);
                e++;
                Union(subsets, x, y);
            }
        }

        // for id recognizing with hotspots
        for (final edges r : result) {
            r.setSrc(r.getSrc() + 1);
            r.setDest(r.getDest() + 1);
        }

        return result;
    }

    private static void Union(final subset[] subsets, final int x, final int y) {
        final int xRoot = find(subsets, x);
        final int yRoot = find(subsets, y);

        // attach smaller tree under root of high rank tree
        if (subsets[xRoot].rank < subsets[yRoot].rank) {
            subsets[xRoot].parent = yRoot;
        } else if (subsets[xRoot].rank > subsets[yRoot].rank) {
            subsets[yRoot].parent = xRoot;
        }
        // ranks are the same, make 1 as root and increment its rank by 1
        else {
            subsets[yRoot].parent = xRoot;
            subsets[xRoot].rank++;
        }
    }

    static int find(final subset subsets[], final int i) {
        // find root and make root as parent of i
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }

        return subsets[i].parent;
    }

    // comparator methods
    public static ArrayList<edges> sortByWeight(final ArrayList<edges> eList) {
        Collections.sort(eList, Comparator.comparing(edges::getWeight).thenComparing(edges::getWeight));
        return eList;
    }

    public static ArrayList<edges> sortBySrc(final ArrayList<edges> eList) {
        Collections.sort(eList, Comparator.comparing(edges::getSrc).thenComparing(edges::getSrc));
        return eList;
    }

    public static ArrayList<hotspot> sortById(final ArrayList<hotspot> hList)
    {
        Collections.sort(hList, Comparator.comparing(hotspot::getID).thenComparing(hotspot::getID));
        return hList;
    }
    
}