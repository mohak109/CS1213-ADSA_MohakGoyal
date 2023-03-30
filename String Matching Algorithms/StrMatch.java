/*
 * Author: Mohak Goyal (mohak109)
 * Created on: 30/03/2023
 * Last Updated on: 31/03/2023
 * Topic: String Matching Algorithms (Naive, Rabin Karb, KMP (Knuth Morris Pratt))
 * 
 */

public class StrMatch {
    public static void main(String[] args) {
        MatchString MatchStr = new MatchString();

        String text = "AABAACAADAABAAABAA";
        String pattern = "AABA";

        long start1 = System.nanoTime();
        System.out.println(MatchStr.naiveApproach(text, pattern));
        long end1 = System.nanoTime();
        System.out.println("Elapsed Time in nano seconds: " + (end1 - start1));

        System.out.println();

        long start2 = System.nanoTime();
        System.out.println(MatchStr.rabinKarp(text, pattern, 17));
        long end2 = System.nanoTime();
        System.out.println("Elapsed Time in nano seconds: " + (end2 - start2));

        System.out.println();

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
            if (pat.equalsIgnoreCase(txt.substring(i, i + pat_l))) {
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
                if (pat.equalsIgnoreCase(txt.substring(i, i + pat_l))) {
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
        int M = pat.length();
        int N = txt.length();
        boolean status = false;

        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];
        int j = 0; // index for pat[]

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat, M, lps);

        int i = 0; // index for txt[]
        while ((N - i) >= (M - j)) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                System.out.println("Found pattern " + "at index " + (i - j));
                status = true;
                j = lps[j - 1];
            }

            // mismatch after j matches
            else if (i < N && pat.charAt(j) != txt.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
        return status;
    }

    void computeLPSArray(String pat, int M, int lps[]) {
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