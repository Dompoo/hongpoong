package Dompoo.Hongpoong.config.security;

import Dompoo.Hongpoong.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long memberId;

    public UserPrincipal (Member member) {
        super(member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        memberId = member.getId();
    }
}
