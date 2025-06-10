package com.ttasum.memorial.domain.entity.Main;

import javax.persistence.*;

@Entity
@Table(name = "tb25_410_heaven_letter")
public class HeavenLetterMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer letterSeq;

    @Column(name = "letter_title")
    private String letterTitle;

    @Column(name = "donor_name")
    private String donorName;

    @Column(name = "letter_writer")
    private String letterWriter;

    @Column(name = "del_flag")
    private String delFlag;

    @Column(name = "write_time")
    private String writeTime;

}