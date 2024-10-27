package Lab1WithSemaphore;
import Lab2.BinarySemaphore;

public class Lab1withSemaphore {
    public static void main(String[] args) {
        BinarySemaphore semaphore = new BinarySemaphore();
        BinarySemaphore semaphore2 = new BinarySemaphore();
        Counter counter = new Counter();
        Thread incrementThread = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                try {
                    semaphore.acquire();
                    counter.increment();
                    semaphore2.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                try {
                    semaphore2.acquire();
                    counter.decrement();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        incrementThread.start();
        decrementThread.start();
        try {
            incrementThread.join();
            decrementThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(counter.getCounter());
    }
}
