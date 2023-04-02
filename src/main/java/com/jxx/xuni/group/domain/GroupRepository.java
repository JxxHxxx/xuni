package com.jxx.xuni.group.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GroupRepository extends Repository<Group, Long> {
    Optional<Group> findById(Long id);
    Group save(Group group);
}
