package Lab2;

public class BinarySemaphore implements Fork {
    private boolean isAvailable = true;

    public synchronized void acquire() throws InterruptedException {
        while (!isAvailable) {
            wait();
        }
        isAvailable = false;
    }

    public synchronized void release() {
        isAvailable = true;
        notifyAll();
    }

}
