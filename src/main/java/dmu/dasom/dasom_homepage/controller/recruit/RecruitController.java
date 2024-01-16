package dmu.dasom.dasom_homepage.controller.recruit;

import dmu.dasom.dasom_homepage.domain.recruit.DasomApplicant;
import dmu.dasom.dasom_homepage.service.recruit.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruit")
public class RecruitController {
    private final RecruitService recruitService;

    @Autowired
    public RecruitController(RecruitService recruitService) {
        this.recruitService = recruitService;
    }

    @PostMapping()
    public String saveNewApplicant(DasomApplicant applicant) {
        String result = recruitService.saveNewApplicant(applicant);
        return result;
    }

    @GetMapping()
    public List<DasomApplicant> getApplicantList() {
        return recruitService.getApplicantList();
    }

    @GetMapping("/{acStudentNo}")
    public DasomApplicant getApplicant(@PathVariable int acStudentNo) {
        return recruitService.getApplicant(acStudentNo);
    }

    @PutMapping("/{acStudentNo}")
    public String updateApplicantInfo(@PathVariable int acStudentNo, DasomApplicant dasomApplicant) {
        String result = recruitService.updateApplicantInfo(acStudentNo, dasomApplicant);
        return result;
    }

    @DeleteMapping("/{acStudentNo}")
    public String deleteApplicant(@PathVariable int acStudentNo) {
        String result = recruitService.deleteApplicant(acStudentNo);
        return result;
    }

}
