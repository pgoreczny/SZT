package com.szt.bandCMS.repositories;


import com.szt.bandCMS.models.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {
    List<Album> findAll();

    Optional<Album> findByTitle(String album);
}
