package kr.teammanagers.storage.application.command;


import kr.teammanagers.common.EntityStatus;
import kr.teammanagers.common.payload.code.status.ErrorStatus;
import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.global.provider.AmazonS3ProviderV2;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.storage.application.module.StorageModuleService;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.storage.dto.request.CreateStorageRequest;
import kr.teammanagers.storage.dto.response.StorageResponse;
import kr.teammanagers.team.application.module.TeamModuleService;
import kr.teammanagers.team.domain.TeamManage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StorageCommandServiceImpl implements StorageCommandService {

    private final AmazonS3ProviderV2 s3Provider;
    private final StorageModuleService storageModuleService;
    private final TeamModuleService teamModuleService;

    @Override
    public StorageResponse uploadFile(CreateStorageRequest request, Member member) {

        Long teamId = request.getTeamId();
        MultipartFile file = request.getFile();

        // 파일 이름과 확장자 추출
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        // 팀 관리 Entity 가져오기
        TeamManage teamManage = teamModuleService.findTeamManageByMemberIdAndTeamId(member.getId(), teamId);

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

        storageModuleService.save(teamData);

        return StorageResponse.builder()
                .message("파일 업로드 성공")
                .success(true)
                .build();
    }

    @Override
    public void deleteFile(Long teamId, Long storageId, Member member) {
        TeamData teamData = storageModuleService.findById(storageId);
        if(!teamModuleService.existsTeamManageByMemberIdAndTeamId(member.getId(), teamId)){
            throw new GeneralException(ErrorStatus.TEAM_MANAGE_NOT_FOUND);
        }

        if (!teamData.getTeamManage().getTeam().getId().equals(teamId)) {
            throw new IllegalArgumentException("File does not belong to the specified team");
        }
        teamData.updateStatus(EntityStatus.DELETE);
    }
}
