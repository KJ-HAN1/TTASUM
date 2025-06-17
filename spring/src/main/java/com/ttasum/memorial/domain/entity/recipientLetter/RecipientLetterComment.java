package com.ttasum.memorial.domain.entity.recipientLetter;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb25_431_recipient_letter_comment")
public class RecipientLetterComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq", nullable = false)
    private Integer commentSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_seq")
    private RecipientLetter letterSeq;

    @Column(name = "comment_writer", length = 150)
    private String commentWriter;

    @Column(name = "comment_passcode", length = 60)
    private String commentPasscode;

    @Lob
    @Column(name = "contents" , columnDefinition = "TEXT")
    private String contents;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "write_time", nullable = false)
    private LocalDateTime writeTime;

    @Column(name = "writer_id", length = 60)
    private String writerId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    @Column(name = "modifier_id", length = 60)
    private String modifierId;

    @ColumnDefault("'N'")
    @Column(name = "del_flag", nullable = false, length = 1)
    private String delFlag;

    @PrePersist
    public void prepersist(){
        writeTime = LocalDateTime.now();
        this.delFlag = "N";
    }

}