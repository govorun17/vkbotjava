package my.test.vkbotspring.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.services.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@Slf4j
public class CallbackController {

    private final Service service = new Service();

    @PostMapping(value = "/callback", consumes = {"application/json"})
    @ResponseBody
    public String callback(@RequestBody String incomeMess) {
        log.info("!!!Controller!!!");

        if(incomeMess != null) {
            Gson gson = new Gson();
            JsonObject incMessObject = gson.fromJson(incomeMess, JsonObject.class);
            JsonElement secret = incMessObject.get("secret");
            if (secret != null && secret.getAsString().equals("govorun17")) {

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
                     * @return КЛЮЧ полученый с url Service
                     * @see Service#confirmUrl(String)
                     */
                    case "confirmation": {
                        String url = service.confirmUrl(incMessObject.get("group_id").getAsString());
                        String key = service.getSecurityKey(url);
                        log.info(key);
                        return "3bf173bb";
                    }
                    case "message_new": {

                        break;
                    }

                }
            }
            else {
                log.warn("!!!Wrong secret key!!!");
                return "Wrong secret key";
            }
        }
        else {
            log.warn("!!!NULL message!!!");
            return "NULL message";
        }

        log.info("!!!Controller OK!!!");
        return "OK!";
    }

    @GetMapping("/testconnection")
    public String test() {
        return "Get OK!!!";
    }
}
