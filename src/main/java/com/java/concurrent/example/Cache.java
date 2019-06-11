package com.java.concurrent.example;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-08 17:42
 **/
public class Cache <K,V> {


    private final HashMap<K,V> cacheData;

    Cache(){
        cacheData = new HashMap<K, V>();
    }

    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();



    public <K> Object get(K value){
        readLock.lock();

        try {
            return cacheData.get(value);
        }finally {
            readLock.unlock();
        }

    }


    public void put(K key, V value){
        writeLock.lock();

        try {
            cacheData.put(key, value);
        }finally {
            writeLock.unlock();
        }
    }


    public void remove(K key){

        writeLock.lock();
        try {
            cacheData.remove(key);
        }finally {
            writeLock.unlock();
        }

    }





//    你需要实现一个高效的缓存，它允许多个用户读，但只允许一个用户写，以此来保持它的完整性，你会怎样去实现它？


    public static void main(String[] arg){




    }

}
