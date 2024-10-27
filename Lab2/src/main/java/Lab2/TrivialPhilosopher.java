package Lab2;

public class TrivialPhilosopher extends Thread {
    private BinarySemaphore leftFork;
    private BinarySemaphore rightFork;
    private int leftForkNum;
    private int rightForkNum;

    public TrivialPhilosopher(BinarySemaphore leftFork, int leftForkNum, BinarySemaphore rightFork, int rightForkNum) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.leftForkNum = leftForkNum;
        this.rightForkNum = rightForkNum;
        System.out.println(this.getName() + " Right fork: " + rightForkNum + " Left fork: " + leftForkNum);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Philosopher " + this.getName() + " is thinking");
                leftFork.acquire();
                System.out.println("Philosopher " + this.getName() + " has acquired left fork nr " + leftForkNum);
                rightFork.acquire();
                System.out.println("Philosopher " + this.getName() + " has acquired right fork nr " + rightForkNum);
                System.out.println("Philosopher " + this.getName() + " is eating");
                leftFork.release();
                System.out.println("Philosopher " + this.getName() + " has released left fork nr " + leftForkNum);
                rightFork.release();
                System.out.println("Philosopher " + this.getName() + " has released right fork nr " + rightForkNum);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

}
