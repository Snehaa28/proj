package com.albertsons.addItem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Items")
@ToString
@Component
@Builder
public class Items {

	@Id
	private String Id;
	private String item_id;
	private String ext_description;
	private String pos_description;
	private String upc_type;
	private String scan_code;
	private Boolean food_stamp;
	private Boolean restricted_item;
	private Boolean ewic;
	private Boolean weight_item;
	private int dept;
	private int sell_multiple;
	private Boolean tax_item;
	private Boolean club_item;
	private int smic;
	private String image_url;
	private int sell_price;
	private int regular_price;
	private int promoOfferPrice;
	private String bpn_no;
	ClubPrice clubPrice;
	private int jfuOfferCount;
	J4U jfuOffers;
	private Boolean cms_flag;
	private int weight;
	private Boolean points_apply;
	private Boolean non_discountable;
	private Boolean strikeThroughPrice;
	private String sellby_unit;
	private String sellByWeight;
	
}
