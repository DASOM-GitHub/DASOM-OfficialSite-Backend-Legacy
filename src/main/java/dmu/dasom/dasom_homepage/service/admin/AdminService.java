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

    //회원 리스트 반환
    public List<MemberState> getMemberList(){
        List<MemberState> memberList = adminRepository.getMemberListAll();
        if(memberList.isEmpty()){
            throw new DataNotFoundException();
        }else{
            return memberList;
        }
    }
    //회원 삭제 메소드
    public void delete(int memNo){
        if (adminRepository.existByMemNo(memNo)) {
            adminRepository.deleteMember(memNo);
        } else {
            throw new DataNotFoundException();
        }
    }

    // 회원 정보 수정 메소드
    public void modify(int memNo, MemberState memberState)  {
        if (adminRepository.existByMemNo(memNo)) {
            memberState.setMemNo(memNo);
            adminRepository.modifyMember(memberState);
        } else {
            throw new DataNotFoundException();
        }
    }

    // 회원 상태 변경 메소드
    public void stateChange(int memNo,MemberState memberState) {
        if (adminRepository.existByMemNo((memNo))) {
            memberState.setMemNo(memNo);
            adminRepository.stateChange(memberState);
        } else {
            throw new DataNotFoundException();
        }
    }

    // 회원 검색 결과 리스트 반환 메소드
    public List<MemberState> searchMember(String keyword){
        List<MemberState> memList = adminRepository.searchMembersByKeyword(keyword);
        if (memList.isEmpty())
            throw new DataNotFoundException();
        return memList;
    }

}
