package com.jxx.xuni.auth.application;

import com.jxx.xuni.member.domain.LoginInfo;
import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.member.domain.MemberRepository;
import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.EXISTED_EMAIL;
import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.LOGIN_FAIL;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupForm signupForm) {
        Member member = new Member(LoginInfo.of(signupForm.getEmail(), passwordEncoder.encrypt(signupForm.getPassword())),
                signupForm.getName());

        checkDuplicationOfEmail(signupForm.getEmail());
        memberRepository.save(member);
    }

    public SimpleMemberDetails login(LoginForm loginForm) {
        Member member = memberRepository.findByLoginInfoEmail(loginForm.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(LOGIN_FAIL));

        member.matches(passwordEncoder.encrypt(loginForm.getPassword()));

        return new SimpleMemberDetails(member.getId(), member.receiveEmail(), member.getName(), member.getAuthority());
    }

    private void checkDuplicationOfEmail(String email) {
        if (memberRepository.findByLoginInfoEmail(email).isPresent()) {
            throw new IllegalArgumentException(EXISTED_EMAIL);
        }
    }
}
