package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            return Optional.empty();
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return Optional.of(new Account()); // Special flag: username conflict
        }

        return Optional.of(accountRepository.save(account));
    }

    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsername(username)
                .filter(acc -> acc.getPassword().equals(password));
    }

    public boolean exists(Integer accountId) {
        return accountRepository.existsById(accountId);
    }
}
