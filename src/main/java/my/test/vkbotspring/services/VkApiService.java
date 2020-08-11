package my.test.vkbotspring.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.Builders.VkApiResponseBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Slf4j
@Service
public class VkApiService {

    private final VkApiResponseBuilder vkApiResponseBuilder;

    @Autowired
    public VkApiService(VkApiResponseBuilder vkApiResponseBuilder) {
        this.vkApiResponseBuilder = vkApiResponseBuilder;
    }

    /**
     * Проверка сообщения сервера
     * @param anyKey ключ, пришедший с запросом
     * @return boolean
     * @see VkApiResponseBuilder#checkSecretKey(String) 
     */    
    public Boolean checkSecretKey(JsonElement anyKey) {
        if (anyKey != null)
            return vkApiResponseBuilder.checkSecretKey(anyKey.getAsString());
        else
            return vkApiResponseBuilder.checkSecretKey(null);
    }

    /**
     * Депарсит полученый джсон в поиске ключа или ошибки
     * @param groupKey id группы
     * @return ключ доступа
     */
    public String getSecurityKey(String groupKey) {
        if (!vkApiResponseBuilder.getGROUP_KEY().toLowerCase().equals("null"))
            return vkApiResponseBuilder.getGROUP_KEY();

        log.info("!!!confirming Url!!!");
        String url = vkApiResponseBuilder.buildRequestConfirming(groupKey);
        log.info(url);

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
            }
            else {
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

    /**
     * Отправляет сообщение
     * @param message JsonObject вытянутый "Object"
     * @return id сообщения
     */
    public String sendMessage(JsonObject message) {
        log.info("Sending message...");
        String userId = message.get("from_id").getAsString();
        String userMessage = message.get("text").getAsString();
        log.info(userMessage);
        String url = vkApiResponseBuilder.buildResponseMessage(userId, "Вы написали: " + userMessage);

        try {
            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            BufferedReader content = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String output = content.readLine();
            JsonObject outputObject = gson.fromJson(output, JsonObject.class);
            return outputObject.get("response").getAsString();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return "ERROR!!!";
        }
    }
}
