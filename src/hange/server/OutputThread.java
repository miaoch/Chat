package hange.server;

import hange.pojo.TranObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by miaoch on 2017/5/26.
 */
public class OutputThread extends Thread {
    private ObjectOutputStream oos;//输出流
    private TranObject object;//消息对象
    private boolean isStart = true;// 循环标志位
    private Socket socket;//客户端socket

    public OutputThread(Socket socket) {
        try {
            this.socket = socket;
            this.oos = new ObjectOutputStream(socket.getOutputStream());// 在构造器里面实例化对象输出流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置写线程是否终止
     * @param isStart
     */
    public void setStart(boolean isStart) {//用于外部关闭写线程
        this.isStart = isStart;
    }

    // 调用写消息线程，设置了消息之后，唤醒run方法，可以节约资源
    public void sendMessage(TranObject object) {
        this.object = object;
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        try {
            while (isStart) {
                // 没有消息写出的时候，线程等待
                synchronized (this) {
                    wait();
                }
                if (object != null) {
                    oos.writeObject(object);
                    oos.flush();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 循环结束后，关闭流，释放资源
                if (oos != null) {
                    oos.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
