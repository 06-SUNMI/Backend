package capstone.everyhealth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import capstone.everyhealth.domain.sns.SnsPost;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsRepository extends JpaRepository<SnsPost, Long>{


}
