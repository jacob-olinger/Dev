import java.time.Duration;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class implementVirtualThread {
    public static void main(String[] args){
       long st = System.nanoTime();
        pr yo=new pr();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int j = 0; j < 10_000; j++) {
                int i = j;
                executor.submit(() -> {
                    //Thread.sleep(Duration.ofSeconds(1));
                    yo.run();

                    return i;
                });
            }
        }  // executor.close() is called implicitly, and waits
        System.out.println("done with parrelell");
        long en = System.nanoTime();
        long par = en-st;
//not possible with just normal parrellel threading too many threads
        st=System.nanoTime();
        for(int i=0;i<10000;i++){
            System.out.print(1);
        }
        en=System.nanoTime();
        long sin = en-st;
        System.out.println();
        System.out.println("Parellel with Virtual Threads time: " + par/1000000);
        System.out.println("Single thread: " + sin/1000000);
        System.out.println("ratio of parrelel to single: " + par/sin);
    }
}
class pr implements Runnable{
    static Thread op =Thread.startVirtualThread(new Runnable() {
        @Override
        public void run() {
            System.out.print(1);
        }
    });
    @Override
    public void run() {
        System.out.print(1);
    }
    public static void start(){

    }

}
