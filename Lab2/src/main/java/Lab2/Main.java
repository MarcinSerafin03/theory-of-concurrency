package Lab2;

public class Main {
    public static void main(String[] args) {
        int numPhilosophers = 5;
        BinarySemaphore[] forks = new BinarySemaphore[numPhilosophers];
        CounterSemaphore waiter = new CounterSemaphore(numPhilosophers - 1);

        // Tworzymy semafory (widelce)
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new BinarySemaphore();
        }

        // Rozwiazanie trywialne
//        runTrivialPhilosophers(numPhilosophers, forks);

        // Rozwiazanie asymetryczne
//        runAsymmetricPhilosophers(numPhilosophers, forks);

        // Rozwiazanie z lokajem
//        runWaiterPhilosophers(numPhilosophers, forks, waiter);

    }

    private static void runTrivialPhilosophers(int numPhilosophers, BinarySemaphore[] forks) {
        TrivialPhilosopher[] trivialPhilosophers = new TrivialPhilosopher[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            trivialPhilosophers[i] = new TrivialPhilosopher(forks[i],i, forks[(i + 1) % numPhilosophers],(i + 1) % numPhilosophers);
            trivialPhilosophers[i].start();
        }
    }

    private static void runWaiterPhilosophers(int numPhilosophers, BinarySemaphore[] forks, CounterSemaphore waiter) {
        WaiterPhilosopher[] philosophers = new WaiterPhilosopher[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            philosophers[i] = new WaiterPhilosopher(forks[i], forks[(i + 1) % numPhilosophers], waiter);
            philosophers[i].start();
        }
    }

    private static void runAsymmetricPhilosophers(int numPhilosophers, BinarySemaphore[] forks) {
        AsymmetricPhilosopher[] philosophers = new AsymmetricPhilosopher[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            if (i % 2 == 0) {
                philosophers[i] = new AsymmetricPhilosopher(forks[i],i, forks[(i + 1) % numPhilosophers],(i + 1) % numPhilosophers,false);
            } else {
                philosophers[i] = new AsymmetricPhilosopher(forks[i],i, forks[(i + 1) % numPhilosophers],(i + 1) % numPhilosophers,true);
            }
            philosophers[i].start();
        }
    }
}