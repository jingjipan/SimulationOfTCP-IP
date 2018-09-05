package lab5.sectionA.JingjiPan.entity;
import lab5.sectionA.JingjiPan.logic.NetworkSimulator;
public class Entity2 extends Entity
{    
    // Perform any necessary initialization in the constructor
    boolean change =false;
    public Entity2()
    {
        distanceTable[2][0]=3;
        distanceTable[2][1]=1;
        distanceTable[2][2]=0;
        distanceTable[2][3]=2;
        Packet p1 = new Packet(2,0,distanceTable[2]);
        Packet p2 = new Packet(2,1,distanceTable[2]);
        Packet p3 = new Packet(2,3,distanceTable[2]);
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
            if (Math.min(distanceTable[2][i],distanceTable[source][2]+distanceTable[source][i])<distanceTable[2][i]){
                distanceTable[2][i]=distanceTable[source][2]+distanceTable[source][i];
                change=true;
            }
       }
       
       if (change){
            System.out.println("The distance table of 2 is updated.");
            Packet p1 = new Packet(2,0,distanceTable[2]);
            Packet p2 = new Packet(2,1,distanceTable[2]);
            Packet p3 = new Packet(2,3,distanceTable[2]);
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
        System.out.println(" D2 |   0   1   3");
        System.out.println("----+------------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 2)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
            {
                if (j == 2)
                {
                    continue;
                }
                
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
