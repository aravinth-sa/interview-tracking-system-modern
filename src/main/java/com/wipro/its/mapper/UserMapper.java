package com.wipro.its.mapper;

import com.wipro.its.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Placeholder for future DTO mappings (e.g., UserResponse for admin user listing)
    default String toDisplayName(User user) {
        return user.getUserId() + " (" + user.getUserType().name() + ")";
    }
}
