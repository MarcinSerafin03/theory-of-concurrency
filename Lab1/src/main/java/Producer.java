public class Producer implements Runnable {
    private Buffer buffer;
    private String name;

    public Producer(Buffer buffer, String name) {
        this.buffer = buffer;
        this.name = name;
    }

    public void run() {

        for(int i = 0;  i < 15;   i++) {
            buffer.put("message "+i, name);
        }

    }
}