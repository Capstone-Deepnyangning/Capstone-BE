package com.deepnyangning.capstonebe.external.ai;

import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AiServerClient {
    private static final String AI_SERVER_URL = "https://1ac8-116-44-51-91.ngrok-free.app/register_face";

    public String sendFaceDataToAiServer(MultipartFile file, String identifier){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename(); // null 방지
                }
            });
            body.add("identifier", identifier);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(AI_SERVER_URL, HttpMethod.POST, request, String.class);

            return response.getStatusCode().is2xxSuccessful() ? "SUCCESS" : "FAIL";
        } catch (Exception e){
            throw new CustomException(ErrorCode.AI_SERVER_COMMUNICATION_FAILED);
        }
    }
}
