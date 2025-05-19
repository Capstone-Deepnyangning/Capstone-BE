package com.deepnyangning.capstonebe.domain.face.service;

import com.deepnyangning.capstonebe.domain.face.entity.FaceData;
import com.deepnyangning.capstonebe.domain.face.repository.FaceDataRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.external.ai.AiServerClient;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FaceDataService {
    private final AiServerClient aiServerClient;
    private final FaceDataRepository faceDataRepository;
    private final UserService userService;
    private final FileValidator fileValidator;

    @Transactional
    public void registerFace(MultipartFile file, String identifier){
        fileValidator.validateVideo(file);

        User user = userService.findByIdentifier(identifier);

        String aiServerResponse = aiServerClient.sendFaceDataToAiServer(file, identifier);

        FaceData faceData = FaceData.builder()
                .user(user)
                .status(aiServerResponse)
                .build();
        faceDataRepository.save(faceData);
        if("FAIL".equals(aiServerResponse)){
            throw new CustomException(ErrorCode.FACE_REGISTRATION_FAILED);
        }
        else{
            user.setFaceRegistered(true);
        }
    }
}
