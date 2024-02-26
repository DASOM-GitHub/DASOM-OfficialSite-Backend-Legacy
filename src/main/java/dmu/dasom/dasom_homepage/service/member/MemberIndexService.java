package dmu.dasom.dasom_homepage.service.member;

import dmu.dasom.dasom_homepage.domain.member.DasomMemberIndex;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberIndexService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberIndexService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<DasomMemberIndex> getMemberIndexList() {
        List<DasomMemberIndex> memberIndexList = memberRepository.getMemberIndexList();
        if (memberIndexList.isEmpty())
            throw new DataNotFoundException();
        return memberIndexList;
    }

}
