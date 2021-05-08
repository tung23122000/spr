package dts.com.vn.service;

import dts.com.vn.entities.Account;
import dts.com.vn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<Account> getAllUser() {
        List<Account> list = userRepository.findAll();
        return list;
    }
}
