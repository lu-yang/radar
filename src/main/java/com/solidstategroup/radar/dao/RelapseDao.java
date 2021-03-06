package com.solidstategroup.radar.dao;

import com.solidstategroup.radar.model.sequenced.Relapse;

import java.util.List;

public interface RelapseDao {

    void saveRelapse(Relapse relapse);

    void deleteRelapse(Relapse relapse);

    Relapse getRelapse(long id);

    List<Relapse> getRelapsesByRadarNumber(long radarNumber);

}
