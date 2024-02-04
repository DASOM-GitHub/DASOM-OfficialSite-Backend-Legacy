package dmu.dasom.dasom_homepage.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile noticeFile) throws IOException {
        String originalFileName = noticeFile.getOriginalFilename();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        Date now = new Date();

        String time = sdf.format(now);

        String fileName = time + "_" + originalFileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(noticeFile.getSize());
        metadata.setContentType(noticeFile.getContentType());

        amazonS3.putObject(bucket, fileName, noticeFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
