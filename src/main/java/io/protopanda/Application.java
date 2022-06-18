package io.protopanda;

import io.protopanda.model.GoldSilverPrice;
import io.protopanda.service.GoldSilverPriceService;
import io.protopanda.service.TelegramMessageService;
import io.protopanda.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class Application {
    private final GoldSilverPriceService goldSilverPriceService;
    private final TelegramMessageService telegramMessageService;
    private Instant startTime;
    private Instant stopTime;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Kolkata")
    public void executeScheduledJob() {

        try {

            log.info("<<-------  Executing Gold Silver Price Telegram-Bot  ------->>");
            log.info("Execution started at : " + ServiceUtil.getCurrentDateTimeInCentralTime());

            startTime = Instant.now();

            GoldSilverPrice goldSilverPrice = goldSilverPriceService.scrapeGoldSilverPrice();

            stopTime = Instant.now();
            long timeElapsed = Duration.between(startTime, stopTime).toMillis();

            telegramMessageService.sendTelegramMessage(goldSilverPrice);

            log.info("Execution completed at : " + ServiceUtil.getCurrentDateTimeInCentralTime());
            log.info("Time elapsed: " + timeElapsed + " milliseconds");

        } catch (Exception ex) {
            log.error("Error occurred while running the application: " + ex.getMessage());
        }
    }
}