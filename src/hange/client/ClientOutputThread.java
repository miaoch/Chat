package hange.client;

import hange.pojo.TranObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/5/26.
 */
public class ClientOutputThread extends Thread {
    private Socket socket;
    private ObjectOutputStream oos;
    private boolean isStart = true;
    private TranObject msg;

    public ClientOutputThread(Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    // 这里处理跟服务器是一样的
    public void setMsg(TranObject msg) {
        this.msg = msg;
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        try {
            while (isStart) {
                if (msg != null) {
                    oos.writeObject(msg);
                    oos.flush();
                    synchronized (this) {
                        wait();// 发送完消息后，线程进入等待状态
                    }
                }
            }
            oos.close();// 循环结束后，关闭输出流和socket
            if (socket != null)
                socket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
