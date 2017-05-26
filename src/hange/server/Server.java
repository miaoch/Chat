package hange.server;

import hange.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聊天服务端
 * Created by miaoch on 2017/5/26.
 */
public class Server {
    private ExecutorService executorService;// 线程池
    private ServerSocket serverSocket = null;//服务端的socket
    private boolean isStarted = true;//是否循环等待

    /**
     * 新建一个服务，创建线程池和serverSocket。
     * 异常则调用quit()
     */
    public Server() {
        try {
            // 创建线程池，池中具有(cpu个数*50)条线程
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                    .availableProcessors() * 50);
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            quit();
        }
    }

    /**
     * 启动服务，为所有连接上的客户端分配线程服务。
     */
    public void start() {
        System.out.println(" 服务器已启动...");
        try {
            while (isStarted) {
                Socket socket = serverSocket.accept();//客户端连接的socket
                String ip = socket.getInetAddress().toString();
                System.out.println(" 用户：" + ip + " 已建立连接");
                // 为支持多用户并发访问，采用线程池管理每一个用户的连接请求
                if (socket.isConnected()) {
                    executorService.execute(new SocketTask(socket));// 添加到线程池
                }
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端管理读写线程的线程类
     */
    private final class SocketTask implements Runnable {
        private Socket socket = null;//客户端socket
        private InputThread in;//读线程
        private OutputThread out;//写线程
        private OutputThreadMap map;//存放写线程的map

        public SocketTask(Socket socket) {
            this.socket = socket;
            map = OutputThreadMap.getInstance();
        }
        @Override
        public void run() {
            out = new OutputThread(socket);
            in = new InputThread(socket, out, map);
            in.start();
            out.start();
        }
    }

    /**
     * 退出
     */
    public void quit() {
        try {
            this.isStarted = false;
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
