package com.GDG.Festi.domain.alarm;

import com.GDG.Festi.common.response.ApiResponse;
import com.GDG.Festi.common.response.resEnum.SuccessCode;
import com.GDG.Festi.domain.user.UserRepository;
import com.GDG.Festi.domain.alarm.dto.response.SearchResponseDTO;
import com.GDG.Festi.entity.Alarm;
import com.GDG.Festi.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public ApiResponse<?> search() {
        // TODO.사용자 정보 받아오기(수정예정)
        Users userInfo = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자 정보 기준으로 알림 정보 조회
        List<Alarm> alarms = alarmRepository.findByUser(userInfo);
        List<SearchResponseDTO> alarmList = new ArrayList<>();

        // DTO 변경
        if (alarms.isEmpty()) {
            return ApiResponse.SUCCESS(SuccessCode.NO_CONTENT);
        } else {
            for (Alarm alarm : alarms) {
                alarmList.add(new SearchResponseDTO(
                        alarm.getAlarmId(),
                        alarm.getUser().getUserId().toString(),
                        alarm.getAlarmMsg(),
                        alarm.getSendTime().toString(),
                        alarm.getAlarmType(),
                        alarm.getIsRead().toString(),
                        alarm.getLinkPath()
                ));
            }
            return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, alarmList);
        }
    }
}
