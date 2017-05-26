package hange.client;

import hange.pojo.TranObject;
import hange.pojo.TranObjectType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by miaoch on 2017/5/26.
 */
public class Client {
    private Socket client;
    private ClientThread clientThread;
    private String ip;
    private int port;
    private int userId;

    public Client(String ip, int port, int userId) {
        this.ip = ip;
        this.port = port;
        this.userId = userId;
    }

    public boolean start() {
        try {
            client = new Socket();
            client.connect(new InetSocketAddress(ip, port), 3000);
            if (client.isConnected()) {
                clientThread = new ClientThread(client);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 直接通过client得到读线程
    public ClientInputThread getClientInputThread() {
        return clientThread.getIn();
    }

    // 直接通过client得到写线程
    public ClientOutputThread getClientOutputThread() {
        return clientThread.getOut();
    }

    // 直接通过client停止读写消息
    public void setIsStart(boolean isStart) {
        clientThread.getIn().setStart(isStart);
        clientThread.getOut().setStart(isStart);
    }

    public class ClientThread extends Thread {
        private ClientInputThread in;
        private ClientOutputThread out;

        public ClientThread(Socket socket) {
            in = new ClientInputThread(socket);
            out = new ClientOutputThread(socket);
        }
        public void run() {
            in.start();
            out.start();
            //发送登录消息
            TranObject login = new TranObject();
            login.setFromUser(userId);
            login.setType(TranObjectType.LOGIN);
            out.setMsg(login);
        }
        // 得到读消息线程
        public ClientInputThread getIn() {
            return in;
        }
        // 得到写消息线程
        public ClientOutputThread getOut() {
            return out;
        }
    }
}