package kr.teammanagers.storage.application;


import kr.teammanagers.global.provider.AmazonS3ProviderV2;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.member.repository.MemberRepository;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.common.EntityStatus;
import kr.teammanagers.storage.dto.request.CreateStorageRequest;
import kr.teammanagers.storage.dto.response.StorageResponse;
import kr.teammanagers.storage.repository.TeamDataRepository;
import kr.teammanagers.team.domain.TeamManage;
import kr.teammanagers.team.repository.TeamManageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StorageCommandServiceImpl implements StorageCommandService {

    private final AmazonS3ProviderV2 s3Provider;
    private final TeamDataRepository teamDataRepository;
    private final TeamManageRepository teamManageRepository;
    private final MemberRepository memberRepository;

    @Override
    public StorageResponse uploadFile(CreateStorageRequest request, Member member) {

        Long teamId = request.getTeamId();
        MultipartFile file = request.getFile();

        // 파일 이름과 확장자 추출
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);


        // 팀 관리 Entity 가져오기
        TeamManage teamManage = teamManageRepository.findByMemberIdAndTeamId(member.getId(), teamId)
                .orElseThrow(() -> new RuntimeException("TeamManage not found"));

        // s3 파일 업로드
        String fileUrl = s3Provider.uploadFile(teamManage.getTeam().getId().toString(), file);

        TeamData teamData = TeamData.builder()
                .title(originalFileName)
                .size(String.valueOf(file.getSize()))
                .fileUrl(fileUrl)
                .teamManage(teamManage)
                .uploadAt(LocalDateTime.now())
                .uploader(member.getName())
                .fileExtension(fileExtension)
                .build();

        teamDataRepository.save(teamData);

        return StorageResponse.builder()
                .message("파일 업로드 성공")
                .success(true)
                .build();
    }


    @Override
    public void deleteFile(Long teamId, Long storageId, Member member) {
        TeamData teamData = teamDataRepository.findById(storageId)
                .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));

        TeamManage teamManage = teamManageRepository.findByMemberIdAndTeamId(member.getId(), teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀에 유저가 속해있지 않습니다."));

        if (!teamData.getTeamManage().getTeam().getId().equals(teamId)) {
            throw new IllegalArgumentException("File does not belong to the specified team");
        }

        teamData.updateStatus(EntityStatus.DELETE);
        teamDataRepository.save(teamData);
    }
}
