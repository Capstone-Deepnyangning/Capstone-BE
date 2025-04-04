package com.deepnyangning.capstonebe.domain.face.service;

import com.deepnyangning.capstonebe.domain.face.repository.FaceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaceDataService {
    private final FaceDataRepository faceDataRepository;
}
