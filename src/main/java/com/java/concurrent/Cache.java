package com.java.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-06 15:13
 **/
public class Cache {

    private Map<String, Object> map = new HashMap<>();

    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();



    public Object getValue(String key){
        readLock.lock();
        try {
            return map.get(key);
        }finally {
            readLock.unlock();
        }
    }

    public void insertValue(String key, Object value){

        writeLock.lock();
        try {
            map.put(key, value);
        }finally {
            writeLock.unlock();
        }
    }

    public void deleteValue(String key){
        writeLock.lock();
        try {
            map.remove(key);
        }finally {
            writeLock.unlock();
        }
    }


    public static void main(String[] arg) throws InterruptedException {

        class AddWorker extends Thread{
            private Cache cache;
            AddWorker(Cache cache){
                this.cache = cache;
            }



            @Override
            public void run() {

                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        cache.insertValue("test", "me");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }



        class DeleteWorker extends Thread{
            private Cache cache;
            DeleteWorker(Cache cache){
                this.cache = cache;
            }


            @Override
            public void run() {

                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        cache.deleteValue("test");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
        Cache cache = new Cache();

        for (int i = 0; i < 10; i++) {
            AddWorker addWorker = new AddWorker(cache);
            addWorker.setDaemon(true);
            addWorker.start();
        }

        for (int i = 0; i < 10; i++) {
            DeleteWorker deleteWorker = new DeleteWorker(cache);
            deleteWorker.setDaemon(true);
            deleteWorker.start();
        }

        Thread thread = new Thread(() -> {

            while(true){

                System.out.println(cache.getValue("test"));
            }
        });

        thread.setDaemon(true);
        thread.start();


        TimeUnit.MINUTES.sleep(2);

    }



}
