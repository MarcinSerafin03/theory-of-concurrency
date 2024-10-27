import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterMonitor {
    private final boolean[] printers;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public PrinterMonitor(int numberOfPrinters) {
        printers = new boolean[numberOfPrinters];
        for (int i = 0; i < numberOfPrinters; i++) {
            printers[i] = true;
        }
    }

    public int reservePrinter() {
        lock.lock();
        try {
            while (true) {
                for (int i = 0; i < printers.length; i++) {
                    if (printers[i]) {
                        printers[i] = false;
                        return i;
                    }
                }
                condition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            lock.unlock();
        }
    }

    public void releasePrinter(int printerNumber) {
        lock.lock();
        try {
            printers[printerNumber] = true;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}