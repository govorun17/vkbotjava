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
    private static final String GROUP_CONFIRMATION_CODE = "groups.getCallbackConfirmationCode?";
    //vk api
    private final VkApiClient vkApiClient;
    private final Random random;
    //токен
    private static final String ACCESS_TOKEN = "4a0fbc7bd95b8c06f2176bee7e90ca6be26fedbefdacb313b480aabd0c38dca4c75122be28f85e7a5c6bc";
    private static final String SECRET_KEY = "govorun17";

    /**
     * Конструктор - инициализирует поля
     * Логирует версию апи (мавен не подтягивает последнюю, если крашится надо указать вручную в билдерах)
     */
    public VkApiResponseBuilder() {
        this.random = new Random();
        this.vkApiClient = new VkApiClient(new HttpTransportClient());
        log.info("!!!VK API VERSION " + vkApiClient.getVersion() + "!!!");
    }

    private String getPrefixUrl() {
        return vkApiClient.getApiEndpoint();
    }
    private String getVersion() {
        return vkApiClient.getVersion();
    }
    private String getToken() {
        return "access_token=" + ACCESS_TOKEN;
    }
    private String getPostfix() {
        return this.getToken() + "&v=" + this.getVersion();
    }

    /**
     * Проверка сообщения сервера
     * @param anyKey ключ, пришедший с запросом
     * @return boolean
     */
    public Boolean checkSecretKey(String anyKey) {
        return anyKey.equals(SECRET_KEY);
    }

    /**
     * Создает ответ-сообщение
     * @param message сообщение, которое увидит пользователь
     * @param userId ID пользователя - искать в Json с пришедшим запросом
     * @return возвращает строку с url для апи
     */
    public String buildResponseMessage(String userId, String message) {
        return this.getPrefixUrl() + SEND_MESSAGES
                + "user_id=" + userId
                + "&message=" + message
                + "&random_id=" + random.nextInt()
                + "&" + this.getPostfix();
    }

    /**
     * Создает запрос-подтверждение ссылки
     * @param groupId id группы
     * @return String
     */
    public String buildRequestConfirming(String groupId) {
        return this.getPrefixUrl() + GROUP_CONFIRMATION_CODE
                + "group_id=" + groupId
                + "&" + this.getPostfix();
    }
}
