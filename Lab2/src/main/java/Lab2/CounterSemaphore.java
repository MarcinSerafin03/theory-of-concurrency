package Lab2;

public class CounterSemaphore implements Fork {
    private int counter = 0;

    public CounterSemaphore(int counter) {
        this.counter = counter;
    }

    public synchronized void acquire() throws InterruptedException {
        while (counter == 0) {
            wait();
        }
        counter--;
    }

    public synchronized void release() {
        counter++;
        notifyAll();
    }

}
