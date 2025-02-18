package com.GDG.Festi.domain.polaroid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/polaroid")
public class PolaroidController {
    // 폴라로이드 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> upload() {
        return ResponseEntity.ok().body(null);
    }
}
