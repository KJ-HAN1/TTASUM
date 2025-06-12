package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.heavenLetter.Memorial;
import com.ttasum.memorial.domain.repository.heavenLetter.MemorialRepository;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterUpdateRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.CommonResultResponseDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponseDto;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterRepository;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterUpdateResponsDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
//final 필드에 대해 생성자 주입 자동 생성
@RequiredArgsConstructor
public class HeavenLetterServiceImpl implements HeavenLetterService {

    private final HeavenLetterRepository heavenLetterRepository;
    private final MemorialRepository memorialRepository;

    //등록
    @Transactional
    @Override
    public HeavenLetterResponseDto createLetter(HeavenLetterRequestDto heavenLetterRequestDto) {

        Memorial memorial = null;
        if (heavenLetterRequestDto.getDonateSeq() != null) {
            memorial = memorialRepository.findById(heavenLetterRequestDto.getDonateSeq())
                    .orElseThrow(() -> new IllegalArgumentException("해당 기증자 정보가 존재하지 않습니다"));
        }

        HeavenLetter heavenLetter = HeavenLetter.builder()
                .donateSeq(memorial)
                .areaCode(heavenLetterRequestDto.getAreaCode())
                .letterTitle(heavenLetterRequestDto.getLetterTitle())
                .donorName(heavenLetterRequestDto.getDonorName())
                .letterPasscode(heavenLetterRequestDto.getLetterPasscode())
                .letterWriter(heavenLetterRequestDto.getLetterWriter())
                .anonymityFlag(heavenLetterRequestDto.getAnonymityFlag())
                .letterContents(heavenLetterRequestDto.getLetterContents())
                .fileName(heavenLetterRequestDto.getFileName())
                .orgFileName(heavenLetterRequestDto.getOrgFileName())
                .writerId(heavenLetterRequestDto.getWriterId())
                .build();

        heavenLetterRepository.save(heavenLetter);

        return HeavenLetterResponseDto.success();
    }

    //조회 - 단건
    @Transactional
    @Override
    public HeavenLetterResponseDto.HeavenLetterDetailResponse getLetterById(Integer letterSeq) {

        HeavenLetter heavenLetter = heavenLetterRepository.findByLetterSeqAndDelFlag(letterSeq,"N").get();

        //커맨드 메서드 사용
        heavenLetter.increaseReadCount();

        //엔티티 -> DTO
        return new HeavenLetterResponseDto.HeavenLetterDetailResponse(heavenLetter);
    }

    //페이징처리
    @Transactional
    @Override
    public Page<HeavenLetterResponseDto.HeavenLetterListResponse> getAllLetters(Pageable pageable) {
        return heavenLetterRepository.findAllByDelFlag("N", pageable)
                .map(HeavenLetterResponseDto.HeavenLetterListResponse::fromEntity);
    }

    //수정 인증 (공통)
    @Transactional(readOnly = true)
    @Override
    public boolean verifyPasscode(Integer letterSeq, String passcode) {
        HeavenLetter heavenLetter = heavenLetterRepository.findById(letterSeq).orElseThrow();
        return heavenLetter.getLetterPasscode().equals(passcode);
    }

    //수정
    @Transactional
    @Override
    public HeavenLetterUpdateResponsDto updateLetter(HeavenLetterUpdateRequestDto heavenLetterUpdateRequestDto) {
        HeavenLetter heavenLetterUpdate = heavenLetterRepository.findById(heavenLetterUpdateRequestDto.getLetterSeq()).get();

        //기증자 외래키 변경(연결만 바꿔줌)
        Memorial memorial = memorialRepository.findById(heavenLetterUpdateRequestDto.getDonateSeq()).get();

        //내용수정
        heavenLetterUpdate.updateLetterContents(heavenLetterUpdateRequestDto, memorial);
        //응답 반환
        return HeavenLetterUpdateResponsDto.success();

    }
    //삭제
    @Transactional
    @Override
    public CommonResultResponseDto deleteLetter(HeavenLetterVerifyRequestDto deleteRequest){

        //편지 조회
        //비밀번호 인증
        if (!this.verifyPasscode(deleteRequest.getLetterSeq(), deleteRequest.getLetterPasscode())) {
            return CommonResultResponseDto.fail("비밀번호가 일치하지 않습니다.");
        }
        HeavenLetter heavenLetter = heavenLetterRepository.findById(deleteRequest.getLetterSeq()).get();

        //소프트 삭제 커멘드 사용
        heavenLetter.softDelete();

        return CommonResultResponseDto.success("편지가 정상적으로 삭제 되었습니다.");
    }
    //검색
    @Transactional(readOnly = true)
    @Override
    public Page<HeavenLetterResponseDto.HeavenLetterListResponse> searchLetters(String type, String keyword, Pageable pageable) {
        Logger log = LoggerFactory.getLogger(HeavenLetterServiceImpl.class);

        log.info("[HeavenLetter 검색] type={}, keyword={}, page={}, size={}",
                type, keyword, pageable.getPageNumber(), pageable.getPageSize());

        Specification<HeavenLetter> spec = notDeleted();

        if ("title".equalsIgnoreCase(type)) {
            spec = spec.and(titleContains(keyword));
        } else if ("contents".equalsIgnoreCase(type)) {
            spec = spec.and(contentsContains(keyword));
        } else if ("all".equalsIgnoreCase(type)) {
            spec = spec.and(titleOrContentsContains(keyword));
        }

        Page<HeavenLetter> result = heavenLetterRepository.findAll(spec, pageable);
        log.info("[검색 결과] 총 건수: {}", result.getTotalElements());

        return result.map(HeavenLetterResponseDto.HeavenLetterListResponse::fromEntity);
    }

    private Specification<HeavenLetter> notDeleted() {
        return (root, query, cb) -> cb.equal(root.get("delFlag"), "N");
    }

    private Specification<HeavenLetter> titleContains(String keyword) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("letterTitle")), "%" + keyword.toLowerCase(Locale.ROOT) + "%");
    }

    private Specification<HeavenLetter> contentsContains(String keyword) {
        return (root, query, cb) ->
                cb.like(root.get("letterContents"), "%" + keyword + "%");
    }

    private Specification<HeavenLetter> titleOrContentsContains(String keyword) {
        return (root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("letterTitle")), "%" + keyword.toLowerCase(Locale.ROOT) + "%"),
                        cb.like(root.get("letterContents"), "%" + keyword + "%")
                );
    }
    //이미지 업로드
    @Transactional
    @Override
    public List<Map<String, String>> uploadFiles(List<MultipartFile> files, String subFolder) throws IOException {
        String baseDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        String uploadDir = baseDir + subFolder + "/";
        List<Map<String, String>> resultList = new ArrayList<>();

        for (MultipartFile file : files) {
            String orgFileName = file.getOriginalFilename();
            String fileName = orgFileName;

            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("fileName", fileName);
            fileMap.put("orgFileName", orgFileName);
            fileMap.put("url", "/images/" + subFolder + "/" + fileName);
            resultList.add(fileMap);
        }
        return resultList;
    }
}

