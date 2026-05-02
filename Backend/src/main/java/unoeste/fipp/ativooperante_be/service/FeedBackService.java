package unoeste.fipp.ativooperante_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.ativooperante_be.entity.Denuncia;
import unoeste.fipp.ativooperante_be.entity.FeedBack;
import unoeste.fipp.ativooperante_be.repository.FeedBackRepository;

import java.util.List;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackRepository feedRepo;

    public FeedBack salvarFeedBack(FeedBack elemento) {
        return feedRepo.save(elemento);
    }

    public FeedBack atualizarFeedBack(FeedBack novo) {
        FeedBack elemento = null;
        try {
            elemento = feedRepo.findById(novo.getFee_id()).orElse(null);
            if (elemento != null) {
                elemento.setFee_texto(novo.getFee_texto());
                elemento.setDenuncia(novo.getDenuncia());
                feedRepo.save(elemento);
            }
        } catch (Exception e) {
            return null;
        }
        return elemento;
    }

    public boolean deletarFeedBack(Long id) {
        try {
            FeedBack feed = feedRepo.findById(id).orElse(null);
            if (feed != null) {
                feedRepo.delete(feed);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public List<FeedBack> getAll() {
        return feedRepo.findAll();
    }

    public FeedBack getFeedBackId(Long id) {
        return feedRepo.findById(id).orElse(null);
    }

    public FeedBack getByDenunciaId(Long id) {
        List<FeedBack> feedbacks = feedRepo.findByDenuncia(new Denuncia(id, "", "", 0, null, null, null, null, null));
        return feedbacks.isEmpty() ? null : feedbacks.get(0);
    }
}
