package com.kammradt.learning.service;

import com.kammradt.learning.domain.User;
import com.kammradt.learning.exception.NotFoundException;
import com.kammradt.learning.model.PageModel;
import com.kammradt.learning.model.PageRequestModel;
import com.kammradt.learning.repository.UserRepository;
import com.kammradt.learning.service.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.kammradt.learning.service.util.HashUtil.generateHash;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        String hashedPassword = generateHash(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public User update(User user) {
        String hashedPassword = generateHash(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        Optional<User> result = userRepository.findById(id);
        return result.orElseThrow(() -> new NotFoundException("There are no User with this ID"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public PageModel<User> listAllOnLazyMode(PageRequestModel pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
        Page<User> resultPage = userRepository.findAll(pageable);

        return new PageModel<>(
                  (int) resultPage.getTotalElements(),
                        resultPage.getSize(),
                        resultPage.getTotalPages(),
                        resultPage.getContent());
    }

    public User login(String email, String password) {
        password = generateHash(password);
        Optional<User> result = userRepository.login(email, password);
        return result.orElse(null);
    }


}
