package Lab2;

public class WaiterPhilosopher extends Thread{
    private final Fork leftFork;
    private final Fork rightFork;
    private final CounterSemaphore waiter;

    public WaiterPhilosopher(Fork leftFork, Fork rightFork, CounterSemaphore waiter) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.waiter = waiter;
    }

    public void run(){
        try{
            while(true){
                System.out.println("Philosopher " + this.getName() + " is thinking");
                waiter.acquire();
                leftFork.acquire();
                System.out.println("Philosopher " + this.getName() + " has acquired left fork");
                rightFork.acquire();
                System.out.println("Philosopher " + this.getName() + " has acquired right fork");
                System.out.println("Philosopher " + this.getName() + " is eating");
                leftFork.release();
                System.out.println("Philosopher " + this.getName() + " has released left fork");
                rightFork.release();
                System.out.println("Philosopher " + this.getName() + " has released right fork");
                waiter.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
