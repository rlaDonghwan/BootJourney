package com.BootJourney.Service;

import com.BootJourney.Entity.User;
import com.BootJourney.Exception.DataNotFoundException;
import com.BootJourney.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String email, String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public User getUser(String username) throws DataNotFoundException {
        // 1️⃣ username을 기준으로 User를 조회 (결과는 Optional<User> 형태)
        Optional<User> user = this.userRepository.findByUsername(username);

        // 2️⃣ 조회된 User가 존재하는지 확인
        if(user.isPresent()){
            return user.get(); // ✅ 존재하면 Optional에서 값을 꺼내어 반환
        } else {
            throw new DataNotFoundException("사용자를 찾지 못했습니다."); // ✅ 존재하지 않으면 예외 발생
        }
    }


}
