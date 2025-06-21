package com.tp.APP1.dao;

import com.tp.APP1.models.Achat;
import java.util.List;

public interface AchatDAO {
    List<Achat> getAchatsEnAttente();
    void updateStatus(int achatId, String newStatus);
}
