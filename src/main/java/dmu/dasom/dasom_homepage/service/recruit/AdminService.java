package dmu.dasom.dasom_homepage.service.recruit;

import dmu.dasom.dasom_homepage.domain.admin.MemberState;
import dmu.dasom.dasom_homepage.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void delete(MemberState memberState) {
        //memberState.getNo; // 나중에 만들어질 member_table의 pk값
        memberState.setMemNo(1);
        adminRepository.deleteMember(memberState);
    }
    public void modify(MemberState memberState) {
        adminRepository.modifyMember(memberState);
    }
}
