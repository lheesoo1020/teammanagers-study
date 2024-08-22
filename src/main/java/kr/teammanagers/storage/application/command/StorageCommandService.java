package kr.teammanagers.storage.application.command;

import kr.teammanagers.member.domain.Member;
import kr.teammanagers.storage.dto.request.CreateStorageRequest;
import kr.teammanagers.storage.dto.response.StorageResponse;

public interface StorageCommandService {
    StorageResponse uploadFile(CreateStorageRequest request, Member member);
    void deleteFile(Long teamId, Long storageId, Member member);
}
