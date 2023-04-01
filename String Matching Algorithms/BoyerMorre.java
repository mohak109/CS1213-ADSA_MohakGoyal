public class BoyerMorre {
    public static void main(String[] args) {
        String text = "ABAAABCHDBSHJBABCCJDCABDCDHCAJBCH";
        String pattern = "ABC";
        System.out.println("\n"+"Boyer Moore Approach");
        System.out.println(boyerMoore(text, pattern));
    }

    static boolean boyerMoore(String txt, String pat) {
        boolean status = false;
        int m = pat.length();
        int n = txt.length();

        // Initialising the badchar array
        int badchar[] = new int[128];

        /*
         * Fill the bad character array by calling the preprocessing
         * function badCharHeuristic() for given pattern
         */
        badCharHeuristic(pat, m, badchar);

        int s = 0; // s is shift of the pattern with// respect to text

        while (s <= (n - m)) {
            int j = m - 1;

            /*
             * Keep reducing index j of pattern while
             * characters of pattern and text are
             * matching at this shift s
             */
            while (j >= 0 && pat.charAt(j) == txt.charAt(s + j)) {
                j--;
            }

            /*
             * If the pattern is present at current
             * shift, then index j will become -1 after
             * the above loop
             */
            if (j < 0) {
                System.out.println("Patterns occur at shift = " + s);

                status = true;

                // We shift the pointer at text to the character just after the pattern
                // occurence it matches
                s += m;
            }

            /*
             * We set the shift (s) so that the bad character in text aligns with the last
             * occurrence of it in pattern. The max function is used to make sure that we
             * get a positive shift.
             */
            else {
                s += max(1, j - badchar[txt.charAt(s + j)]);
            }
        }

        // If we don't find the pattern in the text then we print "Pattern not found in
        // the console".
        if (!status) {
            System.out.println("Pattern not found");
        }

        return status;
    }

    // The preprocessing function for Boyer Moore's bad character heuristic
    static void badCharHeuristic(String str, int size, int badchar[]) {
        // Initialize all occurrences as -1
        for (int i = 0; i < badchar.length; i++) {
            badchar[i] = -1;
        }

        // Inserting the value of last occurrence of a character
        // (indices of table are ascii and values are index of occurrence)
        for (int i = 0; i < size; i++) {
            badchar[(int) str.charAt(i)] = i;
        }
    }

    // A utility function to get maximum of two integers
    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

}
