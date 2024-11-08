import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurentMandelbrot extends JFrame {
    private final int MAX_ITER = 1000;
    private final double ZOOM = 150;
    private BufferedImage I;

    public ConcurentMandelbrot(int width, int height) {
        super("Manelbrot Set");
        setBounds(100, 100, width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    private int pixelColor(int x, int y) {
        double zx=0, zy=0, cX=(x-getWidth()/2)/ZOOM, cY=(y-getHeight()/2)/ZOOM, tmp;
        int iter = MAX_ITER;
        while (zx * zx + zy * zy < 4 && iter > 0) {
            tmp = zx * zx - zy * zy + cX;
            zy = 2.0 * zx * zy + cY;
            zx = tmp;
            iter--;
        }
        return iter | (iter << 8);
    }
    public void calculateMandelbrot(int numThreads,int tasksPerThread) {
        {
            ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
            List<Future<?>> futures = new ArrayList<>();

            for (int y = 0; y < getHeight(); y += tasksPerThread) {
                for (int x = 0; x < getWidth(); x += tasksPerThread) {
                    final int startX = x;
                    final int startY = y;
                    Future<?> future = executorService.submit(() -> {
                        for (int j = startY; j < startY + tasksPerThread && j < getHeight(); j++) {
                            for (int i = startX; i < startX + tasksPerThread && i < getWidth(); i++) {
                                int color = pixelColor(i, j);
                                I.setRGB(i, j, color);
                            }
                        }
                    });
                    futures.add(future);
                }
            }
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
        }
    }

    @Override
    public void paint(java.awt.Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    private static int[] calculateTask(int numThreads, int width, int height){
        return new int[]{numThreads,10*numThreads,width*height/numThreads};
//        return new int[]{numThreads};

    }

    public static void main(String[] args) {
        int width = 800;
        int height = 600;
        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"Threads", "Tasks", "Average Time (ms)"});
        int index = 0;
        int numThreads = Runtime.getRuntime().availableProcessors();

        int[] threadValues = {1,numThreads,4*numThreads};
        int[] taskValues;
        for(int threads: threadValues){
            taskValues = calculateTask(threads,width,height);
            for(int tasks: taskValues){
                ConcurentMandelbrot mandelbrot = new ConcurentMandelbrot(width, height);
                long totalTime = 0;
                System.out.printf("Konfiguracja: %d watków, %d zadań\n", threads, tasks);
                for (int i = 0; i < 10; i++) { // Powtarzamy test 10 razy
                    long startTime = System.nanoTime();
                    mandelbrot.calculateMandelbrot(threads, tasks);
                    long endTime = System.nanoTime();
                    totalTime += (endTime - startTime);
                }
//                mandelbrot.setVisible(true);
                double averageTime = totalTime / 10.0 / 1_000_000;
                System.out.printf("Średni czas: %.2f ms\n", averageTime);
                index++;
                results.add(new String[]{String.valueOf(threads), String.valueOf(tasks), String.format("%.2f", averageTime)});

            }
        }

        try (FileWriter csvWriter = new FileWriter("mandelbrot_results.csv")) {
            for (String[] rowData : results) {
                System.out.println(rowData[0] + " " + rowData[1] + " " + rowData[2]);
                csvWriter.append(rowData[0]).append(",");
                csvWriter.append(rowData[1]).append(",");
                csvWriter.append("\"").append(rowData[2]).append("\""); // wrap rowData[2] in quotes
                csvWriter.append("\n");
            }
            System.out.println("Wyniki zapisano do pliku mandelbrot_results.csv");
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku CSV: " + e.getMessage());
        }
        return;
    }
}
