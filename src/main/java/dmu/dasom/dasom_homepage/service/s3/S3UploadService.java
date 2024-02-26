package dmu.dasom.dasom_homepage.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile noticeFile) throws IOException {

        // 첨부파일(이미지가 없을 경우)
        if(noticeFile == null) return "";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(noticeFile.getSize());
        metadata.setContentType(noticeFile.getContentType());

        amazonS3.putObject(bucket, fileName, noticeFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteFile(String fileName){

        // 첨부파일(이미지가 없을 경우)
        if (fileName == null || fileName.equals("")) return;

        int index = fileName.indexOf(".com/");
        String file = fileName;

        if (index != -1) {
            file = fileName.substring(index + 5);
        }
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, file);
        amazonS3.deleteObject(request);

    }

}
