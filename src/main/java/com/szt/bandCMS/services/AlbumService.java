package com.szt.bandCMS.services;

import com.szt.bandCMS.models.Album;
import com.szt.bandCMS.repositories.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> getAlbums() {
        return albumRepository.findAll();
    }

    public Optional<Album> getAlbumByName(String album) {
        return albumRepository.findByTitle(album);
    }
}
