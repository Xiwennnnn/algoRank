package com.algo.bot.event;

import com.algo.bot.data.ErrorMsgEnum;
import com.algo.util.PictureGenerator;
import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Shiro
@Component
@Log
public class EntertainmentEvent {

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/风景")
    public void GetLandscapePhotos(Bot bot, AnyMessageEvent event) {
        String args = event.getMessage().replace("/风景", "").trim();
        String[] params;
        if (args.isEmpty()) {
            params = new String[]{"1920", "1080"};
        } else {
            params = args.split("x");
        }
        if (params.length!= 2) {
            bot.sendMsg(event, "🥲请按照格式/风景 宽x高，如/风景 1080x1920", false);
            return;
        }
        bot.sendMsg(event, "🌐正在生成风景图片，请稍后……", false);
        try {
            byte[] img = PictureGenerator.generateLandscapeImage(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
            String base64 = MsgUtils.builder().img("base64://" + Base64.getEncoder().encodeToString(img)).build();
            bot.sendMsg(event, base64, false);
            log.info("风景图片生成成功");
        } catch (Exception e) {
            log.warning("风景图片生成失败：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.SERVER_ERROR.msg, false);
        }
    }

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = {"/二次元", "/动漫"})
    public void GetAnimationImages(Bot bot, AnyMessageEvent event) {
        String arg = event.getMessage().replace("/二次元", "").replace("/动漫", "").trim();
        int flag;
        if (arg.isEmpty() || "1".equals(arg)) {
            flag = 1;
        } else if ("2".equals(arg)) {
            flag = 2;
        } else if ("3".equals(arg)) {
            flag = 3;
        } else {
            bot.sendMsg(event, ErrorMsgEnum.FORMAT_ERROR.msg, false);
            return;
        }
        bot.sendMsg(event, "🌐正在生成动漫图片，请稍后……", false);
        try {
            byte[] img = PictureGenerator.generateAnimationImage(flag);
            String base64 = MsgUtils.builder().img("base64://" + Base64.getEncoder().encodeToString(img)).build();
            bot.sendMsg(event, base64, false);
        } catch (Exception e) {
            log.warning("动漫图片生成失败：" + e.getMessage());
            bot.sendMsg(event, ErrorMsgEnum.SERVER_ERROR.msg, false);
        }
    }
}
