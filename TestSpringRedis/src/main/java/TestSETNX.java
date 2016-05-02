import common.SpringRedisBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.Key;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lansi on 2015/8/16.
 */
public class TestSETNX {

    private static RedisTemplate rt = SpringRedisBuilder.build();

    private static final String key = "test.locker";

    private static int threadSize = 10;

    public static void main(String[] args) {
        CountDownLatch cdl = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            final CountDownLatch finalCdl = cdl;
            new Thread(() -> {
                boolean res = rt.opsForValue().setIfAbsent(key, String.valueOf(System.currentTimeMillis() + 3000));
                System.out.println("first==="+Thread.currentThread().getName() + "======" + res);
                if (res) {
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //delete the locker
                    rt.delete(key);
                }
                finalCdl.countDown();
            }).start();
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cdl = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            final CountDownLatch finalCdl1 = cdl;
            new Thread(() -> {
                boolean res = rt.opsForValue().setIfAbsent(key, String.valueOf(System.currentTimeMillis() + 3000));
                System.out.println("second==="+Thread.currentThread().getName() + "======" + res);
                if (res) {
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //delete the locker
                    rt.delete(key);
                }
                finalCdl1.countDown();
            }).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
