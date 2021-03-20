package org.mcnichol.algorithm;

import org.mcnichol.logging.LogUtility;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * Listing 22.7 Implementation of algorithm for generating primes from
 * Introduction to Java - Programming and Data Structures 11E for capturing execution time in nanoseconds
 */
public class Listing22_7 implements PrimeComputer {

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
            int count = 0;

            long now = System.nanoTime();
            boolean[] primes = new boolean[n + 1];

            Arrays.fill(primes, true);

            for (int k = 2; k <= n / k; k++) {
                if (primes[k]) {
                    for (int j = k; j <= n / k; j++) {
                        primes[k * j] = false;
                    }
                }
            }

            for (int p = 2; p < primes.length; p++) {
                if (primes[p]) count++;
            }

            long executionTime = System.nanoTime() - now;
            executionTimeInNano[i] = executionTime;

            LogUtility.getInstance().log(Level.INFO, String.format("Number of prime values %,d completed in %.4f seconds", count, (executionTimeInNano[i] / 1_000_000_000D)));
        }


        return executionTimeInNano;
    }

}
