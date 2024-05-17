package com.odyssey.localCuisine;


import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/local-cuisine")
public class LocalCuisineController {

    @Autowired
    private LocalCuisineService localCuisineService;

    @GetMapping
    public List<LocalCuisine> getAllLocalCuisines() {
        return localCuisineService.getAllLocalCuisines();
    }

    @GetMapping("/{localCuisineId}")
    public LocalCuisine getLocalCuisineById(@PathVariable("localCuisineId") Integer localCuisineId) {
        return localCuisineService.getLocalCuisine(localCuisineId);
    }

    @GetMapping("/location/{locationId}")
    public List<LocalCuisine> getLocalCuisinesByLocationId(@PathVariable("locationId") Integer locationId) {
        return localCuisineService.getLocalCuisinesByLocationId(locationId);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void registerLocalCuisine(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestPart("image") MultipartFile image,
            @RequestParam("locationId") Integer locationId
    ) {
        localCuisineService.addLocalCuisine(new LocalCuisineRegistrationDto(
                name, description, image, locationId
        ));
    }

    @DeleteMapping("/{localCuisineId}")
    public void deleteLocalCuisine(@PathVariable("localCuisineId") Integer localCuisineId) {
        localCuisineService.deleteLocalCuisine(localCuisineId);
    }

    @PutMapping("/{localCuisineId}")
    public void updateLocalCuisine(
            @PathVariable("localCuisineId") Integer localCuisineId,
            @RequestBody LocalCuisineUpdateInformationDto dto) {
        localCuisineService.updateLocalCuisineInformation(localCuisineId, dto);
    }

    @PutMapping(value = "/{localCuisineId}/picture", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void updateLocalCuisinePicture(
            @PathVariable("localCuisineId") Integer localCuisineId,
            @RequestParam("file") MultipartFile file
    ) {
        localCuisineService.updateLocalCuisinePicture(localCuisineId, file);
    }


}
