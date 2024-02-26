package dmu.dasom.dasom_homepage.auth.userdetails;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import dmu.dasom.dasom_homepage.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DasomMember member = memberRepository.getMemberByEmail(username);

        // 해당하는 사용자 정보가 있을 경우
        if (member != null) {
            return new CustomUserDetails(member);
        } else {
            return null;
        }

    }
}
