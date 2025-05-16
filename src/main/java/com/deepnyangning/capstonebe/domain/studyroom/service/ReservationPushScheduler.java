package com.deepnyangning.capstonebe.domain.studyroom.service;

import com.deepnyangning.capstonebe.domain.notification.entity.PushType;
import com.deepnyangning.capstonebe.domain.notification.service.NotificationService;
import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomReservation;
import com.deepnyangning.capstonebe.domain.studyroom.repository.StudyRoomReservationRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

        for(StudyRoomReservation reservation : reservations){
            User user = reservation.getUser();
            notificationService.sendPush(user, PushType.STUDY_ROOM_REMINDER);
        }
    }
}
