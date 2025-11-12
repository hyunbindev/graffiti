package com.hyunbindev.graffiti.service.secret;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.secret.CreateSecretDTO;
import com.hyunbindev.graffiti.data.secret.SecretDTO;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.secret.SecretEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.group.GroupRepository;
import com.hyunbindev.graffiti.repository.jpa.secret.SecretRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecretService {
	private final SecretRepository secretRepository;
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
	
	private static final String SAFE_SYMBOLS = "!@#$%^&*()_+-=[]{};:,.<>?/~`";
	/**
	 * 비밀글 생성
	 * @param userUuid
	 * @param createDto
	 */
	@Transactional
	public void createSecretFeed(String userUuid, CreateSecretDTO createDto) {
		MemberEntity author = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(createDto.getGroupUuid())
				.orElseThrow(()-> new CommonAPIException("그룹을 찾을 수 없습니다.",HttpStatus.NOT_FOUND));
		
		if(!author.isInGroup(group))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		SecretEntity secret = SecretEntity.builder()
				.author(author)
				.group(group)
				.text(createDto.getText())
				.hint(createDto.getHint())
				.answer(createDto.getAnswer())
				.build();
		
		secretRepository.save(secret);
	}
	/**
	 * 사진과 함께 게시글 작성
	 * @param userUuid
	 * @param createDto
	 */
	@Transactional
	public void createSecretFeedWithImage(String userUuid, CreateSecretDTO createDto) {
		MemberEntity author = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(createDto.getGroupUuid())
				.orElseThrow(()-> new CommonAPIException("그룹을 찾을 수 없습니다.",HttpStatus.NOT_FOUND));
		
		if(!author.isInGroup(group))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		SecretEntity secret = SecretEntity.builder()
				.author(author)
				.group(group)
				.text(createDto.getText())
				.hint(createDto.getHint())
				.answer(createDto.getAnswer())
				.build();
		//랜덤 문자로 치환
		secret.setText(randomText(secret.getText()));
		
		secretRepository.save(secret);
	}
	/**
	 * 비밀글 정답없이조회
	 * @param userUuid
	 * @param feedId
	 * @return SecretDTO
	 */
	@Transactional(readOnly=true)
	public SecretDTO getSecretFeed(String userUuid, Long feedId) {
		MemberEntity user = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		SecretEntity secret =secretRepository.findById(feedId)
				.orElseThrow(()->new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		if(!user.isInGroup(secret.getGroup()))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//랜덤문자로 치환
		secret.setText(randomText(secret.getText()));
		
		return SecretDTO.mappingDTO(secret,false, 0, 0, 0);
	}
	
	@Transactional(readOnly=true)
	public SecretDTO getSecretFeedWithAnswer(String userUuid, Long feedId, String answer) {
		MemberEntity user = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		SecretEntity secret =secretRepository.findById(feedId)
				.orElseThrow(()->new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		
		if(!user.isInGroup(secret.getGroup()))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//정답과 맞지 않다면
		if(!secret.getAnswer().equals(answer))
			return SecretDTO.mappingNotAnswerDTO(secret,0,0,0);
		//정답이라면
		return SecretDTO.mappingDTO(secret,false, 0, 0, 0);
	}
	
	@Transactional
	public void deleteSecretFeed(String userUuid, Long feedId) {
		
	}
	
	public static String randomText(String text) {
		StringBuilder textBuilder = new StringBuilder();
		if(text.length() ==0) return "empty";
		Random random = new Random();

        text.chars().forEach(c -> {
            char ch = (char) c;

            if (Character.isWhitespace(ch)) {
                textBuilder.append(ch);
            } else {
                int r = random.nextInt(3);
                char randomChar;
                switch (r) {
                    case 0 : //한글 범위 (U+AC00 ~ U+D7A3)
                            randomChar = (char) (0xAC00 + random.nextInt(0xD7A3 - 0xAC00 + 1));
                            break;
                    case 1 :
                            randomChar = (char) ('A' + random.nextInt(26));
                            break;
                    default :
                            randomChar = (char) ('a' + random.nextInt(26));
                }
                textBuilder.append(randomChar);
            }
        });
        return textBuilder.toString();
	}
}