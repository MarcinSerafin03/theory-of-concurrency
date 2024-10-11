//Zadanie
//zad 1 Napisac program BEZ SYNCHRONIZACJI, w ktorym mamy obiekt klasy Counter przechowywujący pewną zmienną całkowitą oraz dwie metody inkrementującą i dekrementującą.Nastepnie jeden watek wywoluje na tym obiekcie metode inkrementująca 100000000 razy, drugi dekrementująca 100000000 razy.
//
//Czy wynik zawsze jest zero? Sprawdzić działanie na różnych systemach. (1 pkt)
//
//
//zad 2 Wprowadzić synchronizację do programu wykorzystujac slowo kluczowe "synchronized" (1 pkt)
//
//zad 3 Mamy klika procesów produkujacych wiadomosci (szkielet kodu) i kilka konsumujacych wiadomosci (szkielet kodu) do/z jednoelementowego bufora. Zadaniem jest napisanie klasy Buffer z metodami put i take, tak, aby dostep byl synchronizowany uzywajac monitora Javy dla obiektu klasy Buffer. Kazda wiadomosc jest produkowana przez jednego producenta i konsumowana przez jednego, dowolnego konsumenta. (1 pkt)
//
//zad 4 wyjasnij, dlaczego przy sprawdzaniu warunku czy bufor jest pusty/pelny nalezy uzyc instrukcji while , a nie wystarczy instrukcja if .


public class Main {
    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            System.out.println("Test " + i);
//            Ex1();
//        }
//        Ex2();
        Ex3();
    }

    private static void Ex1() {
        Counter counter = new Counter();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                counter.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                counter.decrement();
            }
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(counter.getCounter());
    }

    private static void Ex2() {
        SynchronizedCounter counter = new SynchronizedCounter();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                counter.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                counter.decrement();
            }
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(counter.getCounter());
    }

    private static void Ex3() {
        Buffer buffer = new Buffer();
        Producer producer1 = new Producer(buffer, "Producer1");
        Producer producer2 = new Producer(buffer, "Producer2");

        Consumer consumer1 = new Consumer(buffer, "Consumer1");
        Consumer consumer2 = new Consumer(buffer, "Consumer2");
        Consumer consumer3 = new Consumer(buffer,  "Consumer3");



        Thread thread1 = new Thread(producer1);
        Thread thread2 = new Thread(producer2);
        Thread thread3 = new Thread(consumer1);
        Thread thread4 = new Thread(consumer2);
        Thread thread5 = new Thread(consumer3);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}