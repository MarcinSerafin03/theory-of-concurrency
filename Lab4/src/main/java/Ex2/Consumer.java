package Ex2;

import java.util.Random;

public class Consumer extends Thread {
    private Buffer buffer;
    private int M;
    private Random random = new Random();

    public Consumer(Buffer buffer, int M) {
        this.buffer = buffer;
        this.M = M;
    }

    @Override
    public void run() {
        while(true){
            int n = random.nextInt(M)+1;
            buffer.get(n);
//            System.out.println("Consumer consumed " + n);
        }
    }
}
