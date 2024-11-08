package Ex2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        int[] M ={1000, 10000, 100000};
        int[] producersConsumers = {10, 100, 1000};
        FileWriter naiveFileWriter = new FileWriter("./naiveOutput.csv");
//        Ex1(M,producersConsumers, naiveFileWriter);
        Thread ex1Thread = new Thread(() -> {
            Ex1(M, producersConsumers, naiveFileWriter);
        });

//        ex1Thread.start();
//        naiveFileWriter.close();

        FileWriter fairFileWriter = new FileWriter("./fairOutput.csv");
//        Ex2(M,producersConsumers, fairFileWriter);
        Thread ex2Thread = new Thread(() -> {
            Ex2(M, producersConsumers, fairFileWriter);
        });

//        ex2Thread.start();
//        fairFileWriter.close();

        ex1Thread.start();
        ex2Thread.start();

        try {
            Thread.sleep(5000);
            ex1Thread.interrupt();
            ex2Thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ex1Thread.join();
            ex2Thread.join();
            naiveFileWriter.close();
            fairFileWriter.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return;


    }

    private static void Ex1(int[] M, int[] producerConsumers, FileWriter fileWriter) {
        for(int i = 0; i < M.length; i++){
            Naive(M[i], producerConsumers[i], fileWriter);
        }
    }

    private static void Naive(int M, int count, FileWriter fileWriter) {
        NaiveBuffer buffer = new NaiveBuffer(M, fileWriter);
        Thread[] threads = new Thread[count*2];

        for(int i = 0; i < count; i++){
            Producer producer = new Producer(buffer, M);
            Consumer consumer = new Consumer(buffer, M);
            threads[i] = producer;
            threads[i+count] = consumer;
        }

        for(Thread t : threads){
            t.start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void Ex2(int[] M, int[] producerConsumers, FileWriter fileWriter) {
        for(int i = 0; i < M.length; i++){
            Fair(M[i], producerConsumers[i], fileWriter);
        }
    }

    private static void Fair(int M, int count, FileWriter fileWriter) {
        FairBuffer buffer = new FairBuffer(M, fileWriter);
        Thread[] threads = new Thread[count*2];

        for(int i = 0; i < count; i++){
            Producer producer = new Producer(buffer, M);
            Consumer consumer = new Consumer(buffer, M);
            threads[i] = producer;
            threads[i+count] = consumer;
        }

        for(Thread t : threads){
            t.start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

