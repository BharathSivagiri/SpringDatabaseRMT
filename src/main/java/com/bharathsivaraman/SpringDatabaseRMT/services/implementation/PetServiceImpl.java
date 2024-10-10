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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    @Value("${scheduler.enabled}")
    private boolean isSchedulerEnabled;

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

    @Override
    public PetModel createPet(PetModel petModel)
    {
        logger.info("Creating a new pet: {}", petModel.getName());

        Pet pet = petMapper.toEntity(petModel);
        Pet savedPet = petRepository.save(pet);

        logger.info("Pet created successfully: {}", savedPet.getName());

        logService.logApiCall(
                petModel.getOwnerName(),
                "Pet Creation",
                "Created Successfully",
                ZonedDateTime.now()
        );
        return petMapper.toModel(savedPet);
    }


    @Override
    public PetModel getPetById(Long id)
    {
        logger.info("Fetching pet with ID: {}", id);

        Pet pet = petRepository.findById((long) Math.toIntExact(id))
                .orElseThrow(() -> {
                    logger.error("Pet not found with id: {}", id);
                    return new DataNotFoundException("Pet not found with id: " + id);
                });

        logger.info("Pet found with id: {}", id);

        logService.logApiCall(
                String.valueOf(id),
                "Pet by ID",
                "Updated Successfully",
                ZonedDateTime.now()
        );

        return petMapper.toModel(pet);
    }

    @Override
    public List<PetModel> getAllPets()
    {
        logger.info("Fetching all pets");

        List<PetModel> pets = petRepository.findAll().stream()
                .map(petMapper::toModel)
                .collect(Collectors.toList());

        logger.info("All pets fetched successfully");

        return pets;
    }
    @Override
    public PetModel updatePet(Long id, PetModel petModel)
    {
        logger.info("Updating pet with ID: {}", id);
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pet not found with id: {}", id);
                    return new DataNotFoundException("Pet not found with id: " + id);
                });

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
        } catch (DateTimeParseException e)
        {
            logger.error("Invalid date format. Please use yyyyMMdd format.");
            throw new DateInvalidException("Invalid date format. Please use yyyyMMdd format.");
        }

        Pet updatedPet = petRepository.save(existingPet);

        logger.info("Pet updated successfully: {}", updatedPet.getName());

        logService.logApiCall(
                petModel.getOwnerName(),
                "Pet Update",
                "Updated Successfully",
                ZonedDateTime.now()
        );

        return petMapper.toModel(updatedPet);
    }

    @Override
    public PetModel deletePet(Long id)
    {
        logger.info("Deleting pet with ID: {}", id);

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pet not found with id: {}", id);
                    return new DataNotFoundException("Pet not found with id: " + id);
                });

        PetModel petModel = petMapper.toModel(pet);
        petRepository.deleteById(id);

        logger.info("Pet deleted successfully: {}", petModel.getName());

        logService.logApiCall(
                petModel.getOwnerName(),
                "Pet Deletion",
                "Deleted Successfully",
                ZonedDateTime.now()
        );
        return petModel;

    }

    //Pet Diet API Service Implementation

    @Override
    public PetDietModel createPetDiet(PetDietModel petDietModel)
    {
        logger.info("Creating a new pet diet: {}", petDietModel.getDietName());

        Long id = Long.valueOf(petDietModel.getId());
        Optional<Pet> petOptional = petRepository.findById(id);
        if (petOptional.isEmpty())
        {
            logger.error("Pet not found with id: " + id);
            throw new IllegalArgumentException("Pet not found with id: " + id);
        }
        PetDiet petDiet = petDietMapper.toDEntity(petDietModel,petOptional.get());
        PetDiet savedPetDiet = petDietRepository.save(petDiet);

        logger.info("Pet diet created successfully: {}", savedPetDiet.getDietName());

        logService.logApiCall(
                petDietModel.getDietName(),
                "Pet Diet Creation",
                "Created Successfully",
                ZonedDateTime.now()
        );

        return petDietMapper.toDModel(savedPetDiet);
    }

    @Override
    public PetDietModel getPetDietById(Long dietId)
    {
        logger.info("Fetching pet diet with ID: {}", dietId);

        PetDiet petDiet = petDietRepository.findById(dietId)
                .orElseThrow(() -> {
                    logger.error("Pet Diet not found with id: {}", dietId);
                    return new DataNotFoundException("Pet Diet not found with id: " + dietId);
                });

        logger.info("Pet diet found with id: {}", dietId);

        return petDietMapper.toDModel(petDiet);
    }

    @Override
    public List<PetDietModel> getAllPetDiets()
    {
        logger.info("Fetching all pet diets");

        List<PetDietModel> petdiets =  petDietRepository.findAll().stream()
                .map(petDietMapper::toDModel)
                .collect(Collectors.toList());

        logger.info("All pet diets fetched successfully");
        return petdiets;
    }

    @Override
    public PetDietModel updatePetDiet(Long dietId, PetDietModel petDietModel)
    {
        logger.info("Updating pet diet with ID: {}", dietId);

        PetDiet existingPetDiet = petDietRepository.findById(dietId)
                .orElseThrow(() -> {
                    logger.error("Pet Diet not found with id: {}", dietId);
                    return new DataNotFoundException("Pet Diet not found with id: " + dietId);
                });

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
            logger.error("Invalid date format. Please use yyyyMMdd format.");
            throw new DateInvalidException("Invalid date format. Please use yyyyMMdd format.");
        }

        PetDiet updatedPetDiet = petDietRepository.save(existingPetDiet);

        logger.info("Pet diet updated successfully: {}", updatedPetDiet.getDietName());

        logService.logApiCall(
                updatedPetDiet.getDietName(),
                "Pet Diet Update",
                "Updated Successfully",
                ZonedDateTime.now()
        );

        return petDietMapper.toDModel(updatedPetDiet);
    }

    @Override
    public PetDietModel deletePetDiet(Long dietId)
    {
        logger.info("Deleting pet diet with ID: {}", dietId);

        PetDiet petDiet = petDietRepository.findById(dietId)
                .orElseThrow(() -> {
                        logger.error("Pet Diet not found with id: {}", dietId);
                        return new DataNotFoundException("Pet Diet not found with id: " + dietId);
                });
        PetDietModel petDietModel = petDietMapper.toDModel(petDiet);
        petDietRepository.deleteById(dietId);

        logger.info("Pet diet deleted successfully: {}", petDietModel.getDietName());

        logService.logApiCall(
                petDietModel.getDietName(),
                "Pet Diet Deletion",
                "Deleted Successfully",
                ZonedDateTime.now()
        );

        return petDietModel;
    }

    //Pet ID displays all info from two tables

    @Override
    public List<PetDietWithPetInfoModel> getPetsWithDiet(Long id, LocalDate startDate, LocalDate endDate)
    {
        logger.info("Fetching pets with diet information for pet ID: {}", id);

        List<Pet> pets = (id != null)
                ? Collections.singletonList(petRepository.findById(id)
                .orElseThrow(() -> {
                        logger.error("Pet not found with id: " + id);
                        return new DataNotFoundException("Pet not found with id: " + id);
                })) : petRepository.findAll();

        logger.info("Found {} pets with diet information", pets.size());

        LocalDate effectiveStartDate = startDate != null ? startDate : LocalDate.MIN;
        LocalDate effectiveEndDate = endDate != null ? endDate : LocalDate.MAX;

        logger.info("Effective date range : {} to {}", effectiveStartDate, effectiveEndDate);

        logger.info("Fetched {} pet diets for pets within date range" , pets.size());

        logService.logApiCall(
                "System",
                "Pet with Pet diet",
                "Fetched Successfully",
                ZonedDateTime.now()
        );
        return pets.stream()
                .flatMap(pet -> {
                    logger.info("Processing pet with ID: {}", pet.getId());
                    List<PetDiet> petDiets = petDietRepository.findByPet(pet).stream()
                            .filter(diet -> diet.getRecStatus() == DBRecordStatus.ACTIVE
                                    && !diet.getStartDate().isAfter(effectiveEndDate)
                                    && !diet.getEndDate().isBefore(effectiveStartDate))
                            .toList();
                    logger.info("Found {} diets for pet with ID: {}", petDiets.size(), pet.getId());
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
        logger.info("Fetching pet diets with status: {}", status);
        List<PetDiet> petDiets;
        if (status == null || status.equalsIgnoreCase("all"))
        {
            logger.info("Fetching all pet diets");
            petDiets = petDietRepository.findAll();
        }
        else if (status.equalsIgnoreCase("active"))
        {
            logger.info("Fetching active pet diets");
            petDiets = petDietRepository.findByRecStatus(DBRecordStatus.ACTIVE);
        }
        else if (status.equalsIgnoreCase("inactive"))
        {
            logger.info("Fetching inactive pet diets");
            petDiets = petDietRepository.findByRecStatus(DBRecordStatus.INACTIVE);
        }
        else
        {
            logger.error("Invalid status: {}", status);
            throw new IllegalArgumentException("Invalid status. Use 'all', 'active', or 'inactive'.");
        }

        logger.info("Fetched {} pet diets", petDiets.size());

        logService.logApiCall(
                "System",
                "Pet Diet Record Status",
                "Fetched Successfully",
                ZonedDateTime.now()
        );

        return petDiets.stream()
                .map(petDietMapper::toDModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PetModel> getPetsWithPagingAndSorting(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String startingLetter, String searchTerm, String searchTermOwner)
    {
        logger.info("Fetching pets with paging and sorting");
        clearHibernateCache();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> query = cb.createQuery(Pet.class);
        Root<Pet> pet = query.from(Pet.class);

        List<Predicate> predicates = new ArrayList<>();
        if (startingLetter != null && !startingLetter.isEmpty())
        {
            logger.info("Fetching pets with starting letter: {}", startingLetter);
            predicates.add(cb.like(cb.lower(pet.get("name")), startingLetter.toLowerCase() + "%"));
        }

        if (searchTerm != null && !searchTerm.isEmpty())
        {
            logger.info("Fetching pets with search term: {}", searchTerm);
            predicates.add(cb.like(cb.lower(pet.get("type")), searchTerm.toLowerCase() + "%"));
        }

        if (searchTermOwner != null && !searchTermOwner.isEmpty())
        {
            logger.info("Fetching pets with search term owner: {}", searchTermOwner);
            predicates.add(cb.like(cb.lower(pet.get("ownerName")), searchTermOwner.toLowerCase() + "%"));
        }

        logger.info("Fetching pets with pageable: {}", pageable);

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

        logger.info("Fetched {} pets", pets.size());

        logService.logApiCall(
                searchTermOwner,
                "Paging and Sorting",
                "Fetched Successfully",
                ZonedDateTime.now()
        );

        return new PageImpl<>(petModels, pageable, petModels.size());
    }

    @Override
    public List<PetModel> searchPets(String name, String type, String ownerName)
    {
        logger.info("Searching pets with name: {}, type: {}, ownerName: {}", name, type, ownerName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> query = cb.createQuery(Pet.class);
        Root<Pet> pet = query.from(Pet.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty())
        {
            logger.info("Searching pets with name: {}", name);
            predicates.add(cb.like(cb.lower(pet.get("name")),name.toLowerCase() + "%"));
        }
        if (type != null && !type.isEmpty())
        {
            logger.info("Searching pets with type: {}", type);
            predicates.add(cb.like(cb.lower(pet.get("type")),type.toLowerCase() + "%"));
        }
        if (ownerName != null && !ownerName.isEmpty())
        {
            logger.info("Searching pets with ownerName: {}", ownerName);
            predicates.add(cb.like(cb.lower(pet.get("ownerName")),ownerName.toLowerCase() + "%"));
        }
        logger.info("Searching pets with predicates: {}", predicates);

        query.where(predicates.toArray(new Predicate[0]));

        List<Pet> pets = entityManager.createQuery(query).getResultList();

        logger.info("Found {} pets", pets.size());

        logService.logApiCall(
                ownerName,
                "Search Pet with Pet Info",
                "Fetched Successfully",
                ZonedDateTime.now()
        );
        return pets.stream().map(petMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void sendPetInfoEmail(Long id)
    {
        logger.info("Sending pet info email for pet with ID: {}", id);

        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));

        List<PetDiet> petDiet = petDietRepository.findByPetId(id);

        logger.info("Found {} pet diets for pet with ID: {}", petDiet.size(), id);

        Context context = new Context();
        context.setVariable("name", pet.getName());
        context.setVariable("type", pet.getType());
        context.setVariable("ownerName", pet.getOwnerName());
        context.setVariable("ownerEmail", pet.getOwnerEmail());
        context.setVariable("petDiet", petDiet);

        logger.info("Created email content for pet with ID: {}", id);

        String emailContent = tempEngine.process("pet-diet-email", context);

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper help = new MimeMessageHelper(mailMessage);

        logger.info("Sending email for pet with ID: {}", id);

        try
        {
            logger.info("Setting email properties for pet with ID: {}", id);

            help.setTo(pet.getOwnerEmail());
            help.setSubject("Pet Diet Information");
            help.setText(emailContent, true);
            mailSender.send(mailMessage);
        }
         catch (MessagingException e)
         {
             logger.error("Failed to send email for pet with ID: {}", id, e);
             throw new RuntimeException("Failed to send email", e);
         }

        logger.info("Email sent successfully for pet with ID: {}", id);

        logService.logApiCall(
                pet.getOwnerName(),
                "Email Service",
                "Sent Successfully",
                ZonedDateTime.now()
        );
    }

    @Scheduled(cron = "${scheduler.cron}")
    public void scheduleDailyPetInfoEmail()
    {
        logger.info("Scheduling daily pet info email for all pets");

        if (isSchedulerEnabled)
        {
            logger.info("Sending daily pet info email for all pets");

            List<Pet> allPets = petRepository.findAll();
            for (Pet pet : allPets)
            {
                logger.info("Sending daily pet info email for pet with ID: {}", pet.getId());
                sendPetInfoEmail((long) pet.getId());
            }

            logService.logApiCall(
                    "System",
                    "Scheduled Email Service",
                    "Sent Successfully",
                    ZonedDateTime.now()
            );
            logger.info("Daily pet info email sent successfully for all pets");
        }
    }

    public void clearHibernateCache()
    {
        logger.info("Clearing Hibernate cache");
        entityManager.clear();
    }
}



