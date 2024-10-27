import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Kelner {
    private final Lock lock = new ReentrantLock();
    private final Condition tableCondition = lock.newCondition();
    private final Condition[] pairsCondition;
    private boolean[] pairsWaiting;
    private int eating = 0;

    public Kelner(int numberOfPairs) {
        pairsCondition = new Condition[numberOfPairs];
        pairsWaiting = new boolean[numberOfPairs];
        for (int i = 0; i < numberOfPairs; i++) {
            pairsCondition[i] = lock.newCondition();
            pairsWaiting[i] = false;
        }
    }
    public void wantTable(int pairNumber) throws InterruptedException {
        lock.lock();
        try {
            while(eating>0){
                tableCondition.await();
            }
            if(!pairsWaiting[pairNumber]){
                pairsWaiting[pairNumber]=true;
                while(pairsWaiting[pairNumber]){
                    pairsCondition[pairNumber].await();
                }
            }
            else {
                pairsWaiting[pairNumber]=false;
                eating=2;
                pairsCondition[pairNumber].signal();
            }
        }
        finally {
            lock.unlock();
        }
    }

    public void releaseTable() {
        lock.lock();
        try {
            eating--;
            if (eating == 0) {
                tableCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}