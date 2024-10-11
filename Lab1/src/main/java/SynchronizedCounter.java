public class SynchronizedCounter
    {
    private int counter = 0;

    public synchronized void increment()
    {
        counter++;
    }

    public synchronized void decrement()
    {
        counter--;
    }

    public synchronized int getCounter()
    {
        return counter;
    }
}
