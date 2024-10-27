package Lab2;

public interface Fork {
    public void acquire() throws InterruptedException;
    public void release();

}
