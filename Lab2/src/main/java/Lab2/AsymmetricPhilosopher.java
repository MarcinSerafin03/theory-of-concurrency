package Lab2;

public class AsymmetricPhilosopher extends Thread {
    private final Fork LeftFork;
    private final Fork RightFork;
    private final int leftForkNum;
    private final int rightForkNum;
    private boolean isOdd;


    public AsymmetricPhilosopher(Fork leftFork,int leftForkNum, Fork rightFork,int rightForkNum, boolean isOdd) {
        this.LeftFork = leftFork;
        this.RightFork = rightFork;
        this.leftForkNum = leftForkNum;
        this.rightForkNum = rightForkNum;
        this.isOdd = isOdd;
        System.out.println(this.getName() + " Right fork: " + rightForkNum + " Left fork: " + leftForkNum);
    }

    public void run(){
        try{
            while(true){
                System.out.println("Philosopher " + this.getName() + " is thinking");
                if(isOdd){
                    LeftFork.acquire();
                    System.out.println("Philosopher " + this.getName() + " has acquired left fork nr " + leftForkNum);
                    RightFork.acquire();
                    System.out.println("Philosopher " + this.getName() + " has acquired right fork nr " + rightForkNum);
                }else{
                    RightFork.acquire();
                    System.out.println("Philosopher " + this.getName() + " has acquired right fork nr " + rightForkNum);
                    LeftFork.acquire();
                    System.out.println("Philosopher " + this.getName() + " has acquired left fork nr " + leftForkNum);
                }
                System.out.println("Philosopher " + this.getName() + " is eating");
                LeftFork.release();
                RightFork.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
