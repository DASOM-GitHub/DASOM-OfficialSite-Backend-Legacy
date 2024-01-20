package dmu.dasom.dasom_homepage.controller.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicant;
import dmu.dasom.dasom_homepage.service.recruit.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/recruit")
public class RecruitController {
    private final RecruitService recruitService;

    @Autowired
    public RecruitController(RecruitService recruitService) {
        this.recruitService = recruitService;
    }

    @GetMapping("/apply")
    public String applyForm() {
        return "/apply-form";
    }

    @PostMapping("/apply")
    public String saveNewApplicant(DasomApplicant dasomApplicant, Model model) {
        if (recruitService.isApplicantValid(dasomApplicant.getAcStudentNo())) {
            // 지원자의 학번이 중복 될 경우 브라우저에 alert를 띄움
            model.addAttribute("alert", "이미 지원하셨습니다.");
            return "/apply-form";
        } else {
            // 지원 성공 페이지로 redirect 예정
            recruitService.saveNewApplicant(dasomApplicant);
            return "redirect:/";
        }
    }

    @ResponseBody
    @GetMapping("/list")
    public List<DasomApplicant> showApplicantList() {
        return recruitService.getApplicantList();
    }

}
