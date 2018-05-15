package language;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.json.JSONObject;

import java.util.ArrayList;

@Component
public class LanguageSelector {

    private static RabbitTemplate rabbitTemplate;

    @Autowired
    private LanguageRepository lr;


    @Autowired
    private RabbitAdmin ra;



    public LanguageSelector(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }



    public void receiveMessage(String message) {

        JSONObject tweet = new JSONObject(message);
        String lang = tweet.get("lang").toString();


        ra.declareQueue(new Queue("language/"+lang, false));
        rabbitTemplate.convertAndSend("language/"+lang,tweet.toString());


        if(lang.equals("es")){
            rabbitTemplate.convertAndSend("language/default",tweet.toString());

        }


    }



}
