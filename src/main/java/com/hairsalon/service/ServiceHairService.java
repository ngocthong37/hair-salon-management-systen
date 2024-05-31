package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.ServiceHair;
import com.hairsalon.entity.User;
import com.hairsalon.respository.ServiceHairRepository;
import com.hairsalon.respository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ServiceHairService {
    @Autowired
    private ServiceHairRepository serviceHairRepository;

    @Autowired
    private StorageService storageService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<ResponseObject> getAll() {
        List<ServiceHair> hairServiceList = null;
        hairServiceList = serviceHairRepository.findAll();
        if (!hairServiceList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", hairServiceList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

    public ResponseEntity<ResponseObject> findByServiceName(String serviceName) {
        List<ServiceHair> hairServiceModelList = new ArrayList<>();
        try {
            Map<String, Object> results = new TreeMap<String, Object>();
            hairServiceModelList = serviceHairRepository.findByPartialServiceName(serviceName);
            results.put("hairService", hairServiceModelList);
            if (results.size() > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", results));
            }
            else {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Have error", ""));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("ERROR", "Have error:", "loi"));
        }
    }

    public ResponseEntity<ResponseObject> findById(Integer id) {
//        Optional<ServiceHair> hairServiceModel = new Optional<ServiceHair>();
//        try {
//            Map<String, Object> results = new TreeMap<String, Object>();
//            hairServiceModel = serviceHairRepository.findById(id);
//            results.put("hairService", hairServiceModel);
//            if (results.size() > 0) {
//                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", results));
//            }
//            else {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Have error", ""));
         //   }

//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseObject("ERROR", "Have error:", e.getMessage()));
//        }
    }

    public String uploadImage(MultipartFile file, String namePath, Integer serviceHairId) {
        String imageUrl = storageService.uploadImages(file, namePath);
        serviceHairRepository.updateImage(imageUrl, serviceHairId);
        return imageUrl;
    }


    public ResponseEntity<Object> add(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            String serviceName = jsonNode.get("serviceName") != null ? jsonNode.get("serviceName").asText() : "";
            Double price = jsonNode.get("price") != null ? jsonNode.get("price").asDouble() : null;
            String description = jsonNode.get("description") != null ? jsonNode.get("description").asText() : "";
            String url = jsonNode.get("url") != null ? jsonNode.get("url").asText() : "";
            ServiceHair serviceHair = new ServiceHair();
            serviceHair.setServiceName(serviceName);
            serviceHair.setDescription(description);
            serviceHair.setPrice(price);
            serviceHair.setUrl(url);
            serviceHair.setStatus("OK");
            ServiceHair saveServiceHair = serviceHairRepository.save(serviceHair);


            if (saveServiceHair.getId() > 0) {
                List<User> customerList = userRepository.findAllCustomer();
                String[] cc = new String[customerList.size()];
                for (int i = 0; i < customerList.size(); i++) {
                    User customer = customerList.get(i);
                    cc[i] = customer.getEmail();
                }
                Map<String, Object> model = new HashMap<>();
                model.put("name", saveServiceHair.getServiceName());
                model.put("price", saveServiceHair.getPrice() + " VND");
                model.put("description", saveServiceHair.getDescription());
//                emailSendService.sendMail("thongnguyenngoc3738@gmail.com", cc, "Thông báo dịch vụ mới", model);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", saveServiceHair.getId()));
            }
            else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Have error when add service hair", ""));
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }
    }

//    public List<String> uploadImage(List<MultipartFile > files, String namePath, Integer quotationId) {
//        List<String> imageUrls;
//
//        imageUrls = storageService.uploadImages(files, namePath);
//        List<ProductImage> productImageList = productImageRepository.findByQuotationId(quotationId);
//
//        for (int i = 0; i < imageUrls.size(); i++) {
//            quotationRepository.updateImage(imageUrls.get(i), quotationId, productImageList.get(i).getId());
//        }
//        quotationRepository.updateDefaultImage(imageUrls.get(0), quotationId);
//        return imageUrls;
//    }


    public ResponseEntity<Object> update(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            Integer id = jsonNode.get("id") != null ? jsonNode.get("id").asInt() : null;
            String serviceName = jsonNode.get("serviceName") != null ? jsonNode.get("serviceName").asText() : "";
            Double price = jsonNode.get("price") != null ? jsonNode.get("price").asDouble() : null;
            String description = jsonNode.get("description") != null ? jsonNode.get("description").asText() : "";
            String url = jsonNode.get("url") != null ? jsonNode.get("url").asText() : "";
            Optional<ServiceHair> serviceHair = serviceHairRepository.findById(id);
            if (serviceHair.isPresent()) {
                serviceHair.get().setServiceName(serviceName);
                serviceHair.get().setDescription(description);
                serviceHair.get().setPrice(price);
                serviceHair.get().setUrl(url);
                ServiceHair updatedServiceHair =  serviceHairRepository.save(serviceHair.get());
                if (updatedServiceHair.getId() > 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", updatedServiceHair.getId()));
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Have error when add service hair", ""));

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }
    }

    public ResponseEntity<Object> updateStatus(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            Integer id = jsonNode.get("id") != null ? jsonNode.get("id").asInt() : null;
            String status = jsonNode.get("status") != null ? jsonNode.get("status").asText() : null;
            Optional<ServiceHair> serviceHair = serviceHairRepository.findById(id);
            if (serviceHair.isPresent()) {
                serviceHair.get().setStatus(status);
                ServiceHair updatedServiceHair =  serviceHairRepository.save(serviceHair.get());
                if (updatedServiceHair.getId() > 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", updatedServiceHair.getId()));
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ERROR", "Have error when update status service hair", ""));

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }
    }


}
