/*
 * Author: Mohak Goyal (mohak109)
 * Created on: 30/03/2023
 * Last Updated on: 31/03/2023
 * Topic: String Matching Algorithms (Naive, Rabin Karb, KMP (Knuth Morris Pratt))
 * Reference: GeeksforGeeks
 */

import java.util.Scanner;

public class StrMatch {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the text:");
        String text = scan.nextLine();
        System.out.println("Enter the pattern to be matched:");
        String pattern = scan.nextLine();

        scan.close();

        MatchString MatchStr = new MatchString();

        // String text = "AAAAAAAAAAAABAAAAAA";
        // String pattern = "AABA";

        System.out.println("\nNaive Approach (Brute Force)");
        long start1 = System.nanoTime();
        System.out.println(MatchStr.naiveApproach(text, pattern));
        long end1 = System.nanoTime();
        System.out.println("Elapsed Time in nano seconds: " + (end1 - start1));

        System.out.println("\nRabin-Karp Approach");
        long start2 = System.nanoTime();
        System.out.println(MatchStr.rabinKarp(text, pattern, 17));
        long end2 = System.nanoTime();
        System.out.println("Elapsed Time in nano seconds: " + (end2 - start2));

        System.out.println("\nKnuth-Morris Pratt Approach");
        long start3 = System.nanoTime();
        System.out.println(MatchStr.KMP(text, pattern));
        long end3 = System.nanoTime();
        System.out.println("Elapsed Time in nano seconds: " + (end3 - start3));

        System.out.println();
    }
}

class MatchString {

    boolean naiveApproach(String txt, String pat) {
        /*
         * By default we consider that we have
         * not matched the string yet hence,
         */
        boolean status = false;
        int pat_l = pat.length(); // length of the pattern to be matched
        int txt_l = txt.length(); // length of the text in which pattern is to be matched

        /*
         * Brute force method where we check each and every pattern of length pat in txt
         * and match it to be pat, if we get any match then we return the starting index
         * of the pattern in the text.
         */
        for (int i = 0; i < (txt_l - pat_l) + 1; i++) {

            // check condition for matching pattern in text
            if (pat.equals(txt.substring(i, i + pat_l))) {
                System.out.println("Pattern found at index " + i);
                status = true; // once we find the text, we change the status to be true
            }

        }

        // If we don't find the pattern in the text then we print "Pattern not found in
        // the console".
        if (!status) {
            System.out.println("Pattern not found");
        }

        return status;
    }

    boolean rabinKarp(String txt, String pat, int q) {
        /*
         * By default we consider that we have
         * not matched the string yet hence,
         */
        boolean status = false;
        int txt_l = txt.length(); // length of the text in which pattern has to be matched
        int pat_l = pat.length(); // length of the pattern to be matched in the text

        /*
         * Values of pattern and text, calculating with hash function which is the int
         * value of each character and multiplied with 10 raised to the power the index
         * of the character
         */
        int hash_p = 0;
        int hash_t = 0;

        // Calculating the hash value of pattern to be matched
        for (int i = 0; i < pat_l; i++) {
            hash_p = hash_p + (pat.charAt(i) * (int) Math.pow(10, i));
        }
        hash_p %= q;

        /*
         * Matching the hash value of subset of text, same size as of pattern, with the
         * hash value of pattern. We calculate the hash value of subset of text in each
         * iteration.
         */
        for (int i = 0; i < (txt_l - pat_l) + 1; i++) {
            hash_t = 0;

            // Calculating the hash value of subset of text
            for (int j = i; j < i + pat_l; j++) {
                hash_t = hash_t + (txt.charAt(j) * (int) Math.pow(10, j - i));
            }
            hash_t %= q;

            // Mathcing the hash value and if that matched then we check if we got the
            // pattern in the subset
            if (hash_p == hash_t) {

                // check condition for matching pattern in text
                if (pat.equals(txt.substring(i, i + pat_l))) {
                    System.out.println("Pattern found at index " + i);
                    status = true; // once we find the text, we change the status to be true
                }
            }
        }

        // If we don't find the pattern in the text then we print "Pattern not found in
        // the console".
        if (!status) {
            System.out.println("Pattern not found");
        }

        return status;
    }

    boolean KMP(String txt, String pat) {
        boolean status = false;
        int M = pat.length();
        int N = txt.length();

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];
        int j = -1; // index for pat[]

        // Processing the pattern (computing lps[] array)
        computeLPSArray(pat, lps);

        int i = 0; // index for txt[]

        // Loop that will check for patterns in text provided
        while (i < N) {

            /*
             * We match the characters of text and pattern
             * If the character matches then we move further and check the next character
             * and try to match the whole pattern, if any where the character does not match
             * then we go to the else condition, read the comment in else to know more
             */
            if (txt.charAt(i) == pat.charAt(j + 1)) {
                i++;
                j++;
            }
            /*
             * If the character does not match then we go to lps array and and get the index
             * as to not start from the beginning, which makes it different from naive
             * approach.
             */
            else {
                i++;
                j = lps[j + 1] - 1;
            }

            /*
             * This is the default condition where we check if we have found the whole
             * pattern and if we have then we change the status and check for more
             * occurences of pattern in text by resetting the j
             */
            if (j == M - 1) {
                System.out.println("Pattern found at index " + (i - j - 1));
                j = -1;
                status = true;
            }

        }

        // If we don't find the pattern in the text then we print "Pattern not found in
        // the console".
        if (!status) {
            System.out.println("Pattern not found");
        }

        return status;
    }

    void computeLPSArray(String pat, int lps[]) {
        // length of the pattern
        int M = pat.length();
        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1];

                    // Also, note that we do not increment
                    // i here
                } else // if (len == 0)
                {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }
}