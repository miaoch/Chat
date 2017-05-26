package hange;

import hange.client.Client;
import hange.pojo.TranObject;
import hange.pojo.TranObjectType;
import hange.server.Server;

/**
 * Created by miaoch on 2017/5/26.
 */
public class TestMain {
    public static void main(String args[]) throws InterruptedException {
        Client c1 = new Client("127.0.0.1", Constants.SERVER_PORT, 1);
        c1.start();
        while (true) {
            TranObject msg = new TranObject();
            msg.setType(TranObjectType.MESSAGE);
            msg.setObject("c1 say hello to c2!");
            msg.setFromUser(1);
            msg.setToUser(2);
            c1.getClientOutputThread().setMsg(msg);
            Thread.sleep(10000);
        }
    }
}
