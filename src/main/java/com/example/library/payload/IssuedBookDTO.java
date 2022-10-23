package com.example.library.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IssuedBookDTO {
private Long studentId;
private String studentName;
private Long studentPhoneNumber;
private String callno;

}
