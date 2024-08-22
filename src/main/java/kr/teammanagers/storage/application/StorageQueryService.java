package kr.teammanagers.storage.application;

import kr.teammanagers.member.domain.Member;
import kr.teammanagers.storage.dto.StorageDto;

import java.util.List;

public interface StorageQueryService {
    List<StorageDto> getFiles(Long teamId, Member member);
    StorageDto downloadFile(Long teamId, Long storageId, Member member);
}
