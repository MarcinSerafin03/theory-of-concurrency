package Ex1;

import java.util.Random;

public class Processor extends Thread {
    private Buffer buffer;
    private int id;
    private Random random = new Random();

    public Processor(Buffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            buffer.process(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}