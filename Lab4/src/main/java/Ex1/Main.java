package Ex1;

public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(100, 5);
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        Processor[] processors = new Processor[5];
        for (int i = 0; i < 5; i++) {
            processors[i] = new Processor(buffer, i+1);
        }
        producer.start();
        for (int i = 0; i < 5; i++) {
            processors[i].start();
        }
        consumer.start();

    }
}
