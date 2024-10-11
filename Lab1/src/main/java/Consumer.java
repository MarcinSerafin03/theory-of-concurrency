public class Consumer implements Runnable {
    private Buffer buffer;
    private String name;

    public Consumer(Buffer buffer, String name) {
        this.buffer = buffer;
        this.name = name;
    }

    public void run() {

        for(int i = 0;  i < 10;   i++) {
            String message = buffer.take(name);
        }

    }
}
