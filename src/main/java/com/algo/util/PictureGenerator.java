package com.algo.util;

import com.luciad.imageio.webp.WebPReadParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PictureGenerator {
    private static final String LOREM_PICSUM_API_URL = "https://picsum.photos/[width]/[height]";
    private static final String OTHER_PICSUM_API_URL = "https://tu.ltyuanfang.cn/api/fengjing.php";
    private static final String[] ANIMATION_PICSUM_PC_API_URLs = {
            "https://t.alcy.cc/ycy",
            "https://t.alcy.cc/moez",
            "https://t.alcy.cc/pc",
            "https://t.alcy.cc/fj",
            "https://t.alcy.cc/bd"
    };
    private static final String[] ANIMATION_PICSUM_MOBILE_API_URLS = {
            "https://t.alcy.cc/mp",
            "https://t.alcy.cc/moemp",
            "https://t.alcy.cc/aimp",
    };
    private static final String ANIMATION_AVATER_API_URL = "https://t.alcy.cc/tx";

    private static int maxAttempts = 3;

    public static byte[] generateLandscapeImage(int width, int height) throws IOException {
        String url = LOREM_PICSUM_API_URL.replace("[width]", String.valueOf(width)).replace("[height]", String.valueOf(height));
        for (int i = 0; i < maxAttempts; i++) {
            try {
                return HttpRequester.getBytes(url);
            } catch (IOException ignored) {

            }
        }
        for (int i = 0; i < maxAttempts; i++) {
            try {
                return HttpRequester.getBytes(OTHER_PICSUM_API_URL);
            } catch (IOException ignored) {

            }
        }
        throw new IOException("获取风景图片失败");
    }

    /**
     * 1 横板
     * 2 竖版
     * 3 头像
     * @param choice
     * @return
     * @throws Exception
     */
    public static byte[] generateAnimationImage(int choice) throws Exception {
        String url = "";
        if (choice == 1) {
            url = ANIMATION_PICSUM_PC_API_URLs[(int) (Math.random() * ANIMATION_PICSUM_PC_API_URLs.length)];
        } else if (choice == 2) {
            url = ANIMATION_PICSUM_MOBILE_API_URLS[(int) (Math.random() * ANIMATION_PICSUM_MOBILE_API_URLS.length)];
        } else {
            url = ANIMATION_AVATER_API_URL;
        }
        for (int i = 0; i < maxAttempts; i++) {
            try {
                return  HttpRequester.getBytes(url); //webp格式图片，需要转换为png格式
//                ImageReader reader = ImageIO.getImageReadersByFormatName("webp").next();
//                reader.setInput(ImageIO.createImageInputStream(new ByteArrayInputStream(bytes)));
//                WebPReadParam readParam = new WebPReadParam();
//                readParam.setBypassFiltering(true);
//                BufferedImage image = reader.read(0, readParam);
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                ImageIO.write(image, "png", outputStream);
//                return outputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new IOException("获取动漫图片失败");
    }
}
