package my.test.vkbotspring.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.services.VkApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CallbackController {

    private final VkApiService vkApiService;

    @Autowired
    public CallbackController(VkApiService vkApiService) {
        this.vkApiService = vkApiService;
    }


    @PostMapping(value = "/callback", consumes = {"application/json"})
    @ResponseBody
    public String callback(@RequestBody String incomeMessage) {
        log.info("!!!Controller!!!");
        if (incomeMessage == null) {
            log.warn("!!!NULL message!!!");
        }

        Gson gson = new Gson();
        JsonObject incMessObject = gson.fromJson(incomeMessage, JsonObject.class);
        JsonElement secret = incMessObject.get("secret");
        if (!vkApiService.checkSecretKey(secret)) {
            log.warn("!!!Wrong secret key!!!");
        }
        log.info(incMessObject.get("type").getAsString());
        switch (incMessObject.get("type").getAsString()) {
            /**
             * кейс подтверждения сервера
             * Для получения уведомлений нужно подтвердить адрес сервера.
             * На него будет отправлен POST-запрос, содержащий JSON:
             * {
             *  "type": "confirmation",
             *  "group_id": ***********
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
                String key = vkApiService.getSecurityKey(incMessObject.get("group_id").getAsString());
                log.info(key);
                return key;
            }
            case "message_new": {
                String answer = vkApiService.sendMessage(incMessObject.getAsJsonObject("object"));
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
