package at.ripet.first_app.repository;

import at.ripet.first_app.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {}
