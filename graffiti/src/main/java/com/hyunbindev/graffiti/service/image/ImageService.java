package com.hyunbindev.graffiti.service.image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hyunbindev.graffiti.config.MinioConfig;
import com.hyunbindev.graffiti.config.RabbitWebpConvertQueueConfig;
import com.hyunbindev.graffiti.exception.CommonAPIException;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
	private final MinioClient minioClient;
	private final RabbitTemplate rabbitTemplate;
	/**
	 * 이미지 저장 로직
	 * @param feedType
	 * @param multipartFile
	 * @return image name
	 */
	public String saveImage(String imageName, MultipartFile multipartFile){
		try (InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes())) {
			minioClient.putObject(PutObjectArgs.builder()
			        .bucket(MinioConfig.BUCKET_NAME)
			        .object(imageName)
			        .stream(inputStream, multipartFile.getSize(), -1)
			        .contentType("image/webp")
			        .build());
			
			//메세지 발행
			sendWebpConvertMessage(imageName);
		} catch (Exception e) {
			//저장 실패 처리
			log.error("이미지 저장 실패 : {}",e);
			throw new CommonAPIException("이미지 저장 실패",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return imageName;
	}
	/**
	 * preSigned Url 발급
	 * @param objectName
	 * @return
	 */
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
		    throw new CommonAPIException("이미지 조회 실패",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * webp convert 메세지 발행
	 * @param feedType
	 * @param imageName
	 */
	private void sendWebpConvertMessage(String imageName) {
		Map<String, String> message = new HashMap<>();
		message.put("imageName", imageName);
		rabbitTemplate.convertAndSend(RabbitWebpConvertQueueConfig.Config.EXHANGE.getValue(), RabbitWebpConvertQueueConfig.Config.ROUTING.getValue(),message);
	}
}
