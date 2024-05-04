package com.odyssey.localCuisine;



import java.util.List;
import java.util.Optional;

public interface LocalCuisineDao {
    List<LocalCuisine> selectAllLocalCuisines();
    Optional<LocalCuisine> selectLocalCuisineById(Integer Id);
    List<LocalCuisine> selectLocalCuisinesByLocationId(Integer locationId);
    void insertLocalCuisine(LocalCuisine localCuisine);
    void updateLocalCuisine(LocalCuisine localCuisine);
    boolean existsLocalCuisineById(Integer Id);
    boolean existsLocalCuisineByNameAndLocationId(String name, Integer locationId);
    void deleteLocalCuisineById(Integer id);

}
