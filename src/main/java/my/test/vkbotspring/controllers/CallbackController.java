package my.test.vkbotspring.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.DTO.JsonObjectDTO;
import my.test.vkbotspring.services.VkApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class CallbackController {

    private final VkApiService vkApiService;
    private final Gson gson;

    @Autowired
    public CallbackController(VkApiService vkApiService) {
        this.vkApiService = vkApiService;
        this.gson = new Gson();
    }

    @PostMapping(value = "/callback", consumes = {"application/json"})
    public String callback(@RequestBody JsonObjectDTO incomeMessage) {
        log.info("!!!Controller!!!");
        if (incomeMessage == null) {
            log.warn("!!!NULL message!!!");
            return "ok";
        }

        if (!vkApiService.checkSecretKey(incomeMessage.getSecret())) {
            log.warn("!!!Wrong secret key!!!");
        }
        log.info(incomeMessage.getType());
        switch (incomeMessage.getType()) {
            /**
             * кейс подтверждения сервера
             * Для получения уведомлений нужно подтвердить адрес сервера.
             * На него будет отправлен POST-запрос, содержащий JSON:
             * {
             *  "type": "confirmation",
             *  "group_id": ***********,
             *  "secret": ********
             * }
             * Парсим group_id и запрашиваем ссылку
             * Get запрос возвращает ключ-подтверждение
             * {
             *  "response": {
             *      "code": "********"
             *  }
             * }
             * парсим - вытягиваем ключ, возвращаем VK Api
             * @return КЛЮЧ полученый с url VkApiService
             * @see VkApiService#confirmUrl(String)
             */
            case "confirmation": {
                String key = vkApiService.getSecurityKey(incomeMessage.getGroupId());
                log.info(key);
                return key;
            }
            case "message_new": {
                String answer =
                        vkApiService.sendMessage(incomeMessage.getObject().toString());
                log.info(answer);
            }

        }
        log.info("!!!Controller OK!!!");
        return "ok";
    }

    @GetMapping("/testconnection")
    public String test() {
        return "Get OK!!!";
    }
}
