package dmu.dasom.dasom_homepage.service.admin;

import dmu.dasom.dasom_homepage.domain.admin.MemberState;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    //회원 삭제 메소드
    public void delete(int memNo){
        adminRepository.deleteMember(memNo);
    }

    // 회원 정보 수정 메소드
    public void modify(int memNo,MemberState memberState) throws Exception {
        if (adminRepository.existByMemNo(memNo)) {
            adminRepository.modifyMember(memberState);
        } else {
            throw new Exception();
        }
    }

    // 회원 상태 변경 메소드
    public void stateChange(int memNo,MemberState memberState) {
        memberState.setMemNo(memNo);
        adminRepository.stateChange(memberState);
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
