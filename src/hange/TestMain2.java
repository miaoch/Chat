package hange;

import hange.client.Client;
import hange.pojo.TranObject;
import hange.pojo.TranObjectType;

/**
 * Created by miaoch on 2017/5/26.
 */
public class TestMain2 {
    public static void main(String args[]) throws InterruptedException {
        Client c2 = new Client("127.0.0.1", Constants.SERVER_PORT, 2);
        c2.start();
        while (true) {
            TranObject msg = new TranObject();
            msg.setType(TranObjectType.MESSAGE);
            msg.setFromUser(2);
            msg.setToUser(1);
            msg.setObject("c2 say hi to c1!");
            c2.getClientOutputThread().setMsg(msg);
            Thread.sleep(10000);
        }
    }
}
