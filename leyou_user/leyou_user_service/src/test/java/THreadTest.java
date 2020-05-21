import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class THreadTest extends Thread {
    @Override
    public void run() {
        Lock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();
     //   currentThread().setName();
       // setName();
        for(int i=0;i<10;i++){
            System.out.println("i======"+i);
        }
    }

    public static void main(String[] args) {
        new Runnable() {
            @Override
            public void run() {

            }
        };
        int i=1/0;
        System.out.println("main方法执行。。。。。。start");
        THreadTest tHreadTest = new THreadTest();
        tHreadTest.setPriority(1);
        tHreadTest.start();

        System.out.println("main方法执行。。。。。。end");

    }
}
