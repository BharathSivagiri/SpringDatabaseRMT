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
import com.bharathsivaraman.SpringDatabaseRMT.services.LogService;
import com.bharathsivaraman.SpringDatabaseRMT.services.PetService;
import com.bharathsivaraman.SpringDatabaseRMT.utility.DateUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import java.util.*;
import java.util.stream.Collectors;

@Service //@Service is a specialization of the @Component annotation that indicates that it's a service class.
@RequiredArgsConstructor //@RequiredArgsConstructor is a Lombok annotation that generates a constructor with required arguments for all final fields in the class.
public class PetServiceImpl implements PetService
{
    @Autowired
    private final PetRepository petRepository;

    @Autowired
    private final PetMapper petMapper;

    @Autowired
    private final PetDietRepository petDietRepository;

    @Autowired
    private final PetDietMapper petDietMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine tempEngine;

    @Autowired
    private final LogService logService;

    @PersistenceContext
    private EntityManager entityManager;

    //Pet API Service Implementation

    @Autowired
    private Validator validator;

    @Override
    public PetModel createPet(PetModel petModel)
    {
        Pet pet = petMapper.toEntity(petModel);
        Pet savedPet = petRepository.save(pet);

        logService.logApiCall(
                petModel.getOwnerName(),
                "API Pet Creation",
                "Created Successfully",
                ZonedDateTime.now()
        );
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

        logService.logApiCall(
                petModel.getOwnerName(),
                "API Pet Update",
                "Updated Successfully",
                ZonedDateTime.now()
        );

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
        Long id = Long.valueOf(petDietModel.getId());
        Optional<Pet> petOptional = petRepository.findById(id);
        if (petOptional.isEmpty())
        {
            throw new IllegalArgumentException("Pet not found with id: " + id);
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
    public Page<PetModel> getPetsWithPagingAndSorting(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String startingLetter, String searchTerm, String searchTermOwner)
    {
        clearHibernateCache();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> query = cb.createQuery(Pet.class);
        Root<Pet> pet = query.from(Pet.class);

        List<Predicate> predicates = new ArrayList<>();
        if (startingLetter != null && !startingLetter.isEmpty()) {
            predicates.add(cb.like(cb.lower(pet.get("name")), startingLetter.toLowerCase() + "%"));
        }

        if (searchTerm != null && !searchTerm.isEmpty()) {
            predicates.add(cb.like(cb.lower(pet.get("type")), searchTerm.toLowerCase() + "%"));
        }

        if (searchTermOwner != null && !searchTermOwner.isEmpty()) {
            predicates.add(cb.like(cb.lower(pet.get("ownerName")), searchTermOwner.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(sort.stream()
                .map(order -> order.isAscending() ? cb.asc(pet.get(order.getProperty())) : cb.desc(pet.get(order.getProperty())))
                .collect(Collectors.toList()));

        List<Pet> pets = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<PetModel> petModels = pets.stream()
                .map(petMapper::toModel)
                .collect(Collectors.toList());

        return new PageImpl<>(petModels, pageable, petModels.size());
    }

    @Override
    public List<PetModel> searchPets(String name, String type, String ownerName)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> query = cb.createQuery(Pet.class);
        Root<Pet> pet = query.from(Pet.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(pet.get("name")),name.toLowerCase() + "%"));
        }
        if (type != null && !type.isEmpty()) {
            predicates.add(cb.like(cb.lower(pet.get("type")),type.toLowerCase() + "%"));
        }
        if (ownerName != null && !ownerName.isEmpty()) {
            predicates.add(cb.like(cb.lower(pet.get("ownerName")),ownerName.toLowerCase() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));

        List<Pet> pets = entityManager.createQuery(query).getResultList();
        return pets.stream().map(petMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void sendPetInfoEmail(Long id)
    {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));

        List<PetDiet> petDiet = petDietRepository.findByPetId(id);

        Context context = new Context();
        context.setVariable("name", pet.getName());
        context.setVariable("type", pet.getType());
        context.setVariable("ownerName", pet.getOwnerName());
        context.setVariable("ownerEmail", pet.getOwnerEmail());
        context.setVariable("petDiet", petDiet);

        String emailContent = tempEngine.process("pet-diet-email", context);

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper help = new MimeMessageHelper(mailMessage);

        try
        {
            help.setTo(pet.getOwnerEmail());
            help.setSubject("Pet Diet Information");
            help.setText(emailContent, true);
            mailSender.send(mailMessage);
        }
         catch (MessagingException e)
         {
             throw new RuntimeException("Failed to send email", e);
         }
    }

    @Value("${scheduler.enabled}")
    private boolean isSchedulerEnabled;

    @Scheduled(cron = "${scheduler.cron}")
    public void scheduleDailyPetInfoEmail() {
        if (isSchedulerEnabled) {
            List<Pet> allPets = petRepository.findAll();
            for (Pet pet : allPets) {
                sendPetInfoEmail((long) pet.getId());
            }
        }
    }

    public void clearHibernateCache()
    {
        entityManager.clear();
    }
}



