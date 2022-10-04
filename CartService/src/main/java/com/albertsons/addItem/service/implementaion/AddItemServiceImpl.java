package com.albertsons.addItem.service.implementaion;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.albertsons.addItem.constants.AddItemConstatnts;
import com.albertsons.addItem.model.AddItemResponse;
import com.albertsons.addItem.model.Error;
import com.albertsons.addItem.model.Errors;
import com.albertsons.addItem.model.Items;
import com.albertsons.addItem.repository.ItemRepository;
import com.albertsons.addItem.service.AddItemService;
import com.mongodb.MongoException;

public class AddItemServiceImpl implements AddItemService {

	@Autowired
	ItemRepository itemRepository;

	@Override
	public AddItemResponse addItemToCart(String storeId, Items items) {

		Optional<Items> itemData = itemRepository.findByOne(items.getItem_id(), storeId);

		AddItemResponse response;
		if (itemData.isPresent()) {
			response = new AddItemResponse();
			response.setStatus(AddItemConstatnts.MESSAGE_GENERIC1);
			response.setCode(200);
			return response;
		} else {
			try {
				List<Items> result = itemRepository.findOneAndUpdate("", storeId);
				if (result.size() > 1) {
					response = new AddItemResponse();
					response.setStatus(AddItemConstatnts.MESSAGE_GENERIC3);
					response.setCode(200);
					return response;
				}
			} catch (MongoException e) {
				response = new AddItemResponse();
				Error error = new Error();
				error.setStatus(AddItemConstatnts.MESSAGE_GENERIC2);
				error.setCode(400);
				response.setError(error);
				return response;
			}
		}
	}

	@Override
	public Errors validateItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
