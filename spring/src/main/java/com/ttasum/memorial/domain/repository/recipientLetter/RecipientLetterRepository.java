package com.ttasum.memorial.domain.repository.recipientLetter;

import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipientLetterRepository extends JpaRepository<RecipientLetter, Integer>, JpaSpecificationExecutor<RecipientLetter> {

}
