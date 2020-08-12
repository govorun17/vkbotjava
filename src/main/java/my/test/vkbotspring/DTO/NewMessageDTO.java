package my.test.vkbotspring.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
@JsonComponent
public class NewMessageDTO {
    private Integer data;
    @JsonProperty(value = "from_id")
    private Integer fromId;
    private Integer id;
    private Integer out;
    @JsonProperty(value = "peer_id")
    private Integer peerId;
    private String text;
    @JsonProperty(value = "conversation_message_id")
    private Integer conversationMessageId;
    @JsonProperty(value = "fwd_messages")
    private ArrayList<Object> fwdMessages;
    private Boolean important;
    @JsonProperty(value = "random_id")
    private Integer randomId;
    private ArrayList<Object> attachments;
    @JsonProperty(value = "is_hidden")
    private Boolean isHidden;
}
