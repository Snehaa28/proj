package com.albertsons.addItem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.albertsons.addItem.model.AddItemResponse;
import com.albertsons.addItem.model.Items;
import com.albertsons.addItem.service.AddItemService;

@RestController
@Component
@RequestMapping("/api")
public class AddItemController {

	AddItemService addItemService;

	@PutMapping("/addItemToCart")
	public ResponseEntity<AddItemResponse> addItemToCart(@PathVariable("storeId") String storeId,
			@RequestBody Items items) {
		AddItemResponse response = addItemService.addItemToCart(storeId, items);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
