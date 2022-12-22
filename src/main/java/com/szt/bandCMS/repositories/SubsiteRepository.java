package com.szt.bandCMS.repositories;


import com.szt.bandCMS.models.Subsite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubsiteRepository extends CrudRepository<Subsite, Long> {
    List<Subsite> findAll();
    List<Subsite> findAllByOrderByPositionAsc();
}
