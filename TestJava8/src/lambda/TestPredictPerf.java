package lambda;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Thomas on 2015/5/3.
 */
public class TestPredictPerf {
    public static final int size = 100000;

    public static final CountDownLatch cdl = new CountDownLatch(2);

    public static void main(String[] args) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()-> {
            List<Double> list = new ArrayList<Double>();
            for (int i = 0; i < size; i++) {
                list.add(i, Math.random());
            }
            Iterator i = list.iterator();
            long start = System.nanoTime();
            while (i.hasNext()) {
                Double d = (Double) i.next();
                if (d < 0.5) {
                    i.remove();
                }
            }
            double spend = (System.nanoTime() - start)/1000000;
            System.out.println("normal spend: "+spend+" count: "+list.size());
            cdl.countDown();
        }).start();
        new Thread(()->{
            List<Double> list = new ArrayList<Double>();
            for (int i = 0; i < size; i++) {
                list.add(i, Math.random());
            }
            long start = System.nanoTime();
            list.removeIf((e) -> e < 0.5);
            double spend = (System.nanoTime() - start) / 1000000;
            System.out.println("predict spend: "+spend+" count: "+list.size());
            cdl.countDown();
        }).start();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
