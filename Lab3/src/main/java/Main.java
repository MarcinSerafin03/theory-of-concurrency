//W języku java istnieja również nowsze (bardziej elastyczne) mechanizmy synchronizacji, zaimplementowane w pakiecie java.util.concurrent.locks
//W zadaniach nalezy użyc tego mechanizmu. Nalezy korzystac z lock, condition, await, signal
//Przed zrobieniem zadań przeanalizuj monitor BoundedBuffer przeznaczony dla producentow i konsumentow.
// Stolik dwuosobowy
//Napisz monitor Kelner sterujacy dostepem do jednego stolika dwuosobowego. Ze stolika korzysta N par osob. Algorytm osoby z pary o numerze j: M
// forever{
//  wlasne sprawy;
//  Kelner.chce_stolik(j);
//  jedzenie;
//  Kelner.zwalniam();
//}
// Stolik jest przydzielany parze w momencie gdy obydwie osoby tego zazadaja, zwalnianie nie musi byc jednoczesne.
// Stolik ma mieć codition i każda para ma miec condition


public class Main {
    public static void main(String[] args) {
//        Ex1();
        Ex2();
    }

    private static void Ex1(){
        PrinterMonitor printerMonitor = new PrinterMonitor(3);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    while (true) {
                        System.out.println("Thread " + finalI + " is creating a print job");
                        int printerNumber = printerMonitor.reservePrinter();
                        System.out.println("Thread " + finalI + " is printing on printer " + printerNumber);

                            Thread.sleep(1000);

                        printerMonitor.releasePrinter(printerNumber);
                        System.out.println("Thread " + finalI + " released printer " + printerNumber);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
    private static void Ex2() {
        Kelner kelner = new Kelner(3);
        Person[] people = new Person[6];
        for (int i = 0; i < 6; i++) {
            people[i] = new Person(kelner, i, i / 2);
        }
        for (int i = 0; i < 6; i++) {
            people[i].start();
        }
    }

}