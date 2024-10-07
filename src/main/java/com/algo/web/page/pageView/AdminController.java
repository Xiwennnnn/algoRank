package com.algo.web.page.pageView;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    @RequestMapping("/admin")
    public String adminPage() {
        return "adminLogin";
    }

    @RequestMapping("/admin/login")
    public String adminLogin(String password, RedirectAttributes redirectAttributes) {
        if ("admin".equals(password)) {
            return "adminPage"; // 登录成功，跳转到管理页面
        } else {
            // 使用RedirectAttributes传递错误信息
            redirectAttributes.addFlashAttribute("error", "密码错误，请重试。");
            return "redirect:/admin"; // 重定向回登录页面
        }
    }
}
