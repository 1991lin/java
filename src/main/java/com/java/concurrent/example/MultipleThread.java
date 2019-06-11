package com.java.concurrent.example;

import java.util.concurrent.TimeUnit;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-08 17:38
 **/
public class MultipleThread {


//    现在有T1、T2、T3三个线程，你怎样保证T2在T1执行完后执行，T3在T2执行完后执行？


    public static void main(String[] arg) throws InterruptedException {


        Thread one = new Thread(() -> {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("This is one");
        });
        one.start();
        one.join();

        Thread two = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("This is two");
        });

        two.start();
        two.join();
        Thread three = new Thread(() -> {
            System.out.println("This is three");
        });

        three.start();
        three.join();

        TimeUnit.MINUTES.sleep(4);

    }



}
