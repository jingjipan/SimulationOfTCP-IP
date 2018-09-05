package lab5.sectionA.JingjiPan.logic;


import lab5.sectionA.JingjiPan.entity.Event;

public interface EventList
{
    public boolean add(Event e);
    public Event removeNext();
    public String toString();
    public double getLastPacketTime(int entityFrom, int entityTo);
}
