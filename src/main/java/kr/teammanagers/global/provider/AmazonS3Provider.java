package kr.teammanagers.global.provider;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.teammanagers.global.config.AmazonConfig;
import kr.teammanagers.global.constant.AmazonConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Provider {

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    // 일반 이미지 업로드
    public void uploadImage(final String filePath, final Long id, final MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType()); // 다운로드가 아닌 브라우저로 직접 조회를 하기 위함
        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), filePath + "/" + id, file.getInputStream(), metadata));
        } catch (IOException e) {
            log.error(AmazonConstant.FILE_UPLOAD_ERROR + ": {}", (Object) e.getStackTrace());
        }
    }

    public String generateUrl(final String filePath, final Long id) {

        // 버킷 내에서 파일이 존재하는지 확인
        String fullPath = filePath + "/" + id;
        boolean doesObjectExist = amazonS3.doesObjectExist(amazonConfig.getBucket(), fullPath);

        if (!doesObjectExist) {
            // 파일이 존재하지 않으면 null 또는 예외를 반환
            return null;
        }

        // 서명된 URL의 유효 기간 설정
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 60);

        // 서명된 URL 생성 요청
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(amazonConfig.getBucket(), filePath + "/" + id)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        // Content-Disposition을 inline으로 설정하여 다운로드가 아닌 브라우저에서 직접 보이도록 설정
        generatePresignedUrlRequest.addRequestParameter("response-content-disposition", "inline");

        // 서명된 URL 생성
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public boolean isFileExist(final String filePath, final Long id) {
        return amazonS3.doesObjectExist(amazonConfig.getBucket(), filePath + "/" + id);
    }

    public void deleteFile(final String filePath, final Long id) {
        amazonS3.deleteObject(amazonConfig.getBucket(), filePath + "/" + id);
    }
}
