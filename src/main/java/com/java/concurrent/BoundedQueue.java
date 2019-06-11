package com.java.concurrent;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-06 15:46
 **/
public class BoundedQueue {


    BoundedQueue(int size) {

        item = new Object[size];

    }

    private Object[] item;
    private int count, removeIndex, addIndex;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();


    public void insertData(Object object) throws InterruptedException {

        lock.lock();
        try {
            while (count == item.length) {
                notFull.await();
            }

            item[addIndex] = object;
            if (++ addIndex == item.length) {
                addIndex = 0;
            }

            Arrays.asList(item).forEach(System.out::println);
            System.out.println("---------------------------");
            count++;
            notEmpty.signal();


        } finally {
            lock.unlock();
        }

    }


    public Object removeData() throws InterruptedException {

        lock.lock();

        try {

            while (count == 0 ){
                notEmpty.await();
            }
            Object o = item[removeIndex];
            item[removeIndex] = null;
            if (++ removeIndex == item.length) {
                removeIndex = 0;
            }
            Arrays.asList(item).forEach(System.out::println);
            System.out.println("---------------------------");
            count--;
            notFull.signal();
            return o;


        } finally {

            lock.unlock();

        }


    }


    public static void main(String[] arg) throws InterruptedException {

        BoundedQueue boundedQueue = new BoundedQueue(4);


        class Worker extends Thread {
            @Override
            public void run() {
                try {
                    boundedQueue.removeData();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        class AddWorker extends Thread {

            private Object addObject;

            AddWorker(Object addObject) {
                this.addObject = addObject;
            }

            @Override
            public void run() {
                try {
                    boundedQueue.insertData(addObject);
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        for (int i = 0; i < 10; i++) {

            Worker worker = new Worker();
            worker.setDaemon(true);
            worker.start();
        }


        for (int i = 0; i < 20; i++) {
            AddWorker addWorker = new AddWorker("aaa");
            addWorker.setDaemon(true);
            addWorker.start();
        }


        TimeUnit.MINUTES.sleep(4);


    }


}
