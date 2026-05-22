package at.ripet.first_app.service;

import at.ripet.first_app.entity.Branch;
import at.ripet.first_app.repository.BranchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository repository;

    @Transactional
    public List<Branch> findAll() {
        return repository.findAll();
    }

    public void save(Branch branch) {
        repository.save(branch);
    }

    public void delete(Branch branch) {
        repository.delete(branch);
    }
}
