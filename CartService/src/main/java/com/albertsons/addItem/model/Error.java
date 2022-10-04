package com.albertsons.addItem.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Component
public class Error {
	private String code;
	private String message;
	private String type;
	private String category;
	private String vendor;

}
