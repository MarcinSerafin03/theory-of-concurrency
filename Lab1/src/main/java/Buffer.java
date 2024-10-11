// producentów i konsumentów ma być wiecej niz jeden (konsument musi mieć co zabrać z bufora, a producent miejsce do wstawienia wiadomości)


public class Buffer{
    private String message;
    private boolean empty = true;

    public synchronized void put(String message, String name) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.message = message;
        empty = false;
        notifyAll();
        System.out.println("Produced: " + message + " by " + name);
    }

    public synchronized String take(String name) {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        empty = true;
        notifyAll();
        System.out.println("Consumed: " + message + " by " + name);
        return message;
    }

}
