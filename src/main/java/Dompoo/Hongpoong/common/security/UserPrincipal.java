package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long memberId;
    private final String username;
    private final String role;

    public UserPrincipal (Member member) {
        super(member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getRole())));

        memberId = member.getId();
        username = member.getUsername();
        role = member.getRole();
    }
}
