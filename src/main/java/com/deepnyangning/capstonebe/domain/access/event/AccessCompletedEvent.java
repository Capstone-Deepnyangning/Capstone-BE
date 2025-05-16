package com.deepnyangning.capstonebe.domain.access.event;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccessCompletedEvent {
    private final User user;
    private final AccessType accessType;
}
