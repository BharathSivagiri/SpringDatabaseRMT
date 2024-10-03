package com.bharathsivaraman.SpringDatabaseRMT.services.implementation;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.entities.PetDiet;
import com.bharathsivaraman.SpringDatabaseRMT.enums.DBRecordStatus;
import com.bharathsivaraman.SpringDatabaseRMT.enums.PetDietStatus;
import com.bharathsivaraman.SpringDatabaseRMT.enums.PetStatus;
import com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom.DataNotFoundException;
import com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom.DateInvalidException;
import com.bharathsivaraman.SpringDatabaseRMT.mapper.PetDietMapper;
import com.bharathsivaraman.SpringDatabaseRMT.mapper.PetMapper;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietWithPetInfoModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.repo.PetDietRepository;
import com.bharathsivaraman.SpringDatabaseRMT.repo.PetRepository;
import com.bharathsivaraman.SpringDatabaseRMT.services.PetService;
import com.bharathsivaraman.SpringDatabaseRMT.utility.DateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //@Service is a specialization of the @Component annotation that indicates that it's a service class.
@RequiredArgsConstructor //@RequiredArgsConstructor is a Lombok annotation that generates a constructor with required arguments for all final fields in the class.
public class PetServiceImpl implements PetService
{
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    private final PetDietRepository petDietRepository;
    private final PetDietMapper petDietMapper;

    //Pet API Service Implementation

    @Override
    public PetModel createPet(PetModel petModel)
    {
        Pet pet = petMapper.toEntity(petModel);
        Pet savedPet = petRepository.save(pet);
        return petMapper.toModel(savedPet);
    }

    @Override
    public PetModel getPetById(Long id)
    {
        Pet pet = petRepository.findById((long) Math.toIntExact(id))
                .orElseThrow(() -> new DataNotFoundException("Pet not found with id: " + id));
        return petMapper.toModel(pet);
    }

    @Override
    public List<PetModel> getAllPets()
    {
        return petRepository.findAll().stream()
                .map(petMapper::toModel)
                .collect(Collectors.toList());
    }
    @Override
    public PetModel updatePet(Long id, PetModel petModel)
    {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pet not found with id: " + id));

        existingPet.setName(petModel.getName());
        existingPet.setType(petModel.getType());
        existingPet.setOwnerName(petModel.getOwnerName());
        existingPet.setPrice(Double.valueOf(petModel.getPrice()));
        existingPet.setStatus(PetStatus.valueOf(petModel.getStatus().toUpperCase()));
        existingPet.setRecStatus(DBRecordStatus.valueOf(petModel.getRecStatus().toUpperCase()));

        try {
            existingPet.setBirthDate(DateUtils.convertToDate(petModel.getBirthDate()));
            existingPet.setCreatedDate(DateUtils.convertToDate(petModel.getCreatedDate()));
            existingPet.setUpdatedDate(DateUtils.convertToDate(petModel.getUpdatedDate()));
        } catch (DateTimeParseException e) {
            throw new DateInvalidException("Invalid date format. Please use yyyyMMdd format.");
        }

        Pet updatedPet = petRepository.save(existingPet);
        return petMapper.toModel(updatedPet);
    }

    @Override
    public PetModel deletePet(Long id)
    {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pet not found with id: " + id));
        PetModel petModel = petMapper.toModel(pet);
        petRepository.deleteById(id);
        return petModel;
    }

    //Pet Diet API Service Implementation

    @Override
    public PetDietModel createPetDiet(PetDietModel petDietModel)
    {
       Optional<Pet> petOptional = petRepository.findById(Integer.parseInt(petDietModel.getId()));
        if (petOptional.isEmpty())
        {
            throw new IllegalArgumentException("Pet not found with id: " + petDietModel.getId());
        }
        PetDiet petDiet = petDietMapper.toDEntity(petDietModel,petOptional.get());
        PetDiet savedPetDiet = petDietRepository.save(petDiet);
        return petDietMapper.toDModel(savedPetDiet);
    }

    @Override
    public PetDietModel getPetDietById(Long dietId)
    {
        PetDiet petDiet = petDietRepository.findById(dietId)
                .orElseThrow(() -> new DataNotFoundException("Pet Diet not found with id: " + dietId));
        return petDietMapper.toDModel(petDiet);
    }

