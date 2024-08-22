package kr.teammanagers.alarm.dto;

import kr.teammanagers.alarm.domain.Alarm;
import kr.teammanagers.alarm.domain.AlarmType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AlarmDto(
        Long alarmId,
        AlarmType alarmType,
        Long referenceId,
        String content,
        LocalDateTime date,
        Boolean isRead
) {
    public static AlarmDto from(Alarm alarm) {
        return AlarmDto.builder()
                .alarmId(alarm.getId())
                .alarmType(alarm.getType())
                .referenceId(alarm.getReferenceId())
                .content(alarm.getContent())
                .date(alarm.getDate())
                .isRead(alarm.getIsRead())
                .build();
    }

}
