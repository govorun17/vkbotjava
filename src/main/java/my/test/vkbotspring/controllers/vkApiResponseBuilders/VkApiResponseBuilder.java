package my.test.vkbotspring.controllers.vkApiResponseBuilders;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Билдер для ответов вк апи
 * @author govorun17
 */
@Slf4j
public class VkApiResponseBuilder {
    //методы vk api
    private static final String SEND_MESSAGES = "messages.send?";
    //vk api
    private final VkApiClient vkApiClient;
    private final Random random;
    //токен
    private final String accessToken = "d898da7492c166b963dcf1350ee42f5182c088a91a96703e535fd3cec50a8e69f37bab9ee6844aaceb7fb";

    /**
     * Конструктор - инициализирует поля
     * Логирует версию апи (мавен не подтягивает последнюю, если крашится надо указать вручную в билдерах)
     */
    public VkApiResponseBuilder() {
        this.random = new Random();
        this.vkApiClient = new VkApiClient(new HttpTransportClient());
        log.info("!!!VK API VERSION" + vkApiClient.getVersion() + "!!!");
    }

    private String getPrefixUrl() {
        return vkApiClient.getApiEndpoint();
    }
    private String getVersion() {
        return vkApiClient.getVersion();
    }
    private String getToken() {
        return "access_token=" + this.accessToken;
    }

    /**
     * Билдит ответ-сообщение
     * @param message сообщение, которое увидит пользователь
     * @param userId ID пользователя - искать в Json с пришедшим запросом
     * @return возвращает строку с url для апи
     */
    public String buildResponseMessage(String userId, String message) {
        return this.getPrefixUrl() + SEND_MESSAGES
                + "user_id=" + userId
                + "&message=" + message
                + "&random_id=" + random.nextInt()
                + "&" + this.getToken();
    }

}
