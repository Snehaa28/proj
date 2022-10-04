package com.albertsons.addItem.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@ToString
public class AddItemResponse {

	String status;
	String ack;
	String code;
	String message;

	Error error;

}
