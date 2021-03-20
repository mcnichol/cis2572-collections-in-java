package org.mcnichol.algorithm;

import org.mcnichol.logging.LogUtility;

import java.util.logging.Level;

/**
 * Listing 22.5 Implementation of algorithm for generating primes from
 * Introduction to Java - Programming and Data Structures 11E for capturing execution time in nanoseconds
 */
public class Listing22_5 implements PrimeComputer {
    /**
     * Implementation of Algorithm for calculating primes with execution time in nano retrieval
     *
     * @param arrayOfPrimeComputerRunLimits upper limit inclusive range to look for prime numbers.  Will not look for prime values beyond upper limit
     * @return list of execution times for each run limit received
     */
    @Override
    public Long[] getExecutionTimeOfPrimeComputations(Integer... arrayOfPrimeComputerRunLimits) {
        Long[] executionTimeInNano = new Long[arrayOfPrimeComputerRunLimits.length];

        for (int i = 0, listOfPrimesToComputeLength = arrayOfPrimeComputerRunLimits.length; i < listOfPrimesToComputeLength; i++) {
            Integer n = arrayOfPrimeComputerRunLimits[i];

            long now = System.nanoTime();

            int count = 0;
            int number = 2;

            while (number <= n) {
                boolean isPrime = true;

                for (int divisor = 2; divisor <= (int) (Math.sqrt(number)); divisor++) {
                    if (number % divisor == 0) {
                        isPrime = false;
                        break;
                    }
                }

                if (isPrime) {
                    count++;
                }

                number++;
            }

            long executionTime = System.nanoTime() - now;
            executionTimeInNano[i] = executionTime;

            LogUtility.getInstance().log(Level.INFO, String.format("Number of prime values %,d completed in %.4f seconds", count, (executionTimeInNano[i] / 1_000_000_000D)));
        }


        return executionTimeInNano;
    }
}
