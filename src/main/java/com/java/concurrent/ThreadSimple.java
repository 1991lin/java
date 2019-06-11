package com.java.concurrent;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-08 13:16
 **/
public class ThreadSimple {


    public static void main(String[] arg){

        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(5);


        System.out.println(
                ManagementFactory.getThreadMXBean().getThreadCount()
        );

        threadPoolExecutor.execute(() -> {

            for (;;){

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("........");

            }

        });


        while (Thread.activeCount() > 2){

            Thread.yield();

        }


    }
}
