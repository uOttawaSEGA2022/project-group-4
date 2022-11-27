package com.example.mealer_project.utils.PostalCodes;

import java.util.Comparator;

/**
 * Class to compare Canadian Postal Codes to closeness
 * You can initialize an instance by providing an origin Postal Code which will be used for all comparisons
 * Example: if origin is X, using the methods exposed by this class you can check which postal code among A or B is closer to X
 * You can also use static methods for comparison if you don't want to set an origin
 */
public class PostalCodeComparator implements Comparator<String> {

    /**
     * Store an origin postal code for comparison
     */
    private PostalCode origin;

    /**
     * Initialize an instance of PostalCodeComparator by providing a string representation of the origin postal code
     * @param originPostalCodeStr string representation of the origin postal code
     * @throws IllegalArgumentException if creation of origin postal code fails due to validation failure
     */
    public PostalCodeComparator(String originPostalCodeStr) throws IllegalArgumentException {
        this.origin = new PostalCode(originPostalCodeStr);
    }

    /**
     * Get the current origin postal code
     * @return origin postal code
     */
    public PostalCode getOrigin() {
        return origin;
    }

    /**
     * Set an origin Postal Code for comparison
     * @param origin string representing a valid Canadian Postal code
     */
    public void setOrigin(String origin) {
        this.origin = new PostalCode(origin);
    }

    private static int getPDDigit(char key) {
        return PostalCode.POSTAL_DISTRICT_CODES.get(key);
    }

    private int getDistanceFromOrigin(PostalCode postalCode) {
        return Math.abs(getPDDigit(origin.getPostalDistrictCharacter()) - getPDDigit(postalCode.getPostalDistrictCharacter()));
    }

    private int comparePostalDistricts(PostalCode firstPostalCode, PostalCode secondPostalCode) throws IllegalArgumentException {
        // guard-clause
        if (firstPostalCode == null || secondPostalCode == null) {
            throw new IllegalArgumentException("Invalid arguments for comparePostalDistrict");
        }
        // confirm we have origin PostalCode
        if (origin == null) {
            throw new IllegalArgumentException("No origin Postal Code currently defined");
        }
        // compare and return result
        // if the postal district codes of both postal codes belong to same postal district as origin, return 0
        if (firstPostalCode.getPostalDistrictCharacter() == origin.getPostalDistrictCharacter() && secondPostalCode.getPostalDistrictCharacter() == origin.getPostalDistrictCharacter())
            return 0;
        // else compare and see which one is closer
        else
            return (getDistanceFromOrigin(firstPostalCode) <= getDistanceFromOrigin(secondPostalCode)) ? -1 : 1;
    }

    private static int getDistanceBetweenCodes(PostalCode firstCode, PostalCode secondCode) {
        return Math.abs(getPDDigit(firstCode.getPostalDistrictCharacter()) - getPDDigit(secondCode.getPostalDistrictCharacter()));
    }

    private static int comparePostalDistricts(PostalCode origin, PostalCode firstPostalCode, PostalCode secondPostalCode) throws IllegalArgumentException {
        // guard-clause
        if (origin == null || firstPostalCode == null || secondPostalCode == null) {
            throw new IllegalArgumentException("Invalid arguments for comparePostalDistrict");
        }
        // compare and return result
        return (getDistanceBetweenCodes(origin, firstPostalCode) <= getDistanceBetweenCodes(origin, secondPostalCode)) ? -1 : 1;
    }

    private int getURIDifference(PostalCode postalCode) {
        return Math.abs(this.origin.getUrbanRuralIdentifier() - postalCode.getUrbanRuralIdentifier());
    }

    private int compareUrbanRuralIdentifier(PostalCode firstPostalCode, PostalCode secondPostalCode) throws IllegalArgumentException {
        // validate input
        if (firstPostalCode == null || secondPostalCode == null) {
            throw new IllegalArgumentException("Invalid values for urban and rural identifier for comparison");
        }
        // confirm we have origin PostalCode
        if (origin == null) {
            throw new IllegalArgumentException("No origin Postal Code currently defined");
        }
        // if the URI bit is same as origin for both, we return 0
        if (firstPostalCode.getUrbanRuralIdentifier() == origin.getUrbanRuralIdentifier() && secondPostalCode.getUrbanRuralIdentifier() == origin.getUrbanRuralIdentifier())
            return 0;
        else
            return (getURIDifference(firstPostalCode) <= getURIDifference(secondPostalCode)) ? -1 : 1;
    }

    private static int getDifferenceBetweenCodeDigits(int firstDigit, int secondDigit) {
        return Math.abs(firstDigit - secondDigit);
    }

    private static int comparePostalCodeDigits(int originDigit, int firstCodeDigit, int secondCodeDigit) throws IllegalArgumentException {
        // validate input
        if (PostalCode.isValidPostalCodeDigit(originDigit) && PostalCode.isValidPostalCodeDigit(firstCodeDigit) && PostalCode.isValidPostalCodeDigit(secondCodeDigit)) {
            // if digits same, return 0
            if (firstCodeDigit == originDigit && secondCodeDigit == originDigit)
                return 0;
            else
                // return -1 if first digit closer to origin, else 1
                return (getDifferenceBetweenCodeDigits(originDigit, firstCodeDigit) <= getDifferenceBetweenCodeDigits(originDigit, secondCodeDigit)) ? -1 : 1;

        }
        // if invalid values
        throw new IllegalArgumentException("Invalid values for urban and rural identifier for comparison");
    }

    private static int getLetterDistance(char firstLetter, char secondLetter) {
        return Math.abs(PostalCode.VALID_POSTAL_CODE_LETTERS.get(firstLetter) - PostalCode.VALID_POSTAL_CODE_LETTERS.get(secondLetter));
    }

