package com.hyunbindev.graffiti.config;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
/**
 * Minio Client Bean 등록
 * @author hyunbinDev
 */
@Configuration
public class MinioConfig {
	@Value("${minio.url}")
	private String url;
	@Value("${minio.access-key}")
	private String accessKey;
	@Value("${minio.secret-key}")
	private String secretKey;
	//버켓 이름
	public static final String BUCKET_NAME ="graffiti";
	
	@Bean
	public MinioClient minioClient() throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException, IOException {
		MinioClient client =  MinioClient.builder()
				.endpoint(url)
				.credentials(accessKey, secretKey)
				.build();
		//bucket 초기화
		boolean isExistBucket = client.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
		//버켓 생성
		if(!isExistBucket) {
			client.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
		}
		return client;
	}
}