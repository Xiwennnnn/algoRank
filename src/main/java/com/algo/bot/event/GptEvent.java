package com.algo.bot.event;

import com.algo.util.GptUtil;
import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Shiro
@Component
public class GptEvent {
    private static final Logger log = LoggerFactory.getLogger(GptEvent.class);

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/gpt")
    public void onGptEvent(Bot bot, AnyMessageEvent event) {
        String message = event.getMessage().replace("/gpt", "").trim();
        try {
            String result = GptUtil.chat(message);
            bot.sendMsg(event, result, false);
        } catch (Exception e) {
            log.warn("GPT error", e);
            bot.sendMsg(event, "🥲出错了，请稍后再试", false);
        }
    }
}
