package com.ttasum.memorial.domain.entity.Main;

import javax.persistence.*;

@Entity
@Table(name = "tb25_400_memorial")
public class RememberanceMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer donateSeq;

    @Column(name = "donor_name")
    private String donorName;

    @Column(name = "gender_flag")
    private String genderFlag;

    @Column(name = "donate_age")
    private Integer donateAge;

    @Column(name = "del_flag")
    private String delFlag;

    @Column(name = "modify_time")
    private String modifyTime;
}

