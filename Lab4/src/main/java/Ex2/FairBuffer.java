package Ex2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairBuffer implements Buffer {
    private int size;
    private int currentSize;
    private Lock lock = new ReentrantLock();
    private Condition[] conditions = new Condition[4];
    // 0 - full
    // 1 - producers waiting
    // 2 - empty
    // 3 - consumers waiting
    private boolean[] waiting = new boolean[4];

    private FileWriter fileWriter;

    public FairBuffer(int size, FileWriter fileWriter) {
        this.size = size;
        this.currentSize = 0;
        this.fileWriter = fileWriter;
        for(int i = 0; i < 4; i++){
            conditions[i] = lock.newCondition();
            waiting[i] = false;
        }
    }

    public void put(int amount){
        lock.lock();
        try {
            long start = System.nanoTime();
            if(waiting[0]){
                waiting[1] = true;
                conditions[1].await();
                waiting[1] = false;
            }
            while (currentSize+amount > size){
                waiting[0] = true;
                if (!conditions[0].await(100, TimeUnit.MILLISECONDS)){
                    waiting[0] = false;
                    conditions[1].signal();
                    return;
                }
                waiting[0] = false;
            }
            long end = System.nanoTime();
            currentSize += amount;
            fileWriter.append("Producer," + amount + "," + (end - start) + "\n");

//            System.out.println("Produced " + amount + " items. Current size: " + currentSize);
            conditions[1].signal();
            conditions[2].signal();
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
            if(waiting[2]){
                waiting[3] = true;
                conditions[3].await();
                waiting[3] = false;
            }
            while (currentSize-amount < 0){
                waiting[2] = true;
                if (!conditions[2].await(100, TimeUnit.MILLISECONDS)){
                    waiting[2] = false;
                    conditions[3].signal();
                    return;
                }
                waiting[2] = false;
            }
            long end = System.nanoTime();
            currentSize -= amount;
            fileWriter.append("Consumer," + amount + "," + (end - start) + "\n");

//            System.out.println("Consumed " + amount + " items. Current size: " + currentSize);
            conditions[0].signal();
            conditions[3].signal();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }


}
