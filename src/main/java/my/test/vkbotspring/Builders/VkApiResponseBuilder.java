package my.test.vkbotspring.Builders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Билдер для ответов вк апи
 * @author govorun17
 */
@Slf4j
@Component
public class VkApiResponseBuilder {
    //vk api
    @Value("${API_VERSION}")
    private String API_VERSION;
    @Value("${PREFIX_URL}")
    private String PREFIX_URL;
    //методы vk api
    @Value("${SEND_MESSAGES}")
    private String SEND_MESSAGES;
    @Value("${GROUP_CONFIRMATION_CODE}")
    private String GROUP_CONFIRMATION_CODE;
    //настройки юзера
    @Value("${ACCESS_TOKEN}")
    private String ACCESS_TOKEN;
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${GROUP_KEY}")
    private String GROUP_KEY;
    /**
     * Конструктор - инициализирует поля
     * Логирует версию апи (мавен не подтягивает последнюю, если крашится надо указать вручную в билдерах)
     */
    public VkApiResponseBuilder() {
        log.info("!!!VK API VERSION " + API_VERSION + "!!!");
    }

    private String getPrefixUrl() {
        return PREFIX_URL;
    }
    private String getVersion() {
        return API_VERSION;
    }
    private String getToken() {
        return "access_token=" + ACCESS_TOKEN;
    }
    private String getPostfix() {
        return this.getToken() + "&v=" + this.getVersion();
    }
    public String getGROUP_KEY() {
        return GROUP_KEY;
    }

    /**
     * Проверка сообщения сервера
     * @param anyKey ключ, пришедший с запросом
     * @return boolean
     */
    public Boolean checkSecretKey(String anyKey) {
        if (anyKey == null)
            return SECRET_KEY.equals("null");
        else
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
                + "&random_id=" + ThreadLocalRandom.current().nextLong(Integer.MAX_VALUE, Long.MAX_VALUE)
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
