package com.high.ticket;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TickerSeller01 implements Runnable{
    public static AtomicInteger ticketSum = new AtomicInteger(Constant.ticketSum);

    @Override
    public void run() {
        int count = 0;
        while ((count=ticketSum.decrementAndGet()) >= 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "卖出了第" + ++count + "张票");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new TickerSeller01(), "一号窗口");
        Thread t2 = new Thread(new TickerSeller01(), "二号窗口");
        Thread t3 = new Thread(new TickerSeller01(), "三号窗口");
        Thread t4 = new Thread(new TickerSeller01(), "四号窗口");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
