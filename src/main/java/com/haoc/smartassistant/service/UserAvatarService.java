package com.haoc.smartassistant.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haoc.smartassistant.mapper.UserMapper;
import com.haoc.smartassistant.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

@Service
public class UserAvatarService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private UserMapper userMapper;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadAvatar(BigInteger userId, MultipartFile avatarFile) throws IOException {
        // Convert MultipartFile to a File object
        File file = convertMultipartFileToFile(avatarFile);

        // Create a unique name for the file in S3
        String fileName = "avatars/user_" + userId + ".png";

        // Upload the file to S3
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // Delete local temporary file
        file.delete();

        // Get the public URL of the uploaded file
        String avatarUrl = amazonS3.getUrl(bucketName, fileName).toString();

        // Update the user's avatar URL in the database
        updateUserAvatar(userId, avatarUrl);

        return avatarUrl;
    }

    private void updateUserAvatar(BigInteger userId, String avatarUrl) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
        if (user != null) {
            user.setUserAvatar(avatarUrl);
            userMapper.updateById(user);
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
