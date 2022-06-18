package io.protopanda.service;

import io.protopanda.model.GoldSilverPrice;
import io.protopanda.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.code.CodeBlock;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramMessageService {
    private final Environment environment;
    private final String TELEGRAM_BOT_TOKEN = "telegram-bot.token";
    private final String TELEGRAM_MESSAGE_RECEIVER_CHAT_ID = "telegram-bot.message-receiver.chat-id";

    public void sendTelegramMessage(GoldSilverPrice goldSilverPrice) {

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String telegramBotToken = this.environment.getRequiredProperty(TELEGRAM_BOT_TOKEN);
            String telegramMessageReceiverChatId = this.environment.getRequiredProperty(TELEGRAM_MESSAGE_RECEIVER_CHAT_ID);
            String telegramBotUrl = "https://api.telegram.org/bot";

            String telegramMessage = this.generateTelegramMessage(goldSilverPrice);

            HttpEntity<MultiValueMap<String, String>> telegramRequest = this.prepareTelegramRequest(telegramMessageReceiverChatId, telegramMessage, headers);
            ResponseEntity<String> responseEntity = new RestTemplate()
                    .exchange(telegramBotUrl + telegramBotToken + "/sendMessage", HttpMethod.POST, telegramRequest, String.class, new Object[0]);

            log.info("Telegram API Response: " + responseEntity.getBody());

        } catch (Exception ex) {
            log.info("Error occurred while sending telegram message: " + ex.getMessage());
        }
    }

    public String generateTelegramMessage(GoldSilverPrice goldSilverPrice) {

        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_CENTER, Table.ALIGN_CENTER)
                .withRowLimit(2)
                .addRow("Date Time", "Gold(22ct)", "Silver");

        tableBuilder.addRow((goldSilverPrice.getDateInIndia()),
                ServiceUtil.convertToIndianCurrencyFormat(String.valueOf(goldSilverPrice.getGoldRate())),
                ServiceUtil.convertToIndianCurrencyFormat(String.valueOf(goldSilverPrice.getSilverRate())));

        return String.valueOf(new CodeBlock(tableBuilder.build(), "markdown"));
    }

    public HttpEntity<MultiValueMap<String, String>> prepareTelegramRequest(String chatId, String textMessage, HttpHeaders headers) {
        MultiValueMap<String, String> urlEncodedMap = new LinkedMultiValueMap();
        urlEncodedMap.add("chat_id", chatId);
        urlEncodedMap.add("text", textMessage);
        urlEncodedMap.add("parse_mode", "markdown");
        HttpEntity<MultiValueMap<String, String>> telegramRequest = new HttpEntity(urlEncodedMap, headers);
        return telegramRequest;
    }
}
