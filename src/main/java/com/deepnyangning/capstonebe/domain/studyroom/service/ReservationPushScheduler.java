package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.notification.entity.PushType;
import com.deepnyangning.capstonebe.domain.notification.service.NotificationService;
import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationPushScheduler {
    private final StudyRoomReservationRepository reservationRepository;
    private final NotificationService notificationService;

    // 월~금 9:30~20:30 1시간 마다 실행
    @Scheduled(cron = "0 30 9-20 * * MON-FRI")
    @Transactional(readOnly = true)
    public void sendPushMessageWeekday(){
        sendPushMessage(LocalTime.now().plusMinutes(30));
    }

    // 토요일 9:30~15:30 1시간 마다 실행
    @Scheduled(cron = "0 30 9-15 * * SAT")
    @Transactional(readOnly = true)
    public void sendPushMessageSaturday(){
        sendPushMessage(LocalTime.now().plusMinutes(30));
    }

    private void sendPushMessage(LocalTime startTime){
        LocalDate date = LocalDate.now();

        List<StudyRoomReservation> reservations = reservationRepository.findByDateAndStartTimeAndStatus(date, startTime, ReservationStatus.CONFIRMED);

        int successCnt = 0;
        int failCnt = 0;

        for(StudyRoomReservation reservation : reservations){
            User user = reservation.getUser();
            try{
                notificationService.sendPush(user, PushType.STUDY_ROOM_REMINDER);
                successCnt++;
                log.debug("스터디룸 리마인드 푸시 전송 성공: ientifier={}, reservationId={}", user.getIdentifier(), reservation.getId());
            } catch (Exception e){
                failCnt++;
                log.debug("스터디룸 리마인드 푸시 전송 실패: identifier={}, reservationId={}, error={}", user.getIdentifier(), reservation.getId(), e.toString());
            }
        }

        log.info("스터디룸 리마인드 알림 전송 완료 - 날짜: {}, 시작 시간: {}, 총 대상: {}건, 성공: {}건, 실패: {}건",
                date, startTime, reservations.size(), successCnt, failCnt);
    }
}
