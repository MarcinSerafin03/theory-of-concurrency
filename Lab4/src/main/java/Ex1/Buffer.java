package Ex1;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Buffer {
    private final int size;
    private final int numberOfProcessors;

    private final int[] buffer; // Shared buffer

    // Semaphores for each stage to control processing order
    private final Semaphore[] stageSemaphores;
    public Buffer(int size, int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
        this.size = size;
        this.buffer = new int[size];
        this.stageSemaphores = new Semaphore[numberOfProcessors+2];
        for (int i = 0; i < size; i++) {
            buffer[i] = -1;  // Initialize buffer cells to -1
        }
        for (int i = 0; i < stageSemaphores.length; i++) {
            stageSemaphores[i] = new Semaphore(i == 0 ? size : 0);
        }

    }


    public void produce() throws InterruptedException {
        int i = 0;
        while (true) {
            stageSemaphores[0].acquire();
            buffer[i] = 0;
            System.out.println("Ex1.Producer produced buffer[" + i + "] = " + buffer[i]);
            stageSemaphores[1].release();
            i = (i + 1) % size;
            Thread.sleep(new Random().nextInt(1000));
        }
    }

    public void consume() throws InterruptedException {
        int i = 0;
        while (true) {
            stageSemaphores[numberOfProcessors + 1].acquire();
            buffer[i] = -1;
            System.out.println("Ex1.Consumer consumed buffer[" + i + "] = " + buffer[i]);
            stageSemaphores[0].release();
            i = (i + 1) % size;
            Thread.sleep(new Random().nextInt(1000));
        }
    }


    public void process(int stage) throws InterruptedException {
        int i = 0;
        while (true) {
            stageSemaphores[stage].acquire();
            buffer[i] += 1;
            System.out.println("Ex1.Processor " + stage + " processed buffer[" + i + "] = " + buffer[i]);
            stageSemaphores[stage + 1].release();
            i = (i + 1) % size;
            Thread.sleep(new Random().nextInt(1000));
        }
    }







}