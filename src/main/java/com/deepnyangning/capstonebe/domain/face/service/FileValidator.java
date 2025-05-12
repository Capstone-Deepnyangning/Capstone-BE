package com.deepnyangning.capstonebe.domain.face.service;

import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator {
    private static long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB

    public void validateVideo(MultipartFile file){
        if(file.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_VIDEO_FILE, "영상이 비어 있습니다.");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("video/")) {
            throw new CustomException(ErrorCode.INVALID_VIDEO_FILE, "유효한 영상 파일이 아닙니다.");
        }
        if (file.getSize() > MAX_VIDEO_SIZE) {
            throw new CustomException(ErrorCode.INVALID_VIDEO_FILE, "영상 파일 크기가 너무 큽니다.");
        }
    }
}
