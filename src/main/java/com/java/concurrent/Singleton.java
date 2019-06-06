package com.java.concurrent;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-06 10:42
 **/
public class Singleton {


    private Singleton(){}


    private static volatile Singleton singleton;


    public static Singleton getSingletonInstance() {

        if (singleton == null) {

            synchronized (Singleton.class){

                if (singleton == null) {

                    singleton = new Singleton();

                }

            }

        }

        return singleton;
    }


    public static void main(String[] arg){

        System.out.println(Singleton.getSingletonInstance());

    }
}
