package my.test.vkbotspring.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import my.test.vkbotspring.controllers.vkApiResponseBuilders.VkApiResponseBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CallbackController {

    private final VkApiResponseBuilder vkApiResponseBuilder = new VkApiResponseBuilder();

    @PostMapping(value = "/callback", consumes = {"application/json"})
    @ResponseBody
    public String callback(@RequestBody String incomeMess) {
        log.info("!!!Controller!!!");

        if(incomeMess != null) {
            Gson gson = new Gson();
            JsonObject incMessObject = gson.fromJson(incomeMess, JsonObject.class);
            JsonObject childObj = incMessObject.getAsJsonObject("object");
            String userMess = childObj.get("body").getAsString();
            String userId = childObj.get("user_id").getAsString();

        }


        log.info("!!!Controller OK!!!");
        return "OK!";
    }

    @GetMapping("/testconnection")
    public String test() {
        return "Get OK!!!";
    }
}
