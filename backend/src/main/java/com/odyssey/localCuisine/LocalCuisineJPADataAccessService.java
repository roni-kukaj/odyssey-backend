package com.odyssey.localCuisine;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("localCuisineJPAService")
public class LocalCuisineJPADataAccessService implements LocalCuisineDao {
    private final LocalCuisineRepository localCuisineRepository;

    public LocalCuisineJPADataAccessService(LocalCuisineRepository localCuisineRepository) {
        this.localCuisineRepository = localCuisineRepository;
    }

    @Override
    public List<LocalCuisine> selectAllLocalCuisines() {
        return localCuisineRepository.findAll();
    }

    @Override
    public Optional<LocalCuisine> selectLocalCuisineById(Integer id) {
        return localCuisineRepository.findById(id);
    }

    @Override
    public List<LocalCuisine> selectLocalCuisinesByLocationId(Integer locationId) {
        return localCuisineRepository.findByLocationId(locationId);
    }

    @Override
    public void insertLocalCuisine(LocalCuisine localCuisine) {
        localCuisineRepository.save(localCuisine);
    }

    @Override
    public void updateLocalCuisine(LocalCuisine localCuisine) {
        localCuisineRepository.save(localCuisine);
    }

    @Override
    public boolean existsLocalCuisineById(Integer id) {
        return localCuisineRepository.existsLocalCuisineById(id);
    }

    @Override
    public boolean existsLocalCuisineByNameAndLocationId(String name, Integer locationId) {
        return localCuisineRepository.existsLocalCuisineByNameAndLocationId(name, locationId);
    }

    @Override
    public void deleteLocalCuisineById(Integer id) {
        localCuisineRepository.deleteById(id);
    }


}
