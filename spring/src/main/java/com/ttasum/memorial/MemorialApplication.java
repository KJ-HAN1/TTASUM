package com.ttasum.memorial;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemorialApplication {

    public static void main(String[] args) {
        // .env 파일을 로드하여 환경변수로 설정
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()   // .env 파일이 없어도 에러 안 나게
                .load();
        System.setProperty("DB_PW", dotenv.get("DB_PW"));
        System.setProperty("CAP_KEY", dotenv.get("CAP_KEY"));

        SpringApplication.run(MemorialApplication.class, args);

    }

}
