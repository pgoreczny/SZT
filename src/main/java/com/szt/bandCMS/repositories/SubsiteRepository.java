package com.szt.bandCMS.repositories;


import com.szt.bandCMS.models.Subsite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubsiteRepository extends CrudRepository<Subsite, String> {
    List<Subsite> findAll();

    List<Subsite> findAllByOrderByPositionAsc();

    @Query("select max(s.position) from Subsite s")
    int findMaxPosition();

    long deleteByParent(String parent);
}
