package my.test.vkbotspring.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public  class JsonObjectDTO {
    private String type;
    @JsonProperty(value = "group_id")
    private String groupId;
    private String secret;
    private JsonNode object;
}
