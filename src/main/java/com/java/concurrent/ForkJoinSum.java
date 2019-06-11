package com.java.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @program: java
 * @author: Eric
 * @create: 2019-06-08 13:50
 **/
public class ForkJoinSum {


    static class SumTask extends RecursiveTask {

        private final static int THRESHOLD = 2;

        private int start;
        private int end;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Object compute() {

            int sum = 0;
            boolean flag = (end - start) <= THRESHOLD;
            if (flag) {
                for (int i = start; i <= end; i++) {

                    sum += i;

                }
            } else {
                int middle = start + (end - start) / 2;
                SumTask frontTask = new SumTask(start, middle);
                SumTask laterTask = new SumTask(middle + 1, end);

                frontTask.fork();
                laterTask.fork();
                int left = (Integer) frontTask.join();
                int right = (Integer) laterTask.join();
                sum = left + right;
            }
            return sum;

        }
    }


    public static void main(String[] arg) throws ExecutionException, InterruptedException {

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        SumTask sumTask = new SumTask(1, 5);

        ForkJoinTask task = (ForkJoinTask)forkJoinPool.submit(sumTask);


        System.out.println(task.get());


    }


}
