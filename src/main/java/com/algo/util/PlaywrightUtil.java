package com.algo.util;

import com.microsoft.playwright.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class PlaywrightUtil {

    public static String getScreenshot(String html, int width, int height, double scale) throws IOException {
        // 使用 Playwright 渲染 HTML 和截图
        try (Playwright playwright = Playwright.create()) {
            // 启动无头 Chromium 浏览器
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(width, height)  // 设置合适的视口大小
                    .setDeviceScaleFactor(scale)); // 设置设备像素比
            Page page = context.newPage();
            // 设置页面内容为渲染出的 HTML
            page.setContent(html);
            // 等待页面加载完成
            page.waitForLoadState();

            // 截图并裁剪
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setPath(null)); // 截图并保存到内存中
            // 将截图转换为图片
            ByteArrayInputStream inputStream = new ByteArrayInputStream(screenshotBytes);
            BufferedImage screenshot = ImageIO.read(inputStream);
            // 裁剪图像
            BufferedImage croppedImage = screenshot.getSubimage(0, 0, (int)(width * scale), (int)(height * scale)); // 按需裁剪大小

            // 将裁剪后的图像保存为 Base64 编码字符串
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(croppedImage, "png", outputStream);
            browser.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IOException("Failed to take screenshot", e);
        }
    }
}
