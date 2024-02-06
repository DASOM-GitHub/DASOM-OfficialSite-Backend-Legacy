package dmu.dasom.dasom_homepage.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectForm {
    @GetMapping("/board/project-form")
    public String projectForm(){
        return "/project-form";
    }
}