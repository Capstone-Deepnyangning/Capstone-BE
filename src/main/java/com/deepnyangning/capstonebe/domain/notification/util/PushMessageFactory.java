package com.deepnyangning.capstonebe.domain.notification.util;

import com.deepnyangning.capstonebe.domain.notification.entity.PushType;

public class PushMessageFactory {
    public static String getMessage(PushType type){
        return switch (type){
            case ENTRY_SUCCESS ->  "입장 완료! 오늘도 힘찬 하루 보내세요.";
            case EXIT_SUCCESS -> "퇴장이 완료되었습니다. 오늘도 고생하셨어요!";
            case STUDY_ROOM_REMINDER -> "예약하신 스터디룸 이용까지 30분 남았어요!";
            case FACE_ISSUE_REPORTED -> "안면 인식 오류가 접수되었습니다. 조치가 필요합니다.";
        };
    }

    public static String getTargetScreen(PushType type){
        return switch (type) {
            case ENTRY_SUCCESS, EXIT_SUCCESS -> "home";
            case STUDY_ROOM_REMINDER -> "studyroom/reservations";
            case FACE_ISSUE_REPORTED -> "admin/reports";
        };
    } // 프론트랑 얘기해서 보완
}
