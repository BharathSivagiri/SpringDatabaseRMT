package com.bharathsivaraman.SpringDatabaseRMT.services.implementation;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.entities.PetDiet;
import com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom.DataNotFoundException;
import com.bharathsivaraman.SpringDatabaseRMT.mapper.PetDietMapper;
import com.bharathsivaraman.SpringDatabaseRMT.mapper.PetMapper;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.repo.PetDietRepository;
import com.bharathsivaraman.SpringDatabaseRMT.repo.PetRepository;
import com.bharathsivaraman.SpringDatabaseRMT.services.PetService;
import com.bharathsivaraman.SpringDatabaseRMT.utility.DateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeParseException;

import java.util.List;
import java.util.stream.Collectors;

@Service //@Service is a specialization of the @Component annotation that indicates that it's a service class.
@RequiredArgsConstructor //@RequiredArgsConstructor is a Lombok annotation that generates a constructor with required arguments for all final fields in the class.
public class PetServiceImpl implements PetService
{
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    private final PetDietRepository petDietRepository;
    private final PetDietMapper petDietMapper;

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

        try {
            existingPet.setBirthDate(DateUtils.convertToDate(petModel.getBirthDate()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyyMMdd format.", e);
        }

        Pet updatedPet = petRepository.save(existingPet);
        return petMapper.toModel(updatedPet);
    }

    @Override
    public void deletePet(Long id)
    {
        petRepository.deleteById((long) Math.toIntExact(id));
    }

    @Override
    public PetDietModel createPetDiet(Long petId, PetDietModel petDietModel)
    {
        PetDiet petDiet = petDietMapper.toDEntity(petDietModel);
        PetDiet savedPetDiet = petDietRepository.save(petDiet);
        return petDietMapper.toDModel(savedPetDiet);
    }

    @Override
    public PetDietModel getPetDietById(Long petId, Long dietId) {
        return null;
    }

    @Override
    public PetDietModel updatePetDiet(Long petId, Long dietId, PetDietModel petDietModel) {
        return null;
    }

    @Override
    public void deletePetDiet(Long petId, Long dietId) {

    }

}
