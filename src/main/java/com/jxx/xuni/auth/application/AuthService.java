package com.jxx.xuni.auth.application;

import com.jxx.xuni.member.domain.LoginInfo;
import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.member.domain.MemberRepository;
import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public void signup(SignupForm signupForm) {
        Member member = new Member(LoginInfo.of(signupForm.getEmail(), signupForm.getPassword()), signupForm.getName());
        checkDuplicationOfEmail(signupForm.getEmail());

        memberRepository.save(member);
    }

    public SimpleMemberDetails login(LoginForm loginForm) {
        Member member = memberRepository.findByLoginInfoEmail(loginForm.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 입니다."));
        member.matches(loginForm.getPassword());

        return new SimpleMemberDetails(member.getId(), member.receiveEmail(), member.getName());
    }

    private void checkDuplicationOfEmail(String email) {
        if (memberRepository.findByLoginInfoEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }
}
