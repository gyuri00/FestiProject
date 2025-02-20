package com.GDG.Festi.domain.polaroid;

import com.GDG.Festi.common.FileUtil;
import com.GDG.Festi.common.response.ApiResponse;
import com.GDG.Festi.common.response.resEnum.ErrorCode;
import com.GDG.Festi.common.response.resEnum.SuccessCode;
import com.GDG.Festi.domain.UserRepository;
import com.GDG.Festi.domain.polaroid.dto.response.DownloadResponseDTO;
import com.GDG.Festi.domain.polaroid.dto.response.UploadResponseDTO;
import com.GDG.Festi.entity.Polaroid;
import com.GDG.Festi.entity.User;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolaroidService {
    private final UserRepository userRepository;
    private final PolaroidRepository polaroidRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 폴라로이드 업로드
     * @param imgFile 업로드할 이미지 파일
     * @return imgLink 업로한 이미지 URL, polaroidId 업로드한 이미지 ID
     */
    public ApiResponse<?> upload(MultipartFile imgFile) {
        if (imgFile == null || imgFile.isEmpty()) return ApiResponse.ERROR(ErrorCode.IMG_NOT_FOUND);

        // 파일명 생성
        String filename = FileUtil.generateFileName(imgFile);

        // 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imgFile.getSize());
        objectMetadata.setContentType(imgFile.getContentType());

        try {
            // S3에 업로드
            InputStream inputStream = imgFile.getInputStream();
            amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata));

            // 객체 URL 받아오기
            String imgLink = amazonS3.getUrl(bucket, filename).toString();

            // 사용자 정보 받아오기
            User userInfo = userRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 폴라로이드 정보 DB에 저장
            Polaroid polaroidInfo = Polaroid.builder()
                    .user(userInfo)
                    .imgLink(imgLink)
                    .build();
            Polaroid newPolaroidInfo = polaroidRepository.save(polaroidInfo);

            log.info("폴라로이드 업로드 완료, imgLink : {}, polaroidId : {}", imgLink, newPolaroidInfo.getPolaroidId());

            // DTO 변경
            UploadResponseDTO uploadResponseDTO = UploadResponseDTO.builder()
                    .imgLink(imgLink)
                    .polaroidId(newPolaroidInfo.getPolaroidId())
                    .build();

            return ApiResponse.SUCCESS(SuccessCode.SUCCESS_UPLOAD, uploadResponseDTO);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드 중 에러 발생하였습니다.");
        }
    }

    /**
     * 폴라로이드 다운로드
     * @param id 다운로드할 폴라로이드 ID
     * @return polaroidId 업로드한 이미지 ID, userId 폴라로이드 업로드한 사용자 ID, imgLink 업로한 이미지 URL
     */
    public ApiResponse<?> download(Long id) {
        // 폴라로이드 정보 조회
        Polaroid polaroidInfo = polaroidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("폴라로이드 정보를 찾을 수 없습니다."));

        // DTO 변경
        DownloadResponseDTO downloadResponseDTO = DownloadResponseDTO.builder()
                .polaroidId(polaroidInfo.getPolaroidId())
                .userId(polaroidInfo.getUser().getUserId().toString())
                .imgLink(polaroidInfo.getImgLink())
                .build();

        return ApiResponse.SUCCESS(SuccessCode.SUCCESS_DOWNLOAD, downloadResponseDTO);
    }
}
