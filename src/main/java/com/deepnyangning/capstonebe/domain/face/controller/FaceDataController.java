package com.deepnyangning.capstonebe.domain.face.controller;

import com.deepnyangning.capstonebe.domain.face.service.FaceDataService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faces")
@Tag(name = "얼굴 등록 API")
public class FaceDataController implements FaceDataControllerDocs {
    private final FaceDataService faceDataService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> registerFace(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestParam("file") MultipartFile file){
        String identifier = userDetails.getUsername();
        faceDataService.registerFace(file, identifier);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder().success(true).code(201).message("얼굴 등록에 성공했습니다.").build());
    }
}
