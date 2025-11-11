package com.hyunbindev.graffiti.service.image;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hyunbindev.graffiti.config.MinioConfig;
import com.hyunbindev.graffiti.constant.feed.FeedType;
import com.hyunbindev.graffiti.exception.CommonAPIException;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
	private final MinioClient minioClient;
	
	/**
	 * 이미지 저장 로직
	 * @param feedType
	 * @param multipartFile
	 * @return image name
	 */
	public String saveImage(FeedType feedType, MultipartFile multipartFile){
		String imageName = feedType.getFeedType()+"-"+UUID.randomUUID();
		
        try {
			minioClient.putObject(PutObjectArgs.builder()
			        .bucket(MinioConfig.BUCKET_NAME)
			        .object(imageName)
			        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
			        .contentType(multipartFile.getContentType())
			        .build());
		} catch (Exception e) {
			//저장 실패 처리
			log.error("이미지 저장 실패 : {}",e.getMessage());
			throw new CommonAPIException("이미지 저장 실패",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return imageName;
	}
	public String getPresignedUrl(String objectName) {
		try {
		    return minioClient.getPresignedObjectUrl(
		        GetPresignedObjectUrlArgs.builder()
		            .method(Method.GET)          // GET으로 다운로드
		            .bucket(MinioConfig.BUCKET_NAME)
		            .object(objectName)
		            .expiry(60)                  // 유효기간 60초
		            .build()
		    );
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;  // 실패 시 null 반환
		}
	}
}
