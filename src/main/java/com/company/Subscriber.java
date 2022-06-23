package com.company;

import com.company.User.User;

import java.io.IOException;

public interface Subscriber {
    public void update(User user) throws IOException;
}
