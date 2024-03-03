package com.springblogapp.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
private Integer categoryId;
	
	@NotBlank
	@Size(min=4,message="Min size of category title is 4")
	private String categoryTitle;
	
	@Size(min=3,message = "Min size of category desc is 3")
	private String categoryDesc;

}
