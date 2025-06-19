package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.memorial.Memorial;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterCommentRepository;
import com.ttasum.memorial.domain.repository.memorial.MemorialRepository;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterUpdateRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.MemorialSearchRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.*;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterRepository;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterNotFoundException;
import com.ttasum.memorial.exception.heavenLetter.InvalidPasswordException;
import com.ttasum.memorial.exception.heavenLetter.MemorialNotFoundException;
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
    private final HeavenLetterCommentRepository heavenLetterCommentRepository;
    private final Logger log = LoggerFactory.getLogger(HeavenLetterServiceImpl.class);

    //등록
    @Transactional
    @Override
    public HeavenLetterResponseDto createLetter(HeavenLetterRequestDto heavenLetterRequestDto) {

        Memorial memorial = null;
        if (heavenLetterRequestDto.getDonateSeq() != null) {
            memorial = memorialRepository.findById(heavenLetterRequestDto.getDonateSeq())
                    .orElseThrow(MemorialNotFoundException::new);
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
    //추모관에서 등록하는 편지폼
    @Override
    @Transactional(readOnly = true)
    public HeavenLetterFormResponseDto getFormWithDonor(Integer donateSeq) {
        Memorial memorial = memorialRepository.findById(donateSeq)
                .orElseThrow(MemorialNotFoundException::new);

        return HeavenLetterFormResponseDto.builder()
                .donateSeq(memorial.getDonateSeq())
                .donorName(memorial.getDonorName())
                .areaCode(memorial.getAreaCode())
                .build();
    }

    //조회 - 단건
    @Transactional
    @Override
    public HeavenLetterResponseDto.HeavenLetterDetailResponse getLetterById(Integer letterSeq) {

        HeavenLetter heavenLetter = heavenLetterRepository.findByLetterSeqAndDelFlag(letterSeq, "N")
                .orElseThrow(HeavenLetterNotFoundException::new);

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
                .map(letter -> {
                    Long commentCount = heavenLetterCommentRepository.countByLetterSeq_LetterSeqAndDelFlag(letter.getLetterSeq(), "N");
                    return HeavenLetterResponseDto.HeavenLetterListResponse.fromEntity(letter, commentCount);
                });
    }
    //수정 인증 (공통)
    @Transactional(readOnly = true)
    @Override
    public boolean verifyPasscode(Integer letterSeq, String passcode) {
        HeavenLetter heavenLetter = heavenLetterRepository.findById(letterSeq)
                .orElseThrow(HeavenLetterNotFoundException::new);

        if (!heavenLetter.getLetterPasscode().equals(passcode)) {
            throw new InvalidPasswordException();
        }

        return true;
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
    public CommonResultResponseDto deleteLetter(HeavenLetterVerifyRequestDto deleteRequest) {

        //비밀번호 인증
        this.verifyPasscode(deleteRequest.getLetterSeq(), deleteRequest.getLetterPasscode());

        HeavenLetter heavenLetter = heavenLetterRepository.findById(deleteRequest.getLetterSeq())
                .orElseThrow(HeavenLetterNotFoundException::new);

        //소프트 삭제 커멘드 사용
        heavenLetter.softDelete();

        return CommonResultResponseDto.success("편지가 정상적으로 삭제 되었습니다.");
    }

    //검색
    @Transactional(readOnly = true)
    @Override
    public Page<HeavenLetterResponseDto.HeavenLetterListResponse> searchLetters(String type, String keyword, Pageable pageable) {


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

    //이미지 업로드(
    // uuid 방식
    @Transactional
    @Override
    public List<Map<String, String>> uploadFiles(List<MultipartFile> files, String subFolder) throws IOException {
        String baseDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        String uploadDir = baseDir + subFolder + "/";
        List<Map<String, String>> resultList = new ArrayList<>();

        for (MultipartFile file : files) {
            String orgFileName = file.getOriginalFilename();

            // 확장자 추출
            String ext = "";
            if (orgFileName != null && orgFileName.contains(".")) {
                ext = orgFileName.substring(orgFileName.lastIndexOf("."));
            }
            // [선택 1] UUID 기반 파일명 (기본값)
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + ext; // 저장용 파일명

            // [선택 2] 랜덤 HEX (32자리, 대문자)
//            String fileName = generateHexFileName(ext);

            //디렉토리 생성
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);

            //파일 저장
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // 접근 가능한 URL 경로 생성
            String url = "/images/" + subFolder + "/" + fileName;

            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("fileName", fileName);
            fileMap.put("orgFileName", orgFileName);
            fileMap.put("url","/images/" + subFolder + "/" + fileName);
            resultList.add(fileMap);
        }
        return resultList;
    }
    // HEX 방식
//    private String generateHexFileName(String ext) {
//        SecureRandom random = new SecureRandom();
//        byte[] bytes = new byte[16];
//        random.nextBytes(bytes);
//
//        StringBuilder hex = new StringBuilder();
//        for (byte b : bytes) {
//            hex.append(String.format("%02X", b));
//        }
//        return hex.toString() + ext;
//    }

    //기증자 검색
    /**
     * type:
     * "name"  → donorName 검색
     * "from"  → donateDate ≥ keyword (yyyy-MM-dd)
     * "to"    → donateDate ≤ keyword (yyyy-MM-dd)
     * "range" → keyword = "yyyy-MM-dd~yyyy-MM-dd"
     */
    @Override
    @Transactional(readOnly = true)

    public Page<MemorialSearchResponseDto> searchDonors(MemorialSearchRequestDto memorialSearchRequest, Pageable pageable) {


        Specification<Memorial> spec = (root, q, cb) -> cb.equal(root.get("delFlag"), "N");

        if (memorialSearchRequest.getDonateName() != null && !memorialSearchRequest.getDonateName().isBlank()) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("donorName")), "%" + memorialSearchRequest.getDonateName().toLowerCase() + "%"));
        }
        if (memorialSearchRequest.getStartDate() != null) {
            spec = spec.and((root, q, cb) ->
                    cb.greaterThanOrEqualTo(root.get("donateDate"), memorialSearchRequest.getStartDate()));
        }
        if (memorialSearchRequest.getEndDate() != null) {
            spec = spec.and((root, q, cb) ->
                    cb.lessThanOrEqualTo(root.get("donateDate"), memorialSearchRequest.getEndDate()));
        }

        return memorialRepository.findAll(spec, pageable)
                .map(MemorialSearchResponseDto::of);
    }
}
