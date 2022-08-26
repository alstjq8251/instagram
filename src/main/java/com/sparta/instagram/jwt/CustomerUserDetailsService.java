package com.sparta.instagram.jwt;

import com.sparta.instagram.domain.Member;
import com.sparta.instagram.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByUserId(username);
        if (!member.isPresent()) {
            throw new IllegalArgumentException("존재하지 않습니다.");
        }
        return new Principaldetail(member.get()); // -> userDeatils 로
    }

//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return memberRepository.findByUserId(username)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
//    }
//
//    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
//    private UserDetails createUserDetails(Member member) {
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
//        StringBuilder sb = new StringBuilder();
//        sb.append(member.getUserId()).append(" ").append(member.getUserNic()).append(" ").append(member.getUserName());
//        return new User(
//                sb.toString(),
//                member.getPassword(),
//                Collections.singleton(grantedAuthority)
//        );
//    }
}