    @Override
    public List<PetDietModel> getAllPetDiets()
    {
        return petDietRepository.findAll().stream()
                .map(petDietMapper::toDModel)
                .collect(Collectors.toList());
    }

    @Override
    public PetDietModel updatePetDiet(Long dietId, PetDietModel petDietModel)
    {
        PetDiet existingPetDiet = petDietRepository.findById(dietId)
                .orElseThrow(() -> new DataNotFoundException("Pet Diet not found with id: " + dietId));

        existingPetDiet.setDietName(petDietModel.getDietName());
        existingPetDiet.setDescription(petDietModel.getDescription());
        existingPetDiet.setDiet(PetDietStatus.valueOf(petDietModel.getDiet().toUpperCase()));
        existingPetDiet.setRecStatus(DBRecordStatus.valueOf(petDietModel.getRecStatus().toUpperCase()));

        try
        {
            existingPetDiet.setStartDate(DateUtils.convertToDate(petDietModel.getStartDate()));
            existingPetDiet.setEndDate(DateUtils.convertToDate(petDietModel.getEndDate()));
            existingPetDiet.setCreatedDietDate(DateUtils.convertToDate(petDietModel.getCreatedDietDate()));
            existingPetDiet.setUpdatedDietDate(DateUtils.convertToDate(petDietModel.getUpdatedDietDate()));
        }
        catch (DateTimeParseException e)
        {
            throw new DateInvalidException("Invalid date format. Please use yyyyMMdd format.");
        }

        PetDiet updatedPetDiet = petDietRepository.save(existingPetDiet);
        return petDietMapper.toDModel(updatedPetDiet);
    }

    @Override
    public PetDietModel deletePetDiet(Long dietId)
    {
        PetDiet petDiet = petDietRepository.findById(dietId)
                .orElseThrow(() -> new DataNotFoundException("Pet Diet not found with id: " + dietId));
        PetDietModel petDietModel = petDietMapper.toDModel(petDiet);
        petDietRepository.deleteById(dietId);
        return petDietModel;
    }

    //Pet ID displays all info from two tables

    @Override
    public List<PetDietWithPetInfoModel> getPetsWithDiet(Long id, LocalDate startDate, LocalDate endDate)
    {
        List<Pet> pets = (id != null)
                ? Collections.singletonList(petRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pet not found with id: " + id)))
                : petRepository.findAll();

        LocalDate effectiveStartDate = startDate != null ? startDate : LocalDate.MIN;
        LocalDate effectiveEndDate = endDate != null ? endDate : LocalDate.MAX;

        return pets.stream()
                .flatMap(pet -> {
                    List<PetDiet> petDiets = petDietRepository.findByPet(pet).stream()
                            .filter(diet -> diet.getRecStatus() == DBRecordStatus.ACTIVE
                                    && !diet.getStartDate().isAfter(effectiveEndDate)
                                    && !diet.getEndDate().isBefore(effectiveStartDate))
                            .toList();
                    return petDiets.stream()
                            .map(diet -> new PetDietWithPetInfoModel(
                                    pet.getId(),
                                    pet.getName(),
                                    petDietMapper.toDModel(diet)
                            ));
                })
                .collect(Collectors.toList());
    }

    //Pet Diet data record status

    @Override
    public List<PetDietModel> getAllPetDiets(String status)
    {
        List<PetDiet> petDiets;
        if (status == null || status.equalsIgnoreCase("all"))
        {
            petDiets = petDietRepository.findAll();
        }
        else if (status.equalsIgnoreCase("active"))
        {
            petDiets = petDietRepository.findByRecStatus(DBRecordStatus.ACTIVE);
        }
        else if (status.equalsIgnoreCase("inactive"))
        {
            petDiets = petDietRepository.findByRecStatus(DBRecordStatus.INACTIVE);
        }
        else
        {
            throw new IllegalArgumentException("Invalid status. Use 'all', 'active', or 'inactive'.");
        }

        return petDiets.stream()
                .map(petDietMapper::toDModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Pet> getPetsWithPagingAndSorting(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String startingLetter) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Pet> petPage = petRepository.findAll(pageRequest);
        List<Pet> allPets = petPage.getContent();

        if (startingLetter != null && !startingLetter.isEmpty()) {
            allPets = allPets.stream()
                    .filter(pet -> pet.getName().toLowerCase().startsWith(startingLetter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), allPets.size());
        List<Pet> pageContent = allPets.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, allPets.size());
    }

}
