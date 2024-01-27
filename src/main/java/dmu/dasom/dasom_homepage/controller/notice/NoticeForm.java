package dmu.dasom.dasom_homepage.controller.notice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeForm {
    @GetMapping("/notice-form")
    public String noticeForm(){
        return "/notice-form";
    }
}
