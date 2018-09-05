package lab3.sectionx.yourname.alternatebitprotocol;

import lab3.sectionx.yourname.entity.Message;
import lab3.sectionx.yourname.entity.Packet;
import lab3.sectionx.yourname.logic.NetworkSimulator;

public class StudentNetworkSimulator extends NetworkSimulator
{
    /*
     * Predefined Constants (static member variables):
     *
     *   int MAXDATASIZE : the maximum size of the Message data and
     *                     Packet payload
     *
     *   int A           : a predefined integer that represents entity A
     *   int B           : a predefined integer that represents entity B 
     *
     * Predefined Member Methods:
     *
     *  void stopTimer(int entity): 
     *       Stops the timer running at "entity" [A or B]
     *  void startTimer(int entity, double increment): 
     *       Starts a timer running at "entity" [A or B], which will expire in
     *       "increment" time units, causing the interrupt handler to be
     *       called.  You should only call this with A.
     *  void toLayer3(int callingEntity, Packet p)
     *       Puts the packet "p" into the network from "callingEntity" [A or B]
     *  void toLayer5(String dataSent)
     *       Passes "dataSent" up to layer 5
     *  double getTime()
     *       Returns the current time in the simulator.  Might be useful for
     *       debugging.
     *  int getTraceLevel()
     *       Returns TraceLevel
     *  void printEventList()
     *       Prints the current event list to stdout.  Might be useful for
     *       debugging, but probably not.
     *
     *
     *  Predefined Classes:
     *
     *  Message: Used to encapsulate a message coming from layer 5
     *    Constructor:
     *      Message(String inputData): 
     *          creates a new Message containing "inputData"
     *    Methods:
     *      boolean setData(String inputData):
     *          sets an existing Message's data to "inputData"
     *          returns true on success, false otherwise
     *      String getData():
     *          returns the data contained in the message
     *  Packet: Used to encapsulate a packet
     *    Constructors:
     *      Packet (Packet p):
     *          creates a new Packet that is a copy of "p"
     *      Packet (int seq, int ack, int check, String newPayload)
     *          creates a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and a
     *          payload of "newPayload"
     *      Packet (int seq, int aa checksum field of "check", and a
     *          payload of "newPayload"ck, int check)
     *          chreate a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and
     *          an empty payload
     *    Methods:
     *      boolean setSeqnum(int n)
     *          sets the Packet's sequence field to "n"
     *          returns true on success, false otherwise
     *      boolean setAcknum(int n)
     *          sets the Packet's ack field to "n"
     *          returns true on success, false otherwise
     *      boolean setChecksum(int n)
     *          sets the Packet's checksum to "n"
     *          returns true on success, false otherwise
     *      boolean setPayload(String newPayload)
     *          sets the Packet's payload to "newPayload"
     *          returns true on success, false otherwise
     *      int getSeqnum()
     *          returns the contents of the Packet's sequence field
     *      int getAcknum()
     *          returns the contents of the Packet's ack field
     *      int getChecksum()
     *          returns the checksum of the Packet
     *      int getPayload()
     *          returns the Packet's payload
     *
     */

    /*   Please use the following variables in your routines.
     *   int WindowSize  : the window size
     *   double RxmtInterval   : the retransmission timeout
     *   int LimitSeqNo  : when sequence number reaches this value, it wraps around
     */

    public static final int FirstSeqNo = 0;
    private int WindowSize;
    private double RxmtInterval;
    private int LimitSeqNo;
    public int awindow = 0;
    public int bwindow = 0;
    public Packet transmitting_packet;
    public boolean stopwatch;
    // Add any necessary class variables here.  Remember, you cannot use
    // these variables to send messages error free!  They can only hold
    // state information for A or B.
    // Also add any necessary methods (e.g. checksum of a String)

    // This is the constructor.  Don't touch!
    public StudentNetworkSimulator(int numMessages,
                                   double loss,
                                   double corrupt,
                                   double avgDelay,
                                   int trace,
                                   int seed,
                                   int winsize,
                                   double delay)
    {
        super(numMessages, loss, corrupt, avgDelay, trace, seed);
	WindowSize = winsize;
	LimitSeqNo = winsize+1;
	RxmtInterval = delay;
    }

    
    // This routine will be called whenever the upper layer at the sender [A]
    // has a message to send.  It is the job of your protocol to insure that
    // the data in such a message is delivered in-order, and correctly, to
    // the receiving upper layer.
    protected void aOutput(Message message)
    {
        String data;
        data = message.getData();
        int checksum = 0;
        for (int i=0;i<data.length();i++){
            checksum += (int)data.charAt(i);
        }
        checksum = checksum%128;
        Packet p = new Packet(awindow,-1,checksum,data);
        System.out.println("A: sending message #: "+awindow);
        toLayer3(0,p);
        transmitting_packet=p;
        startTimer(0,20);
        //stopwatch=true;
    }
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by a B-side procedure)
    // arrives at the A-side.  "packet" is the (possibly corrupted) packet
    // sent from the B-side.
    protected void aInput(Packet packet)
    {
        int ack = packet.getAcknum();
        int checksum = packet.getChecksum();
        int seqnum = packet.getSeqnum();
        if (checksum == (ack+seqnum)%8){
            if(ack==-1){
                System.out.println("NAK received");
            }
            else{
                System.out.println("A: received ACK for message #: "+awindow+"\n\r\n\r");
                awindow = (awindow+1)%2;
                stopTimer(0);
                //stopwatch = false;
            }
        }
        else{
            System.out.println("CHECKSUM ERROR");
        }
    }
    
    // This routine will be called when A's timer expires (thus generating a 
    // timer interrupt). You'll probably want to use this routine to control 
    // the retransmission of packets. See startTimer() and stopTimer(), above,
    // for how the timer is started and stopped. 
    protected void aTimerInterrupt()
    {
        System.out.println("A: Timed out,retransmitting message: "+awindow);
        toLayer3(0,transmitting_packet);
        startTimer(0,20);
    }
    
    // This routine will be called once, before any of your other A-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity A).
    protected void aInit()
    {

    }
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by an A-side procedure)
    // arrives at the B-side.  "packet" is the (possibly corrupted) packet
    // sent from the A-side.
    protected void bInput(Packet packet)
    {
        int checksum=0;
        int seqnum=packet.getSeqnum();
        String data = packet.getPayload();
        int packetchecksum = packet.getChecksum();
        for (int i=0;i<data.length();i++){
            checksum += (int)data.charAt(i);
        }
        checksum=checksum%128;
        //System.out.println("Corrupted"+" "+checksum+" "+packetchecksum);
        if(checksum==packetchecksum && (seqnum == 0 || seqnum == 1)){
            Packet p = new Packet(bwindow,bwindow,(bwindow*2)%8);
            System.out.println("B: received #: "+seqnum);
            toLayer3(1,p);
            if(seqnum==bwindow){
                bwindow = (bwindow+1)%2;
                toLayer5(packet.getPayload());
            }
        }
        else{
            System.out.println("CHECKSUM ERROR: Received packet has been corrupted. Sending NAK.");
            Packet p = new Packet(bwindow,-1,(bwindow-1)%8);
            toLayer3(1,p);
        }
    }
    
    // This routine will be called once, before any of your other B-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity B).
    protected void bInit()
    {

    }

    // Use to print final statistics
    protected void Simulation_done()
    {

    }	

}
