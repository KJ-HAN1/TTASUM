package com.ttasum.memorial.controller;

import com.ttasum.memorial.dto.Main.MainDto;
import com.ttasum.memorial.service.Main.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping
    public MainDto main() {
        return mainService.getMainDto();
    }

}
