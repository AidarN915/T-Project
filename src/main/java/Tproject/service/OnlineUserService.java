package Tproject.service;

import java.util.HashSet;
import java.util.Set;

public interface OnlineUserService {
    public void addOnline(String username);
    public void removeOnline(String username);
    public boolean isOnline(String username);
    public void sendOnlineUsers();
}
