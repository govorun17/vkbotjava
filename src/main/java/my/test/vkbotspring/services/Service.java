package my.test.vkbotspring.services;

import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.controllers.vkApiResponseBuilders.VkApiResponseBuilder;


@Slf4j
public class Service {

    private final VkApiResponseBuilder vkApiResponseBuilder = new VkApiResponseBuilder();

    /**
     * Возвращает ссылку для получения ключа подтверждения по ключу круппы
     * @param groupKey id группы
     * @return String URL
     */
    public String confirmUrl(String groupKey) {
        log.info("!!!confirming Url!!!");
        String url = vkApiResponseBuilder.buildRequestConfirming(groupKey);
        log.info(url);
        return url;
    }
}
