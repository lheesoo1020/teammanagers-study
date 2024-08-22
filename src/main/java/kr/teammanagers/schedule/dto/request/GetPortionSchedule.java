package kr.teammanagers.schedule.dto.request;

import java.util.List;

public record GetPortionSchedule(
        List<Long> teamManageList
) {
}
