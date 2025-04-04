package com.deepnyangning.capstonebe.domain.studyroom.controller;

import com.deepnyangning.capstonebe.domain.studyroom.service.StudyRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyRoomController {
    private final StudyRoomService studyRoomService;
}