    private static int comparePostalCodeLetters(char originLetter, char firstCodeLetter, char secondCodeLetter) throws IllegalArgumentException {
        // validate input
        if (PostalCode.isValidPostalCodeLetter(originLetter) && PostalCode.isValidPostalCodeLetter(firstCodeLetter) && PostalCode.isValidPostalCodeLetter(secondCodeLetter)) {
            // compare and return result
            // if the letters of first and second postal codes same as origin letter, return 0
            if (firstCodeLetter == originLetter && secondCodeLetter == originLetter)
                return 0;
            else
                // else compare and see which one is closer
                return (getLetterDistance(originLetter, firstCodeLetter) <= getLetterDistance(originLetter, secondCodeLetter)) ? -1 : 1;
        } else {
            // if invalid letters
            throw new IllegalArgumentException("Invalid argument values for comparison");
        }
    }

    /**
     * Compares three postal codes: origin, first postal code and second postal code
     * Returns 0 if first postal code is same as second postal code
     * Returns -1 if first postal code is closer to the origin
     * Returns 1 if second postal code is closer to the origin
     * @param firstPostalCode instance of PostalCode for first postal code
     * @param secondPostalCode instance of PostalCode for second postal code
     * @return -1, 0, 1 based on comparison
     */
    public int comparePostalCodes(PostalCode firstPostalCode, PostalCode secondPostalCode) throws IllegalArgumentException {

        // validate arguments
        if (firstPostalCode == null || secondPostalCode == null) {
            throw new IllegalArgumentException("Invalid value provided for postal codes");
        }
        // confirm we have origin PostalCode
        if (origin == null) {
            throw new IllegalArgumentException("No origin Postal Code currently defined");
        }
        // base case
        if (firstPostalCode.equals(secondPostalCode)) {
            // if both postal codes equal, return zero
            return 0;
        }
        // store result
        int comparisonResult;

        // FSA Postal District comparison
        // if postal districts of both provided codes are different from that of origin
        // we return the one closer to origin right away (no more comparison needed)
        comparisonResult = comparePostalDistricts(firstPostalCode, secondPostalCode);
        if (comparisonResult != 0) {
            // Postal District Comparison result would be -1 if first postal code is closer to origin, else 1
            return comparisonResult;
        }

        // if Postal Districts of all postal codes (origin, firstPostalCode and secondPostalCode) are same
        // we need to compare second bit of FSA

        // URI - Urban Rural Identifier comparison
        comparisonResult = compareUrbanRuralIdentifier(firstPostalCode, secondPostalCode);
        // if URI not the same of all three postal codes (origin, first, second)
        if (comparisonResult != 0) {
            // return -1 if URI of firstPostalCode closer to origin, else 1
            return comparisonResult;
        }

        // if URI same of all three codes (origin, first, second)
        // we need to compare third region identifier bit of the FSA
        comparisonResult = comparePostalCodeLetters(origin.getRegionIdentifier(), firstPostalCode.getRegionIdentifier(), secondPostalCode.getRegionIdentifier());
        // if region identifiers are same
        if (comparisonResult != 0) {
            // return -1 if region identifier of first code closer to origin than second
            return comparisonResult;
        }

        // if URI's same for all, we need to start comparing LDU

        // compare LDU first character
        comparisonResult = comparePostalCodeDigits(origin.getLDUFirstDigit(), firstPostalCode.getLDUFirstDigit(), secondPostalCode.getLDUFirstDigit());
        // if all not same
        if (comparisonResult != 0) {
            // return -1 if first code closer, else 1
            return comparisonResult;
        }

        // compare LDU character
        comparisonResult = comparePostalCodeLetters(origin.getLDUChar(), firstPostalCode.getLDUChar(), secondPostalCode.getLDUChar());
        // if all not same
        if (comparisonResult != 0) {
            // return comparison result
            return comparisonResult;
        }

        // Last comparison
        // compare last character of postal codes (LDU last digit)
        return comparePostalCodeDigits(origin.getLDULastDigit(), firstPostalCode.getLDULastDigit(), secondPostalCode.getLDULastDigit());
    }

    /**
     * Compares three postal codes: origin, first postal code and second postal code
     * Returns 0 if first postal code is same as second postal code
     * Returns -1 if first postal code is closer to the origin
     * Returns 1 if second postal code is closer to the origin
     * @param firstPostalCodeStr string representation of first postal code, ex: "T6X8X9" or "T6x 8X9"
     * @param secondPostalCodeStr string representation of first postal code, ex: "L6B3M9" or "L6B 3M9"
     * @return -1, 0, 1 based on comparison
     */
    public int comparePostalCodes(String firstPostalCodeStr, String secondPostalCodeStr) throws IllegalArgumentException {
        // remove any spaces from the postal codes string representations
        firstPostalCodeStr = firstPostalCodeStr.replaceAll("\\s+", "");
        secondPostalCodeStr = secondPostalCodeStr.replaceAll("\\s+", "");
        return comparePostalCodes(new PostalCode(firstPostalCodeStr), new PostalCode(secondPostalCodeStr));
    }

    /**
     * Method to allow comparison (and sorting) of postal codes
     * Important: requires that you initialize an instance of PostalCodeComparator providing it the origin postal code
     * @param firstPostalCode string representation of first postal code, ex: "T6X8X9" or "T6x 8X9"
     * @param secondPostalCode string representation of first postal code, ex: "L6B3M9" or "L6B 3M9"
     * @return 1 - if second postal code closer to origin, -1 if first postal code closer to origin, 0 is all same
     */
    @Override
    public int compare(String firstPostalCode, String secondPostalCode) {
        return comparePostalCodes(firstPostalCode, secondPostalCode);
    }
}
