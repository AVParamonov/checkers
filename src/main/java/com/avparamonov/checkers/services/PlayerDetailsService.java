package com.avparamonov.checkers.services;

import com.avparamonov.checkers.model.db.entity.Player;
import com.avparamonov.checkers.model.db.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
public class PlayerDetailsService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Player player = playerRepository.findByNickname(nickname);
        if (player == null) {
            throw new UsernameNotFoundException("Player with nickname: " + nickname + " not found.");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(player.getRole().name()));

        return new org.springframework.security.core.userdetails.User(player.getNickname(), player.getPassword(), player.isActive(), true, true, true, grantedAuthorities);
    }

}
