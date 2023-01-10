package com.szt.bandCMS.services;

import com.szt.bandCMS.models.Subsite;
import com.szt.bandCMS.repositories.AlbumRepository;
import com.szt.bandCMS.repositories.SubsiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubsitesService {
    final SubsiteRepository subsiteRepository;
    final AlbumRepository albumRepository;

    public SubsitesService(SubsiteRepository subsiteRepository, AlbumRepository albumRepository) {
        this.subsiteRepository = subsiteRepository;
        this.albumRepository = albumRepository;
    }

    public List<Subsite> getSubsites() {
        return subsiteRepository.findAll();
    }

    public List<Subsite> getHeadSubsites() {
        List<Subsite> subsites = subsiteRepository.findAllByOrderByPositionAsc()
                .stream()
                .filter(subsite -> subsite.getParent() == null)
                .collect(Collectors.toList());
        subsites
                .stream()
                .filter(subsite -> subsite.getName().equals("Albums"))
                .findFirst()
                .get()
                .setChildren(albumRepository.findAll()
                        .stream()
                        .map(album -> new Subsite(album.getCode(), album.getTitle(), album.getLongDesc(),0, String.format("/albums/%d", album.getId(), true)))
                        .collect(Collectors.toList()));
        return subsites;
    }

    public Optional<Subsite> getSubsiteByName(String name) {
        return subsiteRepository.findById(name);
    }

    public List<String>availableParents(String name) {
        List<String>parents = getHeadSubsites()
                .stream()
                .filter(subsite1 -> !subsite1.getName().equals(name))
                .map(subsite1 -> subsite1.getName()).collect(Collectors.toList());
        parents.add("");
        return parents;
    }

    public void save(Subsite subsite) {
        subsiteRepository.save(subsite);
    }

    public void delete(String name) {
        subsiteRepository.deleteById(name);
    }
    public void deleteByParent(String name) {
        subsiteRepository.deleteByParent(name);
    }

    public int getMaxPosition() {
        return subsiteRepository.findMaxPosition();
    }
}
