package my.test.vkbotspring;

import my.test.vkbotspring.services.VkApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource(value = "classpath:application-test.properties")
class VkbotspringApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private VkApiService service;
	@Value(value = "${GROUP_ID}")
	private String GROUP_ID;
	private final String json = "{ \n" +
			"    \"type\": \"message_new\", \n" +
			"    \"object\": {\n" +
			"        \"id\": 1,\n" +
			"        \"from_id\": 183984964,\n" +
			"        \"text\": \"qwer\"\n" +
			"    },\n" +
			"    \"secret\": \"govorun17\"\n" +
			"}";

	@Test
	void contextLoads() throws Exception{
		when(service.getSecurityKey(GROUP_ID)).thenReturn("Ключ получен");
		this.mockMvc.perform(post("/callback").content(json).contentType("json"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("qwer")));
	}

}
