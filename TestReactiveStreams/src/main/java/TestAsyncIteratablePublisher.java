import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.reactivestreams.example.unicast.AsyncIterablePublisher;
import org.reactivestreams.example.unicast.AsyncSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lansi on 2016/4/4.
 */
public class TestAsyncIteratablePublisher {

    public static void main(String[] args) {
        String[] array = new String[]{"a", "b", "c", "d", "aa", "bb", "cc", "dd", "aaa", "bbb", "ccc", "ddd", "abcd", "dcba"};
        final List<String> strings = Arrays.asList(array);
        final Iterator<String> iterator = strings.iterator();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Publisher<String> pub = new AsyncIterablePublisher<String>(strings, 1, executor);
        System.out.println("start subcribing");
        AsyncSubscriber<String> s = new AsyncSubscriber<String>(executor) {
            @Override
            protected boolean whenNext(String element) {
                return iterator.hasNext();
            }
        };
        pub.subscribe(s);
//        pub.subscribe(new TAIPSubscriber());
        System.out.println("subscribe completed");
    }

    static final class TAIPSubscriber implements Subscriber<String>{

        public void onSubscribe(Subscription s) {
            s.request(1);
        }

        public void onNext(String s) {
            System.out.println("[" + Thread.currentThread() + "]===" + s);
        }

        public void onError(Throwable t) {
            System.out.println("[" + Thread.currentThread() + "]===" + t.getMessage());
        }

        public void onComplete() {
            System.out.println("[" + Thread.currentThread() + "]=== finished");
        }
    }

}
