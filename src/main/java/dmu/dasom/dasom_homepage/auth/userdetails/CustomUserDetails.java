package dmu.dasom.dasom_homepage.auth.userdetails;

import dmu.dasom.dasom_homepage.domain.member.DasomMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final DasomMember member;


    public CustomUserDetails(DasomMember dasomMember) {
        this.member = dasomMember;
    }

    // 사용자 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getMemRole();
            }
        });
        return collection;
    }

    // 사용자 비밀번호 반환
    @Override
    public String getPassword() {
        return member.getMemPassword();
    }

    // 사용자 아이디 (여기서는 이메일) 반환
    @Override
    public String getUsername() {
        return member.getMemEmail();
    }

    // 아래는 계정 상태에 대한 설정. 차후 구현.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
