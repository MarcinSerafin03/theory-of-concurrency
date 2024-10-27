public class Person extends Thread {
    private final Kelner kelner;
    private final int personID;
    private final int pairNumber;

    public Person(Kelner kelner,int personID, int pairNumber) {
        this.kelner = kelner;
        this.personID = personID;
        this.pairNumber = pairNumber;
    }

    @Override
    public void run() {
        try{
            while(true){
                Thread.sleep((int) Math.floor(Math.random() * 1000));
                System.out.println("Person " + personID +  " from pair " + pairNumber + " is doing own stuff");
                kelner.wantTable(pairNumber);
                System.out.println("Person " + personID +  " from pair " + pairNumber + " is eating");
                Thread.sleep((int) Math.floor(Math.random() * 1000));
                kelner.releaseTable();
                System.out.println("Person " + personID +  " from pair " + pairNumber + " released table");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
