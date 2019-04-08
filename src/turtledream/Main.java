package turtledream;

import java.io.*;

class MyInputStream extends FileInputStream{

    private int speed;
    private long lastTime = 0;

    MyInputStream(File file, int speed) throws FileNotFoundException {
        super(file);
        this.speed = speed;
    }

    @Override
    public synchronized int read() throws IOException {
        long t = 1000 / speed;
        long currentTime = System.currentTimeMillis();
        if( lastTime == 0){
            try {
                Thread.sleep((t));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (currentTime - lastTime < t) {
            try {
                Thread.sleep((int)(t - currentTime - lastTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lastTime = System.currentTimeMillis();
        return super.read();

    }

    @Override
    public synchronized int read(byte[] b) throws IOException {
        long t = 1000 * b.length / speed;
        long currentTime = System.currentTimeMillis();
        if( lastTime == 0){
            try {
                Thread.sleep((t));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (currentTime - lastTime < t) {
            try {
                Thread.sleep(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lastTime = System.currentTimeMillis();
        return super.read(b);
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        long t = 1000 * (len-off) / speed;
        long currentTime = System.currentTimeMillis();
        if( lastTime == 0){
            try {
                Thread.sleep((t));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (currentTime - lastTime < t) {
            try {
                Thread.sleep((int)(t - currentTime - lastTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lastTime = System.currentTimeMillis();
        return super.read(b, off, len);
    }
}

class MyThread extends Thread{
    private String p;
    private int speed;
    MyThread(String p,int speed){
        this.p = p;
        this.speed = speed;
    }
    @Override
    public void run() {
        try {
            MyInputStream input_stream = new MyInputStream(new File(p), speed);
            byte[] buffer = new byte[input_stream.available()];
            long cur_time = System.currentTimeMillis();

            input_stream.read(buffer);
            String s = new String(buffer);
            long cur_time2 = System.currentTimeMillis();
            synchronized (System.out) {
                System.out.println(s);
                System.out.println("");
                System.out.println("Время чтения файла = " + (cur_time2 - cur_time) + " миллисек");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

        super.run();
    }
}

public class Main
{
    public static void main(String[] args) {
        Thread myThread = new MyThread("/*Расположение читаемого файла*/", 500);
        Thread myThread2 = new MyThread("/*Расположение читаемого файла*/", 1500);
        myThread.start();
        myThread2.start();
    }
}
