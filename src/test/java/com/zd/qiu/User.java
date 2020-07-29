package com.zd.qiu;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    int winContinueNum;
    int threeContinueNum;
    int winContinueNumTemp;
    int threeIncludeNum;
    boolean ok;
    char checkChar;
}