package Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RandomProducentConsumer {

    private static class Buffer {
        private ReentrantLock lock = new ReentrantLock();
        private int maxCapacity;
        private int counter = 0;
        private FileWriter fileWriter;
        private Condition consumerCond;
        private Condition producerCond;

        public Buffer(int limit, FileWriter fileWriter) {
            this.consumerCond = lock.newCondition();
            this.producerCond = lock.newCondition();
            this.maxCapacity = 2 * limit;
            this.fileWriter = fileWriter;
        }

        public void get(int amount) {
            lock.lock();
            try {
                long start = System.nanoTime();
                while (counter < amount) {
                    if (!consumerCond.await(100, TimeUnit.MILLISECONDS)) {
                        consumerCond.signal();
                        return;
                    }
                }
                long end = System.nanoTime();
                counter -= amount;
                fileWriter.append("Consumer," + amount + "," + (end - start) + "\n");
                producerCond.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                lock.unlock();
            }
        }

        public void put(int amount) {
            lock.lock();
            try {
                long start = System.nanoTime();
                while (counter + amount > maxCapacity) {
                    if (!producerCond.await(100, TimeUnit.MILLISECONDS)) {
                        producerCond.signal();
                        return;
                    }
                }
                long end = System.nanoTime();
                counter += amount;
                fileWriter.append("Producer," + amount + "," + (end - start) + "\n");
                consumerCond.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }

    private abstract static class Worker implements Runnable {
        protected Buffer buffer;
        private int limit;

        public Worker(Buffer buffer, int limit) {
            this.buffer = buffer;
            this.limit = limit;
        }

        public void run() {
            int randomInt = (int) (Math.random() * limit);
            doIt(randomInt);
        }

        public abstract void doIt(int randomInt);

    }

    private static class Consumer extends Worker {

        public Consumer(Buffer buffer, int limit) {
            super(buffer, limit);
        }

        @Override
        public void doIt(int randomInt) {
            this.buffer.get(randomInt);
        }

    }

    private static class Producer extends Worker {

        public Producer(Buffer buffer, int limit) {
            super(buffer, limit);
        }

        @Override
        public void doIt(int randomInt) {
            this.buffer.put(randomInt);
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FileWriter fileWriter = new FileWriter("zad2.csv");
        go(1000, 10, fileWriter);
        go(10000, 100, fileWriter);
        go(100000, 1000, fileWriter);
        fileWriter.close();
    }

    private static void go(int bufferSize, int numberOfWorkers, FileWriter fileWriter)
            throws IOException, InterruptedException {
        Buffer buffer = new Buffer(bufferSize, fileWriter);
        Thread[] workers = new Thread[numberOfWorkers * 2];
        for (int i = 0; i < numberOfWorkers; i++) {
            workers[i] = new Thread(new Producer(buffer, bufferSize));
            workers[i + numberOfWorkers] = new Thread(new Consumer(buffer, bufferSize));
        }
        for (int i = 0; i < 2 * numberOfWorkers; i++) {
            workers[i].start();
        }
        for (int i = 0; i < 2 * numberOfWorkers; i++) {
            workers[i].join();
        }
    }

}
