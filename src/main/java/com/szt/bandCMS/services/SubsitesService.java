package com.szt.bandCMS.services;

import com.szt.bandCMS.models.Subsite;
import com.szt.bandCMS.repositories.SubsiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubsitesService {
    final SubsiteRepository subsiteRepository;

    public SubsitesService(SubsiteRepository subsiteRepository) {
        this.subsiteRepository = subsiteRepository;
    }

    public List<Subsite> getSubsites() {
        return subsiteRepository.findAll();
    }

    public  List<Subsite> getHeadSubsites() {
        return subsiteRepository.findAllByOrderByPositionAsc()
                .stream()
                .filter(subsite->subsite.getParent() == null)
                .collect(Collectors.toList());
    }
}
