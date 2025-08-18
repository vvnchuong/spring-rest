package com.jobweb.job.service;

import com.jobweb.job.domain.Company;
import com.jobweb.job.domain.Role;
import com.jobweb.job.domain.User;
import com.jobweb.job.domain.dto.request.UserCreationRequest;
import com.jobweb.job.domain.dto.request.UserUpdateRequest;
import com.jobweb.job.domain.dto.response.UserResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.mapper.UserMapper;
import com.jobweb.job.repository.CompanyRepository;
import com.jobweb.job.repository.RoleRepository;
import com.jobweb.job.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       CompanyRepository companyRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public Page<UserResponse> getAllUsers(Specification<User> spec, Pageable pageable){
        return userRepository.findAll(spec, pageable)
                .map(userMapper::toUserResponse);
    }

    public UserResponse getUserById(long userId){
        return userMapper.toUserResponse(userRepository.findById(userId).get());
    }

    public UserResponse createUser(UserCreationRequest request){
        Optional<User> user = userRepository.findUserByEmail(request.getEmail());
        Role role = roleRepository.findById(request.getRole().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        if(user.isPresent())
            throw new RuntimeException("User is existed");
        User newUser = userMapper.toUser(request);
        newUser.setRole(role);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setCreatedAt(Instant.now());

        if (request.getCompany() != null && request.getCompany().getId() > 0) {
            Company company = companyRepository.findById(request.getCompany().getId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            newUser.setCompany(company);
        }

        userRepository.save(newUser);

        return userMapper.toUserResponse(newUser);
    }

    public UserResponse updateUser(long id, UserUpdateRequest request){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            throw new RuntimeException("User is not existed");
        userMapper.updateUser(user.get(), request);

        return userMapper.toUserResponse(userRepository.save(user.get()));
    }

    public void deleteUser(long id){
        userRepository.deleteById(id);
    }


}
