package hange.client;
import hange.pojo.TranObject;

/**
 * 消息监听接口
 * Created by miaoch on 2017/5/26.
 */
public interface MessageListener {
    void message(TranObject msg);
}
