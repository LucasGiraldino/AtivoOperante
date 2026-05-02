package unoeste.fipp.ativooperante_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.ativooperante_be.entity.Orgaos;
import unoeste.fipp.ativooperante_be.repository.OrgaosRepository;

import java.util.List;

@Service
public class OrgaosService {

    @Autowired
    private OrgaosRepository orgaoRepo;

    public Orgaos salvarOrgao(Orgaos elemento) {
        try {
            return orgaoRepo.save(elemento);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Orgaos> getAllOrgaos() {
        return orgaoRepo.findAll();
    }

    public Orgaos getOrgaoId(Long id) {
        return orgaoRepo.findById(id).orElse(null);
    }

    public boolean apagarOrgao(Orgaos elemento) {
        Orgaos orgao = orgaoRepo.findById(elemento.getOrg_id()).orElse(null);
        try {
            if (orgao != null) {
                orgaoRepo.delete(orgao);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public Orgaos atualizarOrgao(Orgaos novo) {
        try {
            Orgaos elemento = orgaoRepo.findById(novo.getOrg_id()).orElse(null);
            if (elemento != null) {
                elemento.setOrg_nome(novo.getOrg_nome());
                return orgaoRepo.save(elemento);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
