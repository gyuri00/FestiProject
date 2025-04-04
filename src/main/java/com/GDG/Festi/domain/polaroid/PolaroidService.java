package com.GDG.Festi.domain.polaroid;

import com.GDG.Festi.common.FileUtil;
import com.GDG.Festi.common.S3Service;
import com.GDG.Festi.common.response.ApiResponse;
import com.GDG.Festi.common.response.resEnum.ErrorCode;
import com.GDG.Festi.common.response.resEnum.SuccessCode;
import com.GDG.Festi.domain.user.UserRepository;
import com.GDG.Festi.domain.polaroid.dto.response.DownloadResponseDTO;
import com.GDG.Festi.domain.polaroid.dto.response.SearchResponseDTO;
import com.GDG.Festi.domain.polaroid.dto.response.UpdateResponseDTO;
import com.GDG.Festi.domain.polaroid.dto.response.UploadResponseDTO;
import com.GDG.Festi.entity.Polaroid;
import com.GDG.Festi.entity.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolaroidService {
    private final UserRepository userRepository;
    private final PolaroidRepository polaroidRepository;
    private final S3Service s3Service;

    /**
     * 폴라로이드 업로드
     * @param imgFile 업로드할 이미지 파일
     * @return imgLink 업로한 이미지 URL, polaroidId 업로드한 이미지 ID
     */
    @Transactional
    public ApiResponse<?> upload(MultipartFile imgFile) throws IOException {
        if (imgFile == null || imgFile.isEmpty()) return ApiResponse.ERROR(ErrorCode.IMG_NOT_FOUND);

        // S3에 이미지 업로드
        String imgLink = s3Service.uploadFile(imgFile, FileUtil.generateFileName(imgFile));

        // TODO.사용자 정보 받아오기(수정예정)
        Users userInfo = userRepository.findById(1L)
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

    /**
     * 폴라로이드 수정
     * @param imgFile 수정할 폴라로이드 이미지
     * @param id 수정할 폴라로이드 ID
     * @return polaroidId 수정한 폴라로이드 ID, imgLink 수정한 폴라로이드 URL
     * @throws IOException
     */
    @Transactional
    public ApiResponse<?> update(MultipartFile imgFile, Long id) throws IOException {
        // 폴라로이드 정보 조회
        Polaroid polaroidInfo = polaroidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("폴라로이드 정보를 찾을 수 없습니다."));

        // 기존 이미지 삭제
        String deleteImgLink = s3Service.deleteFile(polaroidInfo.getImgLink());
        log.info("폴라로이드가 삭제되었습니다, deleteImgLink : {}", deleteImgLink);

        // 변경 이미지 S3에 업로드
        String imgLink = s3Service.uploadFile(imgFile, FileUtil.generateFileName(imgFile));

        // TODO.사용자 정보 받아오기(수정예정)
        Users userInfo = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 폴라로이드 정보 DB에 저장
        Polaroid newPolaroidInfo = polaroidRepository.save(Polaroid.builder()
                .user(userInfo)
                .polaroidId(id)
                .imgLink(imgLink)
                .build());

        log.info("폴라로이드 수정 완료, imgLink : {}, polaroidId : {}", imgLink, newPolaroidInfo.getPolaroidId());

        // DTO 변경
        UpdateResponseDTO updateResponseDTO = UpdateResponseDTO.builder()
                .imgLink(imgLink)
                .polaroidId(newPolaroidInfo.getPolaroidId())
                .userId(userInfo.getUserId().toString())
                .build();

        return ApiResponse.SUCCESS(SuccessCode.SUCCESS_UPDATE, updateResponseDTO);
    }

    /**
     * 폴라로이드 삭제
     * @param id 삭제할 폴라로이드 ID
     * @return MSG
     */
    public ApiResponse<?> delete(Long id) {
        // 폴라로이드 정보 조회
        Polaroid polaroidInfo = polaroidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("폴라로이드 정보를 찾을 수 없습니다."));

        // 기존 이미지 삭제
        String deleteImgLink = s3Service.deleteFile(polaroidInfo.getImgLink());
        polaroidRepository.deleteById(polaroidInfo.getPolaroidId());

        log.info("폴라로이드가 삭제되었습니다, imgLink : {}, polaroidId : {}", deleteImgLink, polaroidInfo.getPolaroidId());

        return ApiResponse.SUCCESS(SuccessCode.SUCCESS_DELETE);
    }

    /**
     * 폴라로이드 전체 조회
     * @return polaroidId 폴라로이드 Id, imgLink 폴라로이드 URL, userId 사용자 ID List<> 리턴
     */
    public ApiResponse<?> search() {
        // 최대 20개 폴라로이드 정보 랜덤 조회
        List<Polaroid> randomPolaroids = polaroidRepository.findRandomPolaroids();
        List<SearchResponseDTO> polaroidList = new ArrayList<>();

        // DTO 변경
        if (randomPolaroids.isEmpty()) {
            return ApiResponse.SUCCESS(SuccessCode.NO_CONTENT);
        } else {
            for (Polaroid polaroid : randomPolaroids) {
                polaroidList.add(new SearchResponseDTO(
                        polaroid.getPolaroidId(),
                        polaroid.getUser().getUserId().toString(),
                        polaroid.getImgLink()
                ));
            }
            return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, polaroidList);
        }
    }

    /**
     * 폴라로이드 개인 조회
     * @return polaroidId 폴라로이드 Id, imgLink 폴라로이드 URL, userId 사용자 ID List<> 리턴
     */
    public ApiResponse<?> searchMy() {
        // TODO.사용자 정보 받아오기(수정예정)
        Users userInfo = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자 정보 기준으로 폴라로이드 정보 조회
        List<Polaroid> polaroids = polaroidRepository.findByUser(userInfo);
        List<SearchResponseDTO> polaroidList = new ArrayList<>();

        // DTO 변경
        if (polaroids.isEmpty()) {
            return ApiResponse.SUCCESS(SuccessCode.NO_CONTENT);
        } else {
            for (Polaroid polaroid : polaroids) {
                polaroidList.add(new SearchResponseDTO(
                        polaroid.getPolaroidId(),
                        polaroid.getUser().getUserId().toString(),
                        polaroid.getImgLink()
                ));
            }
            return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, polaroidList);
        }
    }
}
