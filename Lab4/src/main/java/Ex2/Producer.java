package Ex2;

import java.util.Random;

public class Producer extends Thread{
    private Buffer buffer;
    private int M;
    private Random random = new Random();

    public Producer(Buffer buffer, int M) {
        this.buffer = buffer;
        this.M = M;
    }

    @Override
    public void run() {
        while(true){
            int n = random.nextInt(M)+1;
            buffer.put(n);
//            System.out.println("Producer produced " + n);
        }
    }
}
