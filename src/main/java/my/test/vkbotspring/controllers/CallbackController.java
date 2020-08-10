package my.test.vkbotspring.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.services.Service;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
            String type = incMessObject.get("type").getAsString();

            switch (type) {
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
                    try {
                        HttpClient httpClient = HttpClientBuilder.create().build();
                        HttpGet httpGet = new HttpGet(url);

                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        BufferedReader content = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                        String output = content.readLine();
                        JsonObject outputObject = gson.fromJson(output, JsonObject.class);
                        JsonObject responseObject = outputObject.getAsJsonObject("response");
                        String key = responseObject.get("code").getAsString();
                        log.info(key);
                        return key;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "message_new": {

                    break;
                }
            }

        }


        log.info("!!!Controller OK!!!");
        return "OK!";
    }

    @GetMapping("/testconnection")
    public String test() {
        return "Get OK!!!";
    }
}
