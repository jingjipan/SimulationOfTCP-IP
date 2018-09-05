package lab5.sectionA.JingjiPan.entity;
import lab5.sectionA.JingjiPan.logic.NetworkSimulator;
import java.lang.Math;
public class Entity0 extends Entity
{    
    // Perform any necessary initialization in the constructor
    boolean change =false;
    public Entity0()
    {
        distanceTable[0][0]=0;
        distanceTable[0][1]=1;
        distanceTable[0][2]=3;
        distanceTable[0][3]=7;
        Packet p1 = new Packet(0,1,distanceTable[0]);
        Packet p2 = new Packet(0,2,distanceTable[0]);
        Packet p3 = new Packet(0,3,distanceTable[0]);
        NetworkSimulator.toLayer2(p1);
        NetworkSimulator.toLayer2(p2);
        NetworkSimulator.toLayer2(p3);
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p)
    {  
       int source = p.getSource();
       System.out.printf("A distance table from %1d is received.\r",source);
       for (int i=0;i<4;i++){
           distanceTable[source][i]=p.getMincost(i);
       }
       for (int i=0;i<4;i++){
            if (Math.min(distanceTable[0][i],distanceTable[source][0]+distanceTable[source][i])<distanceTable[0][i]){
                distanceTable[0][i]=distanceTable[source][0]+distanceTable[source][i];
                change=true;
            }
       }
       
       if (change){
            System.out.println("The distance table of 0 is updated.");
            Packet p1 = new Packet(0,1,distanceTable[0]);
            Packet p2 = new Packet(0,2,distanceTable[0]);
            Packet p3 = new Packet(0,3,distanceTable[0]);
            NetworkSimulator.toLayer2(p1);
            NetworkSimulator.toLayer2(p2);
            NetworkSimulator.toLayer2(p3);
            change=false;
       }
       printDT();
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D0 |   1   2   3");
        System.out.println("----+------------");
        for (int i = 1; i < NetworkSimulator.NUMENTITIES; i++)
        {
            System.out.print("   " + i + "|");
            for (int j = 1; j < NetworkSimulator.NUMENTITIES; j++)
            {
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
