package com.wipro.its.mapper;

import com.wipro.its.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public String toDisplayName(User user) {
        return user.getUserId() + " (" + user.getUserType().name() + ")";
    }
}
