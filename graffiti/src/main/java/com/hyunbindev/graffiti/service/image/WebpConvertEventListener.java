package com.hyunbindev.graffiti.service.image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.config.MinioConfig;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;

import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebpConvertEventListener {
	private final MinioClient minioClient;
	
	
	@RabbitListener(queues = "WEBP_CONVERT")
	public void convertRequestConsume(Map<String,String> message) {
	    long startTime = System.currentTimeMillis();
	    String imageName = message.get("imageName");
	    
	    String tempImageName = imageName + ".tmp_webp"; 
	    
	    try (InputStream inputStream = minioClient.getObject(
	            GetObjectArgs.builder()
	            .bucket(MinioConfig.BUCKET_NAME)
	            .object(imageName)
	            .build()
	    )){
	        log.debug("Processing image: {}", imageName);
	        byte[] webpBytes = convertWebp(inputStream);

	        // 1. 임시 이름으로 WebP 파일 업로드
	        minioClient.putObject(
	                PutObjectArgs.builder()
	                        .bucket(MinioConfig.BUCKET_NAME)
	                        .object(tempImageName) // 임시 이름 사용
	                        .stream(new ByteArrayInputStream(webpBytes), webpBytes.length, -1)
	                        .contentType("image/webp")
	                        .build()
	        );
	        minioClient.copyObject(
	            CopyObjectArgs.builder()
	                .bucket(MinioConfig.BUCKET_NAME)
	                .object(imageName)//덮어쓸 최종 이름 (원본 이름)
	                .source(
	                    CopySource.builder()
	                        .bucket(MinioConfig.BUCKET_NAME)
	                        .object(tempImageName)//임시 파일
	                        .build()
	                )
	                .build()
	        );
	        //임시파일 삭제
	        minioClient.removeObject(
	            RemoveObjectArgs.builder()
	                .bucket(MinioConfig.BUCKET_NAME)
	                .object(tempImageName)
	                .build()
	        );
	        log.info("success converted image : {} | processed time : {}ms", imageName, (System.currentTimeMillis() - startTime));
	    } catch (Exception e) {
	        log.error("failed to convert image {} : {}", imageName, e.getMessage(), e);
	    }
	}
	
	/**
	 * webp 변환
	 * @param image
	 * @return webp byte arr
	 */
	private byte[] convertWebp(InputStream  inputStream) {
	    try {
	        // MultipartFile → ImmutableImage
	        ImmutableImage img = ImmutableImage.loader().fromStream(inputStream);

	        WebpWriter webpWriter = WebpWriter.DEFAULT.withQ(80).withM(4).withZ(9);
	        // WebP 변환
	        return img.bytes(webpWriter);
	    } catch (IOException e) {
	        log.error("이미지 WebP 변환 실패: {}", e.getMessage());
	        throw new CommonAPIException("이미지 변환 실패", HttpStatus.BAD_REQUEST);
	    }
	}
}
