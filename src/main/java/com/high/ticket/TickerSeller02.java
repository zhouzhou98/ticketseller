package com.high.ticket;

import java.util.concurrent.TimeUnit;

public class TickerSeller02 implements Runnable{
    private int index = 1;
    private boolean change = true;

    private final static Object object = new Object();
    @Override
    public void run() {
        synchronized (object) {
            while (index <= Constant.ticketSum && change) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第： " + (index++) + "张票");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                change = false;
            }
            change = true;
        }
    }
    public static void main(String[] args) {
        final TickerSeller02 task = new TickerSeller02();
        for (int i = 0; i < Constant.ticketSum / 4 + 1; i++) {
            Thread windowThread1 = new Thread(task, "一号窗口");
            Thread windowThread2 = new Thread(task, "二号窗口");
            Thread windowThread3 = new Thread(task, "三号窗口");
            Thread windowThread4 = new Thread(task, "四号窗口");
            windowThread1.start();
            windowThread2.start();
            windowThread3.start();
            windowThread4.start();
        }
    }

}
