package com.java.concurrent.example;

import java.util.concurrent.TimeUnit;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-12 00:28
 **/

public class DeadLock {


    private  Object object_A = new Object();
    private  Object object_B = new Object();


    public Object getObject_A() {
        return object_A;
    }

    public void setObject_A(Object object_A) {
        this.object_A = object_A;
    }

    public Object getObject_B() {
        return object_B;
    }

    public void setObject_B(Object object_B) {
        this.object_B = object_B;
    }

    /**
     * 实现死锁
     * @param arg
     */
    public static void main(String[] arg){


       class Thread_one extends Thread{
           private Object a;
           private Object b;

           Thread_one(Object a, Object b){
               this.a = a;
               this.b = b;
           }


           @Override
           public void run() {
               synchronized (a){

                   try {
                       TimeUnit.SECONDS.sleep(2);
                   } catch (InterruptedException e) {
                       System.out.println("InterruptedException ... ");
                   }
                   synchronized (b){

                       System.out.println("This is the Thread one");

                   }

               }
           }
       }

       class Thread_two extends Thread{

           private Object a;
           private Object b;
           Thread_two(Object a, Object b){
               this.a = a;
               this.b = b;
           }


           @Override
           public void run() {
               synchronized (b){

                   try {
                       TimeUnit.SECONDS.sleep(3);
                   } catch (InterruptedException e) {
                       System.out.println("Interrupted exception");
                   }

                   synchronized (a){

                       System.out.println("This is the Thread two");

                   }

               }
           }
       }


       DeadLock deadLock = new DeadLock();



       Thread_one one = new Thread_one(deadLock.getObject_A(), deadLock.getObject_B());
       Thread_two two = new Thread_two(deadLock.getObject_A(), deadLock.getObject_B());

       one.start();
       two.start();


       while (true) {
           Thread.yield();
       }


    }
}
