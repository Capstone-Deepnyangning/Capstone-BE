package com.deepnyangning.capstonebe.domain.user.event;

import com.deepnyangning.capstonebe.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDeletedEvent {
    private final User user;
}
