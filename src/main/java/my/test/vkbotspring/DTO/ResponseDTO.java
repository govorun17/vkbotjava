package my.test.vkbotspring.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseDTO {
    private JsonNode response;
    private final String vkAnswer = "ok";
}
