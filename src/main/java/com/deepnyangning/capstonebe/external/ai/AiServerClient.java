package com.deepnyangning.capstonebe.external.ai;

import com.deepnyangning.capstonebe.external.ai.dto.AiResponse;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiServerClient {
    @Value("${ai.server-url}")
    private String AI_SERVER_URL;
    private final RestTemplate restTemplate;


    public String sendFaceDataToAiServer(MultipartFile file, String identifier){
        String url = AI_SERVER_URL + "/register_face";
        try {
            if (file == null || file.isEmpty()) {
                log.error("파일이 없거나 비어 있음");
                throw new CustomException(ErrorCode.INVALID_VIDEO_FILE);
            }

            log.info("파일 전송: URL={}, 이름={}, 크기={}, identifier={}", url, file.getOriginalFilename(), file.getSize(), identifier);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            File tempFile = File.createTempFile("upload", file.getOriginalFilename());
            file.transferTo(tempFile);
            body.add("file", new FileSystemResource(tempFile));
            body.add("identifier", identifier);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<AiResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, AiResponse.class);

            tempFile.delete();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().isSuccess()) {
                return "SUCCESS";
            } else {
                log.warn("AI 서버 응답 실패: 상태={}, 본문={}", response.getStatusCode(), response.getBody());
                return "FAIL";
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.error("AI 서버 엔드포인트 없음 (404): URL={}", url, e);
            throw new CustomException(ErrorCode.AI_SERVER_ENDPOINT_NOT_FOUND);
        } catch (ResourceAccessException e) {
            log.error("AI 서버 연결 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AI_SERVER_UNREACHABLE);
        } catch (Exception e) {
            log.error("AI 서버 호출 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AI_SERVER_COMMUNICATION_FAILED);
        }
    }

    public void sendDeleteFaceToAiServer(String identifier, boolean removeFolder){
        String url = AI_SERVER_URL + "/delete_face";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("identifier", identifier);
            body.add("remove_folder", String.valueOf(removeFolder)); // true 또는 false 문자열로 전달

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("AI 서버 얼굴 삭제 요청 성공: {}", response.getBody());
            } else {
                log.warn("AI 서버 얼굴 삭제 실패: 상태={}, 응답={}", response.getStatusCode(), response.getBody());
            }

        } catch (HttpClientErrorException.NotFound e) {
            log.error("AI 서버 엔드포인트 없음 (404): URL={}", url, e);
            throw new CustomException(ErrorCode.AI_SERVER_ENDPOINT_NOT_FOUND);
        } catch (ResourceAccessException e) {
            log.error("AI 서버 연결 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AI_SERVER_UNREACHABLE);
        } catch (Exception e) {
            log.error("AI 서버 호출 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AI_SERVER_COMMUNICATION_FAILED);
        }
    }
}
