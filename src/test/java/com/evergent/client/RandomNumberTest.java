package com.evergent.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomNumberTest {
    private static final Logger logger = LogManager.getLogger("AppLogger");
    @Test
    void getRandomInt() {

        RandomNumber randomNumber = new RandomNumber();

       logger.info(randomNumber.getRandomInt(1,100));



    }
}