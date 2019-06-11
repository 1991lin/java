package com.java.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-06 14:00
 **/
public class TwinLock {

    public TwinLock(int count) {
        sync = new Sync(count);
    }

    private Sync sync;


    public void lock() {
        sync.acquireShared(1);
    }

    public void unlock() {
        sync.releaseShared(1);
    }


    private static class Sync extends AbstractQueuedSynchronizer {

        Sync(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("count must be larger than 0");
            }
            setState(count);

        }

        @Override
        protected int tryAcquireShared(int reduceCount) {
            while (true) {
                int current = getState();
                int newCount = current - reduceCount;
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return reduceCount;
                }

            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            while (true) {
                int current = getState();
                int newCount = current + returnCount;
                if (compareAndSetState(current, newCount)) {
                    return true;
                }


            }
        }

    }


    public static void main(String[] arg) throws InterruptedException {

        TwinLock twinLock = new TwinLock(9);

        class Worker extends Thread {


            @Override
            public void run() {
                while (true) {
                    twinLock.lock();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        twinLock.unlock();
                    }
                }
            }
        }


        for (int i = 0; i < 10; i++) {

            Worker worker = new Worker();

            worker.setDaemon(true);

            worker.start();


        }


        TimeUnit.SECONDS.sleep(100);

    }

}
