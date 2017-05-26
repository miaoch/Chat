package hange.pojo;

import java.io.Serializable;

/**
 * Created by miaoch on 2017/5/26.
 */
public class User implements Serializable {
    private int userId;
    private String userName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
