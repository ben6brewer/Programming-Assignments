/*
 * Author : Ben Brewer
 * File   : TicketSale.java
 * Date   : 2/14/2023
 * Purpose: Implements a system for handling ticket requests using a given Deque class
 *          where members have priority over non-members to purchase tickets.
 */

 public class TicketSale 
 {
    private Deque1<Request> memberDeque;
    private Deque1<Request> nonMemberDeque;
    private int tixRemaining;

    /*
     * Method    : TicketSale
     * Purpose   : constructs a TicketSale object
     * Parameters: int numTickets - the number of tickets remaining to be sold
     * Returns   : void
     */

    public TicketSale(int numTickets) 
    {
        tixRemaining = numTickets;
        memberDeque = new Deque1<Request>(numTickets);
        nonMemberDeque = new Deque1<Request>(numTickets);
    }

    /*
     * Method    : addRequest
     * Purpose   : adds the request to the appropriate member or non-member deque
     * Parameters: Request r - the request to be added
     * Returns   : void
     */

    public void addRequest(Request r) 
    {
        if (r.isMember) 
        {
            memberDeque.enqueue(r);
        } 
        else if (!r.isMember)
        {
            nonMemberDeque.enqueue(r);
        }
    }

    /*
     * Method    : processNext
     * Purpose   : processes the next request in the queue by removing it from the queue
     *             and returning it. It will check the member deque before the non-member
     *             deque. If both are empty, it will return null and a SoldOutException
     *             will be thrown if there are no tickets remaining.
     * Parameters: none
     * Returns   : Request - the next request in the queue
     */

    public Request processNext() throws SoldOutException 
    {
        // tix are sold out
        if (tixRemaining == 0) 
        {
            throw new SoldOutException();
        }
        // tix are not sold out so process member deque first
        try 
        {
            tixRemaining--;
            return memberDeque.dequeue();
        } 
        // process non-member deque if member deque is empty
        catch (EmptyDequeException e) 
        {
            tixRemaining++;
            try {
                tixRemaining--;
                return nonMemberDeque.dequeue();
            } 
            // both deques are empty so return null
            catch (EmptyDequeException e1) 
            {
                tixRemaining++;
                return null;
            }
        }
    }
}

