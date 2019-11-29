package main.java.kocev.nenad.serialization;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    public enum UserRole {CLIENT, FREELANCER}
    private String userName;
    private UserRole userRole;

    public User(){}

    public User(String userName, UserRole userRole) {
        this.userName = userName;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) &&
                userRole == user.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userRole);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
