package dmu.dasom.dasom_homepage.service.admin;

import dmu.dasom.dasom_homepage.domain.admin.MemberState;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    // 회원 삭제 메소드
    public String delete(MemberState memberState) {
        //memberState.getNo; // 나중에 만들어질 member_table의 pk값
        memberState.setMemNo(1);
        adminRepository.deleteMember(memberState);
        return "회원 삭제 성공";
    }
    // 화원 정보 수정 메소드
    public String modify(MemberState memberState) {
        adminRepository.modifyMember(memberState);
        return "회원 수정 성공";

    }
    // 회원 상태 변경 메소드
    public String stateChange(MemberState memberState) {
        adminRepository.stateChange(memberState);
        return "회원 상태 변경 성공";
    }

    // 회원 리스트 반환 메소드
    public List<MemberState> searchMember(String memName){
        List<MemberState> memList =adminRepository.getMemberList(memName);
        if(memList.isEmpty()){
            throw new DataNotFoundException();
        }else{
            return memList;
        }
    }

}
