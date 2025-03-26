package com.deepnyangning.capstonebe.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    /*
     * 200 OK: 조회 성공
     */
    SELECT_SUCCESS(HttpStatus.OK, "성공적으로 조회했습니다."),

    /*
     * 200 OK: 삭제 성공
     */
    DELETE_SUCCESS(HttpStatus.OK, "성공적으로 삭제했습니다."),

    /*
     * 201 CREATED: 삽입 성공
     */
    INSERT_SUCCESS(HttpStatus.CREATED, "성공적으로 등록했습니다."),

    /*
     * 204 NO_CONTENT: 수정 성공
     */
    UPDATE_SUCCESS(HttpStatus.NO_CONTENT, "성공적으로 수정했습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
