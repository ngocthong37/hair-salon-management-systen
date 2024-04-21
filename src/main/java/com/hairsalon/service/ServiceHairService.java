package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hairsalon.entity.ProductImage;
import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.ServiceHair;
import com.hairsalon.model.HairServiceModel;
import com.hairsalon.respository.ServiceHairRepository;
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
            ServiceHair saveServiceHair = serviceHairRepository.save(serviceHair);
            if (saveServiceHair.getId() > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", ""));
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
//        JsonNode jsonNode;
//        JsonMapper jsonMapper = new JsonMapper();
//        try {
//            jsonNode = jsonMapper.readTree(json);
//            Integer id = jsonNode.get("id") != null ? jsonNode.get("id").asInt() : null;
//            String serviceName = jsonNode.get("serviceName") != null ? jsonNode.get("serviceName").asText() : "";
//            Double price = jsonNode.get("price") != null ? jsonNode.get("price").asDouble() : null;
//            String description = jsonNode.get("description") != null ? jsonNode.get("description").asText() : "";
//            String url = jsonNode.get("url") != null ? jsonNode.get("url").asText() : "";
//            ServiceHair serviceHair = new ServiceHair();
//            serviceHair.setId(id);
//            serviceHair.setServiceName(serviceName);
//            serviceHair.setDescription(description);
//            serviceHair.setPrice(price);
//            serviceHair.setUrl(url);
//            if (serviceHairImp.update(serviceHair) < 0) {
//                return ResponseEntity.status(HttpStatus.OK)
//                        .body(new ResponseObject("ERROR", "Have error when add service hair", ""));
//            }
//        }
//        catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
//        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", ""));
    }


}
