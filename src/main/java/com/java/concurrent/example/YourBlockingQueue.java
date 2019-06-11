package com.java.concurrent.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-08 18:05
 **/
public class YourBlockingQueue {


    private LinkedList<Integer> data = new LinkedList<>();
    private int max_size;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");


    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    YourBlockingQueue(int max_size) {
        this.max_size = max_size;
    }


    public void put(int i) throws InterruptedException {

        lock.lock();
        try {
            while (data.size() + 1 > max_size) {
                notFull.await();
            }

            data.add(i);
            data.stream().forEach(x -> {
                System.out.print(dateTimeFormatter.format(LocalDateTime.now()) + " -- " + x + " ; ");

            });
            System.out.println("");
            notEmpty.signal();

        } finally {
            lock.unlock();
        }


    }

    /**
     * remove the last
     */
    public void remove() throws InterruptedException {
        lock.lock();
        try {
            while (data.size() <= 0) {
                notEmpty.await();
            }


            data.removeLast();
            data.stream().forEach(x -> {
                System.out.print(dateTimeFormatter.format(LocalDateTime.now()) + " -- " + x + " ; ");

            });
            System.out.println("");
            notFull.signal();

        } finally {
            lock.unlock();
        }
    }


//    用Java实现阻塞队列

    public static void main(String[] arg) {
        YourBlockingQueue blockingQueue = new YourBlockingQueue(2);

        class AddElementThread extends Thread {

            private YourBlockingQueue yourBlockingQueue;

            AddElementThread(YourBlockingQueue yourBlockingQueue) {
                this.yourBlockingQueue = yourBlockingQueue;
            }

            @Override
            public void run() {
                try {
                    while (true) {
                        yourBlockingQueue.put(1);
                        TimeUnit.SECONDS.sleep(1);
                    }


                } catch (InterruptedException e) {
                    System.out.println("InterruptedException when putting the element in the yourBlockingQueue");
                }

            }
        }


        class RemoveElementThread extends Thread {
            private YourBlockingQueue yourBlockingQueue;


            RemoveElementThread(YourBlockingQueue yourBlockingQueue) {
                this.yourBlockingQueue = yourBlockingQueue;
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        yourBlockingQueue.remove();
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException when removing element in yourBlockingQueue");
                    }

                }
            }
        }


        AddElementThread addElementThread = new AddElementThread(blockingQueue);
        RemoveElementThread removeElementThread = new RemoveElementThread(blockingQueue);


        addElementThread.start();
        removeElementThread.start();

        while (true) {
            Thread.yield();
        }


    }
}
