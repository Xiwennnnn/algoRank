package com.algo.bot.event;

import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.MessageHandlerFilter;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Shiro
@Component
@Log
public class QQBotHelper {

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = {"/帮助", "/help", "/菜单", "/menu"})
    public void getHelp(Bot bot, AnyMessageEvent event) {
        String msg = event.getMessage().replace("/帮助", "").replace("/help", "").replace("/菜单", "").replace("/menu", "").trim();
        if (msg.isEmpty()) {
            String helpMsg = MsgUtils.builder()
                    .text("📑帮助菜单，现有以下功能模块(所有命令前方都需要加上/)\n")
                    .text("1. 比赛：想获得比赛详细帮助，请发送/help 比赛\n")
                    .text("2. 用户：想要获得用户详细帮助，请发送/help 用户\n")
                    .text("3. 信息：想要获得信息详细帮助，请发送/help 信息\n")
                    .text("4. 娱乐：想要获得娱乐详细帮助，请发送/help 娱乐\n")
                    .text("5. 算法协会排行榜网站：发送/排行榜").build();
            bot.sendMsg(event, helpMsg, false);
        } else if ("比赛".equals(msg)) {
            String helpMsg = MsgUtils.builder()
                    .text("🏆比赛功能可获取最近比赛的详细信息，共有如下命令：\n")
                    .text("1. /比赛：获取最近比赛信息(默认ACM赛制)\n")
                    .text("2. /acm比赛：获取最近ACM赛制比赛信息\n")
                    .text("3. /oi比赛：获取最近OI赛制比赛信息\n")
                    .text("4. /比赛 OJ1 OJ2...：获取指定OJ赛制比赛信息").build();
            bot.sendMsg(event, helpMsg, false);
        } else if ("用户".equals(msg)) {
            String helpMsg = MsgUtils.builder()
                    .text("👤用户功能可获取用户信息，共有如下命令：\n")
                    .text("1. /用户 [用户名]：获取指定用户信息\n")
                    .text("2. /添加 [真名] 专业:xxx 年级:22 lc:[用户名] cf:[用户名]：将自己加入到算法协会排行榜中，之后可用真名查询\n")
                    .text("3. /修改 [真名] [专业/年级/lc/cf]:[新值]：修改用户信息\n")
                    .text("4. /删除 [真名]：删除用户信息\n")
                    .text("(专业现只支持计算机科学与技术、软件工程、大数据、人工智能、信息与计算科学、计算机大类，其他专业请使用【其他专业:xxx】)")
                    .text("(例如【/添加 张三 专业:计算机科学与技术 年级:22 lc:cf_zhangsan cf:cf_lisi】)").build();
            bot.sendMsg(event, helpMsg, false);
        } else if ("信息".equals(msg)) {
            String helpMsg = MsgUtils.builder()
                    .text("💡信息功能可获取信息，共有如下命令：\n")
                    .text("1. /cf [用户名]：获取codeforces明信片\n")
                    .text("2. /lc [用户名]：获取leetcode明信片\n")
                    .text("3. /cfrk [用户名1] [用户名2]...：获取codeforces统计信息卡片\n")
                    .text("4. /lcrk [用户名]：获取leetcode统计信息卡片").build();
        }  else if ("娱乐".equals(msg)) {
            String helpMsg = MsgUtils.builder()
                    .text("🎮娱乐功能可获取娱乐信息，共有如下命令：\n")
                    .text("1. /gpt [问题]：向gpt提问\n")
                    .text("2. /风景 [w]x[h]：获取指定大小的风景图片如:[/风景 1920x1080]\n")
                    .text("3. /动漫 1/2/3：1为横板，2为竖板, 3为头像，默认为横板\n")
                    .text("其他功能待开发......(有创意的也可以私聊2077353357)").build();
            bot.sendMsg(event, helpMsg, false);
        } else {
            bot.sendMsg(event, "😭😭😭没有找到相关帮助信息，请重新输入！", false);
        }
    }

    @AnyMessageHandler
    @MessageHandlerFilter(startWith = "/排行榜")
    public void getRank(Bot bot, AnyMessageEvent event) {
        bot.sendMsg(event, "💭💡🎈排行榜网址：http://101.33.254.198/", false);
    }
}
