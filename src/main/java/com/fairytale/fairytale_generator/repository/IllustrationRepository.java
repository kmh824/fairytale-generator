package com.fairytale.fairytale_generator.repository;

import com.fairytale.fairytale_generator.entity.Illustration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IllustrationRepository extends JpaRepository<Illustration, Long> {
}
