package Ex2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NaiveBuffer implements Buffer {
    private int size;
    private int currentSize;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private FileWriter fileWriter;

    public NaiveBuffer(int size, FileWriter fileWriter) {
        this.size = 2*size;
        this.currentSize = 0;

        this.fileWriter = fileWriter;
    }

    public void put(int amount){
        lock.lock();
        try {
            long start = System.nanoTime();
            while (currentSize + amount > size) {
                notFull.await();
            }
            long end = System.nanoTime();
            currentSize += amount;
            fileWriter.append("Producer," + amount + "," + (end - start) + "\n");

//            System.out.println("Produced " + amount + " items. Current size: " + currentSize);
            notEmpty.signal();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void get(int amount){
        lock.lock();
        try {
            long start = System.nanoTime();
            while (currentSize - amount < 0) {
                notEmpty.await();
            }
            long end = System.nanoTime();
            currentSize -= amount;
            fileWriter.append("Consumer," + amount + "," + (end - start) + "\n");
//                        System.out.println("Consumed " + amount + " items. Current size: " + currentSize);
            notFull.signal();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
