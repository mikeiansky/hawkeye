package io.github.mikeiansky.hawkeye.sample;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
class HawkeyeSampleControllerTest {

    @Autowired
    public WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

//    @Before
    @BeforeEach
    public void before() {
        System.out.println("BeforeEach =======> ");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private HttpHeaders mockHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json;charset=UTF-8");
        headers.add("authorization", "MTA3OTE2MjpoZWxsbw==");
        return headers;
    }

    @Test
    public void testSampleException() throws InterruptedException {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/hawkeye/sample/exception")
                .headers(mockHeaders())
                .param("type", "6")
                ;

        try {
            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(2000L);
    }

    @Test
    public void testSampleTimeout() throws InterruptedException {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/hawkeye/sample/timeout")
                .headers(mockHeaders())
                .param("type", "6")
                ;

        try {
            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(2000L);

    }


}
