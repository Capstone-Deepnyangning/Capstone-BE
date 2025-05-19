package com.deepnyangning.capstonebe.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE_IDENTIFIER(HttpStatus.BAD_REQUEST, "이미 존재하는 학번입니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 역할입니다."),
    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "예약 가능 시간이 아닙니다."),
    INVALID_RESERVATION_DURATION(HttpStatus.BAD_REQUEST, "예약은 1시간 또는 2시간 단위로만 가능합니다."),
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST, "해당 시간대에 이미 예약이 존재합니다."),
    MISSING_SIMILARITY(HttpStatus.BAD_REQUEST, "안면 인식 인증을 위해 유사도 값이 필요합니다."),
    INSUFFICIENT_SIMILARITY(HttpStatus.BAD_REQUEST, "안면 인식 유사도가 충분하지 않습니다."),
    INVALID_QR_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 QR 코드입니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다."),
    INVALID_STUDY_ROOM_PARTICIPANTS(HttpStatus.BAD_REQUEST, "동반 이용자 수가 부족합니다."),
    ALREADY_RESERVED_ON_DATE(HttpStatus.BAD_REQUEST, "해당 날짜에 이미 예약된 스터디룸이 존재합니다."),
    INVALID_SELF_PARTICIPATION(HttpStatus.BAD_REQUEST, "본인의 학번으로는 신청할 수 없습니다."),
    FACE_REGISTRATION_FAILED(HttpStatus.BAD_REQUEST, "얼굴 등록에 실패했습니다."),
    INVALID_VIDEO_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 영상 파일입니다."),

    /*
     * 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    TOKEN_ALREADY_LOGOUT(HttpStatus.UNAUTHORIZED, "이미 로그아웃된 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    INCORRECT_CURRENT_PASSWORD(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다."),

    /*
     * 403 FORBIDDEN: 권한이 없는 사용자의 요청
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    STUDY_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디룸을 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디룸 예약을 찾을 수 없습니다."),
    FACE_ISSUE_REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "안면 인식 문제 신고 데이터를 찾을 수 없습니다."),
    ACCESS_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "출입 로그를 찾을 수 없습니다."),
    GLOBAL_AVG_NOT_FOUND(HttpStatus.NOT_FOUND, "전체 사용자의 출입 통계 기록을 찾을 수 없습니다."),
    AI_SERVER_ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND, "AI 서버 엔드포인트를 찾을 수 없습니다."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청 방식입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    /*
     * 503 SERVICE_UNAVAILABLE: 서비스 이용 불가
     */
    REDIS_OPERATION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "Redis 작업에 실패했습니다."),
    AI_SERVER_COMMUNICATION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "AI 서버와의 통신에 실패했습니다."),
    AI_SERVER_UNREACHABLE(HttpStatus.SERVICE_UNAVAILABLE, "AI 서버에 연결할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
