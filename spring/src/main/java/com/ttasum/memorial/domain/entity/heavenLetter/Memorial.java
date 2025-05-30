package com.ttasum.memorial.domain.entity.heavenLetter;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb25_400_memorial")
public class Memorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donate_seq", nullable = false)
    private Integer donateSeq;

    //반대쪽 엔티티의 필드명을 기준으로 연결하는 어노테이션
    @OneToMany(mappedBy = "donateSeq")
    //HeavenLetter를 heavenLetters라는 리스트에 담아서 가져옴
    private List<HeavenLetter> heavenLetter1s;

    @Column(name = "donor_name", length = 150)
    private String donorName;

    @Column(name = "anonymity_flag", length = 1)
    private String anonymityFlag;

    @Column(name = "donate_title", length = 600)
    private String donateTitle;

    @Column(name = "area_code", length = 10)
    private String areaCode;

    @Lob
    @Column(name = "contents" , columnDefinition = "TEXT")
    private String contents;

    @Column(name = "file_name", length = 600)
    private String fileName;

    @Column(name = "org_file_name", length = 600)
    private String orgFileName;

    @Column(name = "writer", length = 150)
    private String writer;

    @Column(name = "donate_date", length = 8)
    private String donateDate;

    @Column(name = "gender_flag", length = 1)
    private String genderFlag;

    @Column(name = "donate_age")
    private Integer donateAge;

    @Column(name = "flower_count")
    private Integer flowerCount;

    @Column(name = "love_count")
    private Integer loveCount;

    @Column(name = "see_count")
    private Integer seeCount;

    @Column(name = "miss_count")
    private Integer missCount;

    @Column(name = "proud_count")
    private Integer proudCount;

    @Column(name = "hard_count")
    private Integer hardCount;

    @Column(name = "sad_count")
    private Integer sadCount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "write_time", nullable = false)
    private LocalDateTime writeTime;

    @Column(name = "writer_id", nullable = false, length = 60)
    private String writerId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    @Column(name = "modifier_id", nullable = false, length = 60)
    private String modifierId;

    @ColumnDefault("'N'")
    @Column(name = "del_flag", nullable = false, length = 1)
    private String delFlag;

}