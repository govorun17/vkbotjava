package my.test.vkbotspring.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.Builders.VkApiResponseBuilder;
import my.test.vkbotspring.DTO.ConfCodeDTO;
import my.test.vkbotspring.DTO.NewMessageDTO;
import my.test.vkbotspring.DTO.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


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
    public Boolean checkSecretKey(String anyKey) {
        if (anyKey != null)
            return vkApiResponseBuilder.checkSecretKey(anyKey);
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

        RestTemplate restTemplate = new RestTemplate();
        ResponseDTO response = restTemplate.getForObject(url, ResponseDTO.class);
        if(response != null && response.getResponse() != null){
            ConfCodeDTO codeDTO;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                codeDTO = objectMapper.readValue(response.getResponse().toString(), ConfCodeDTO.class);
            }
            catch (IOException e) {
                log.error(e.getMessage());
                return "ERROR";
            }
            return codeDTO.getCode();
        }
        return "NO RESPONSE";
    }

    /**
     * Отправляет сообщение
     * @param obj JsonObject вытянутый "Object"
     * @return id сообщения
     */
    public String sendMessage(String obj) {
        log.info("Sending message...");
        NewMessageDTO messageDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            messageDTO = objectMapper.readValue(obj, NewMessageDTO.class);
        }
        catch (IOException e) {
            log.error(e.getMessage());
            return "ERROR";
        }

        log.info(messageDTO.getText());
        String url = vkApiResponseBuilder.buildResponseMessage(messageDTO.getFromId().toString(), "Вы написали: " + messageDTO.getText());

        RestTemplate restTemplate = new RestTemplate();
        ResponseDTO response = restTemplate.getForObject(url, ResponseDTO.class);
        if(response != null && response.getResponse() != null){
            return response.getResponse().toString();
        }
        return "NO RESPONSE";
    }
}
