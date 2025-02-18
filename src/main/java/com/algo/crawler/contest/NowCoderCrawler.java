package com.algo.crawler.contest;

import com.algo.data.common.ContestStatus;
import com.algo.data.dto.ContestDto;
import com.algo.exception.ContestCrawlerWrtongException;
import com.algo.exception.HttpRequestWrongException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class NowCoderCrawler extends ContestBaseCrawler {
    private static final String ACM_URL = "https://ac.nowcoder.com/acm/contest/vip-index?rankTypeFilter=0";
    private static final String OI_URL = "https://ac.nowcoder.com/acm/contest/vip-index?rankTypeFilter=2";

    @Override
    public List<ContestDto> crawl() throws ContestCrawlerWrtongException {
        List<ContestDto> contestList = null;
        List<ContestDto> oiList = null;
        contestList = crawl(ACM_URL);
        oiList = crawl(OI_URL);
        for (ContestDto contest : oiList) {
            contest.setOiContest(true);
        }
        contestList.addAll(oiList);
        return contestList;
    }

    private List<ContestDto> crawl(String url) throws ContestCrawlerWrtongException {
        List<ContestDto> contestList = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(get(url));
        } catch (HttpRequestWrongException e) {
            throw new ContestCrawlerWrtongException(e.getMessage());
        }
        Elements contests = doc.select("div.platform-item-cont");
        for (Element contest : contests) {
            contestList.add(parseContest(contest));
        }
        return contestList;
    }

    private ContestDto parseContest(Element contest) {
        String name = contest.selectFirst("a").text();
        String link = "https://ac.nowcoder.com" + contest.selectFirst("a").attr("href");

        String timeRange = contest.selectFirst("li.match-time-icon").text().replace('（', '(');
        Date startTime = parseDate(subMid(timeRange, "比赛时间：", " 至 "));
        Date endTime = parseDate(subMid(timeRange, " 至 ", " ("));

        boolean register = contest.selectFirst("span.match-signup") != null;

        String status = ContestStatus.PUBLIC;
        if (register) {
            status = ContestStatus.REGISTER;
        }

        return new ContestDto("NowCoder", name, startTime, endTime, status, link);
    }

    private Date parseDate(String time) {
        return parseDate(time, "yyyy-MM-dd HH:mm", "Asia/Shanghai");
    }
}
