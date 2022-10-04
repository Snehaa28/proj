package com.albertsons.addItem.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.albertsons.addItem.model.Items;

@Repository
@Component
public interface ItemRepository extends MongoRepository<Items, String> {

	Items findByOne(String itemId, String storeId);

	List<Items> findOneAndUpdate(String itemId, String storeId);

}
