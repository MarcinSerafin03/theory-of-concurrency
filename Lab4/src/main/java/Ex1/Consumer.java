package Ex1;

import java.util.Random;

public class Consumer extends Thread {
    private Buffer buffer;
    private Random random = new Random();

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            buffer.consume();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}