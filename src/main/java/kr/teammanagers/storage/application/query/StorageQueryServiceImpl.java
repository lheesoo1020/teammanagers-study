package kr.teammanagers.storage.application.query;

import kr.teammanagers.global.exception.GeneralException;
import kr.teammanagers.global.provider.AmazonS3ProviderV2;
import kr.teammanagers.global.util.AmazonS3Helper;
import kr.teammanagers.member.domain.Member;
import kr.teammanagers.storage.application.module.StorageModuleService;
import kr.teammanagers.storage.domain.TeamData;
import kr.teammanagers.storage.dto.StorageDto;
import kr.teammanagers.team.application.module.TeamModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static kr.teammanagers.common.payload.code.status.ErrorStatus.TEAM_MANAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StorageQueryServiceImpl implements StorageQueryService {

    private final AmazonS3ProviderV2 s3Provider;
    private final AmazonS3Helper s3Helper;

    private final TeamModuleService teamModuleService;
    private final StorageModuleService storageModuleService;

    //파일 목록 get
    @Override
    public List<StorageDto> getFiles(Long teamId, Member member) {
        if (!teamModuleService.existsTeamManageByMemberIdAndTeamId(member.getId(), teamId)) {
            throw new GeneralException(TEAM_MANAGE_NOT_FOUND);
        }
        List<TeamData> teamDataList = teamModuleService.findTeamManageAllByTeamId(teamId).stream()
                .map(teamManage -> storageModuleService.findAllByTeamManageId(teamManage.getId()))
                .flatMap(List::stream)
                .toList();

        return teamDataList.stream()
                .map(StorageDto::from)
                .collect(Collectors.toList());
    }

    //파일 다운로드
    @Override
    public StorageDto downloadFile(Long teamId, Long storageId, Member member) {

        TeamData teamData = storageModuleService.findById(storageId);
        if (!teamModuleService.existsTeamManageByMemberIdAndTeamId(member.getId(), teamId)) {
            throw new GeneralException(TEAM_MANAGE_NOT_FOUND);
        }

        if (!teamData.getTeamManage().getTeam().getId().equals(teamId)) {
            throw new IllegalArgumentException("File does not belong to the specified team");
        }

        String fileUrl = teamData.getFileUrl();

        String key = s3Helper.extractKeyFromUrl(fileUrl);

        InputStream inputStream = s3Provider.downloadFile(key);

        // 전체 파일 이름 (이름 + 확장자)
        String decodedFileName = URLDecoder.decode(teamData.getTitle(), StandardCharsets.UTF_8);
        String contentType = s3Provider.determineContentType(decodedFileName);

        return StorageDto.builder()
                .inputStream(inputStream)
                .title(decodedFileName)
                .contentType(contentType)
                .build();
    }
}
