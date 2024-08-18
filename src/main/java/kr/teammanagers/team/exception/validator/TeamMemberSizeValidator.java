package kr.teammanagers.team.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.teammanagers.team.application.query.TeamQueryService;
import kr.teammanagers.team.dto.RegisterCommentDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TeamMemberSizeValidator implements ConstraintValidator<TeamMemberSize, List<RegisterCommentDto>> {

    private final TeamQueryService teamQueryService;

    @Override
    public void initialize(TeamMemberSize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<RegisterCommentDto> value, ConstraintValidatorContext constraintValidatorContext) {
        return teamQueryService.countTeamMembersByTeamManageId(value.getFirst().teamManageId()) - 1 == value.size();
    }
}
