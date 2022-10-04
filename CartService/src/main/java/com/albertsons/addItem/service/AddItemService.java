package com.albertsons.addItem.service;

import com.albertsons.addItem.model.AddItemResponse;
import com.albertsons.addItem.model.Errors;
import com.albertsons.addItem.model.Items;

public interface AddItemService {

	public AddItemResponse addItemToCart(String storeId, Items items);

	public Errors validateItem();

}
