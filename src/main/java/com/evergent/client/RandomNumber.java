package com.evergent.client;

import java.util.random.RandomGenerator;

public class RandomNumber {

    private RandomGenerator generator = RandomGenerator.of("Xoshiro256PlusPlus");


    public int getRandomInt(int minimum, int maximum){
        return generator.nextInt(minimum, maximum);

    }

}
