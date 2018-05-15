package language;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.json.JSONObject;

@Component
public class LanguageSelector {

    private static RabbitTemplate rabbitTemplate;


    @Autowired
    private RabbitAdmin ra;


    @Value( "es")
    private String langSel;

    @Autowired
    private LanguageSelectionRepository lsr;





    public LanguageSelector(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        langSel = lsr.findAll().iterator().next().getLanguage();
    }


    public void receiveMessage(String message) {

        JSONObject tweet = new JSONObject(message);
        String lang = tweet.get("lang").toString();


        ra.declareQueue(new Queue("language/"+lang, false));
        rabbitTemplate.convertAndSend("language/"+lang,tweet.toString());


        if(lang.equals(langSel)){
            rabbitTemplate.convertAndSend("language/default",tweet.toString());

        }


    }



}
