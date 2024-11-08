package Ex1;

import java.util.Random;

public class Producer extends Thread {
    private Buffer buffer;
    private Random random = new Random();

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            buffer.produce();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}