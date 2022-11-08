package capstone.everyhealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import capstone.everyhealth.domain.sns.SnsPost;

public interface SnsRepository extends JpaRepository<SnsPost, Long>{

    
}
