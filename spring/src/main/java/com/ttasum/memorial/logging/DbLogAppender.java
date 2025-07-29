/**
 * Logback 전용 custom appender
 */

package com.ttasum.memorial.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DbLogAppender extends AppenderBase<ILoggingEvent> {
    private Connection connection;

    @Override
    public void start() {

        String activeProfile = System.getProperty("spring.profiles.active");
        if (activeProfile != null && activeProfile.contains("test")) {
            // 테스트 환경이면 Appender 시작하지 않음
            addInfo("테스트 환경 감지됨: DbLogAppender 비활성화");
            return;
        }
        try {
            // db 연결 로컬용
//            Dotenv dotenv = Dotenv.configure()
//                    .ignoreIfMissing()
//                    .load();
//            String dbPassword = dotenv.get("DB_PW");

            // 서버용 getenv -> 서버 톹캣 setenv파일에서 get
            String dbPassword = System.getenv("DB_PW");


            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://139.150.65.219:3306/koda_2025?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8",
                    "fastcampus",
                    dbPassword
            );
            super.start();
        } catch (Exception e) {
            addError("DB 연결 실패", e);
        }
    }

    //db 데이터 삽입

    @Override
    protected void append(ILoggingEvent event) {
        Map<String, String> mdc = event.getMDCPropertyMap();
        String urlName  = mdc.getOrDefault("url_name", "-");
        String crudCode = mdc.getOrDefault("crud_code", "-");
        String ipAddr   = mdc.getOrDefault("ip_addr", "-");
        String logText  = mdc.getOrDefault("log_text", "-");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO tb25_940_action_log(url_name, crud_code, ip_addr, log_text) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, urlName);
            stmt.setString(2, crudCode);
            stmt.setString(3, ipAddr);
            stmt.setString(4, logText);
            stmt.executeUpdate();
        } catch (SQLException e) {
//            System.out.println("실패"+e.getMessage());
            addError("로그 DB 저장 실패", e);
        }
    }
}
