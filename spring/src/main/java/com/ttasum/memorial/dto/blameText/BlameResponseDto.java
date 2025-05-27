package com.ttasum.memorial.dto.blameText;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BlameResponseDto {
    private String sentence;
    private int label;
    private double confidence;
    private List<Map<String, Object>> details;
}
