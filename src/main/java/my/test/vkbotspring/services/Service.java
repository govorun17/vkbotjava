package my.test.vkbotspring.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.controllers.vkApiResponseBuilders.VkApiResponseBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


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

    /**
     * Депарсит полученый джсон в поиске ключа или ошибки
     * @param url ссылка на получение ключа доступа
     * @return ключ доступа
     */
    public String getSecurityKey(String url) {
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            BufferedReader content = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String output = content.readLine();
            JsonObject outputObject = gson.fromJson(output, JsonObject.class);
            JsonObject responseObject = outputObject.getAsJsonObject("response");
            if (responseObject != null) {
                return responseObject.get("code").getAsString();
            } else {
                responseObject = outputObject.getAsJsonObject("error");
                String errorMess = responseObject.get("err_mess").getAsString();
                log.error(errorMess);
                return errorMess;
            }
        }
        catch (IOException e) {
            log.error(e.getMessage());
            return "ERROR!!!";
        }
    }
}
