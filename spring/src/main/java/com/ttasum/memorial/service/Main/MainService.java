package com.ttasum.memorial.service.Main;

import com.ttasum.memorial.domain.repository.Main.HeavenLetterMainRepository;
import com.ttasum.memorial.domain.repository.Main.RememberanceMainRepository;
import com.ttasum.memorial.dto.Main.HeavenLetterMainDto;
import com.ttasum.memorial.dto.Main.MainDto;
import com.ttasum.memorial.dto.Main.RememberanceMainDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final HeavenLetterMainRepository heavenLetterMainRepository;
    private final RememberanceMainRepository rememberanceMainRepository;

    public MainDto getMainDto() {

        List<HeavenLetterMainDto> heavenLetterMainDtoList = heavenLetterMainRepository.findRecentHeavenLetters(PageRequest.of(0, 10));
        List<RememberanceMainDto> rememberanceMainDtoList = rememberanceMainRepository.findRecentRememberance(PageRequest.of(0, 5));
        return new MainDto(heavenLetterMainDtoList, rememberanceMainDtoList);
    }

}
