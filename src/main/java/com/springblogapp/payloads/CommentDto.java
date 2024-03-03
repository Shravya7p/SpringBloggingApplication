package com.springblogapp.payloads;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int Id;
	private String content;

}
