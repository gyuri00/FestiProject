package com.GDG.Festi.domain.polaroid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/polaroid")
public class PolaroidController {
    private final PolaroidService polaroidService;

    // 폴라로이드 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile imgFile) throws IOException {
        return ResponseEntity.ok().body(polaroidService.upload(imgFile));
    }

    // 폴라로이드 다운로드
    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(polaroidService.download(id));
    }

    // 폴라로이드 수정
    @PostMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam("file") MultipartFile imgFile,
            @RequestParam("polaroidId") Long id) throws IOException {
        return ResponseEntity.ok().body(polaroidService.update(imgFile, id));
    }
}
