package dts.com.vn.service;

import dts.com.vn.entities.ConstantDeclaration;
import dts.com.vn.repository.ConstantDeclarationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConstantDeclarationService {
    private final ConstantDeclarationRepository constantDeclarationRepository;

    public ConstantDeclarationService(ConstantDeclarationRepository constantDeclarationRepository) {
        this.constantDeclarationRepository = constantDeclarationRepository;
    }

    public ConstantDeclaration findByConstantKey(String constantKey){
        return constantDeclarationRepository.findById(constantKey).orElse(null);
    }

    public ConstantDeclaration save(ConstantDeclaration constantDeclaration){
        return constantDeclarationRepository.save(constantDeclaration);
    }
}
