package kr.teammanagers.member.dto.request;

import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateProfile(
        String name,
        String phoneNumber,
        @Size(max = 20)
        String belong,
        List<String> confidentRole
) {
}
