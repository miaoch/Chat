package hange.client;

import hange.pojo.TranObject;

/**
 * Created by miaoch on 2017/5/26.
 */
public class TestListener implements MessageListener{
    @Override
    public void message(TranObject msg) {
        System.out.println(msg.getObject());
    }
}
