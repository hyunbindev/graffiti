package com.hyunbindev.graffiti.service.image;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hyunbindev.graffiti.config.MinioConfig;
import com.hyunbindev.graffiti.config.rabbitMQ.RabbitWebpConvertQueueConfig;
import com.hyunbindev.graffiti.exception.CommonAPIException;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
	private final MinioClient minioClient;
	private final RabbitTemplate rabbitTemplate;
	final int BUFFER_SIZE = 8192;
	/**
	 * 이미지 저장 로직
	 * @param feedType
	 * @param multipartFile
	 * @return image name
	 */
	public String saveImage(String imageName, MultipartFile multipartFile){
		try (InputStream inputStream = multipartFile.getInputStream();
			 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream,BUFFER_SIZE)
				) {
			minioClient.putObject(PutObjectArgs.builder()
			        .bucket(MinioConfig.BUCKET_NAME)
			        .object(imageName)
			        .stream(bufferedInputStream, multipartFile.getSize(), -1)
			        .contentType(multipartFile.getContentType())
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
		            .method(Method.GET)
		            .bucket(MinioConfig.BUCKET_NAME)
		            .object(objectName)
		            .expiry(60*60)
		            .build()
		    );
		} catch (Exception e) {
		    e.printStackTrace();
		    throw new CommonAPIException("이미지 조회 실패",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * 이미지 삭제
	 * @param imageName
	 */
	public void deleteImage(String imageName) {
        try {
			minioClient.removeObject(
			        RemoveObjectArgs.builder()
			            .bucket(MinioConfig.BUCKET_NAME)
			            .object(imageName)
			            .build()
			    );
		} catch (Exception e) {
			log.error("fail to delete image : {}",imageName);
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
