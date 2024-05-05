package uz.pdp.backend.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<E, T> {
    Boolean save(E e);
    Boolean update(E e);
    List<E> findAll();
    Optional<E> findById(T id);
}
