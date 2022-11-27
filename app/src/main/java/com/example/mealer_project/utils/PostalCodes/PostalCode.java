package com.example.mealer_project.utils.PostalCodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Canadian Postal Code
 */
public class PostalCode {

    // part of FSA
    private char postalDistrictCharacter;
    private int urbanRuralIdentifier;
    private char regionIdentifier;

    // part of LDU
    private int LDUFirstDigit;
    private char LDUChar;
    private int LDULastDigit;

    /**
     * Postal District Codes - First bit of the FSA
     */
    protected static final Map<Character, Integer> POSTAL_DISTRICT_CODES = new HashMap<Character, Integer>() {{
        put('A',1); // Newfoundland and Labrador
        put('B',2); // Nova Scotia
        put('C',3); // Prince Edward Island
        put('E',4); // New Brunswick
        put('G',5); // Eastern Quebec
        put('H',6); // Metropolitan Montreal
        put('J',7); // Western Quebec
        put('K',8); // Eastern Ontario
        put('L',9); // Central Ontario
        put('M',10); // Metropolitan Toronto
        put('N',11); // Southwestern Ontario
        put('P',12); // Northern Ontario
        put('R',13); // Manitoba
        put('S',14); // Saskatchewan
        put('T',15); // Alberta
        put('V',16); // British Columbia
        put('X',17); // Northwest Territories/Nunavut
        put('Y',18); // Yukon
    }};

    /**
     * List of all allowed letters that can be used in Postal Code
     */
    protected static final Map<Character, Integer> VALID_POSTAL_CODE_LETTERS = new HashMap<Character, Integer>() {{
        put('A',1);
        put('B',2);
        put('C',3);
        put('E',4);
        put('G',5);
        put('H',6);
        put('J',7);
        put('K',8);
        put('L',9);
        put('M',10);
        put('N',11);
        put('P',12);
        put('R',13);
        put('S',14);
        put('T',15);
        put('V',16);
        put('W',17);
        put('X',18);
        put('Y',19);
        put('Z',20);
    }};

    public PostalCode(String postalCode) throws IllegalArgumentException {
        // remove spaces from postal code
        postalCode = postalCode.replaceAll("\\s+", "");
        // parse and store postal code information
        this.parsePostalCode(postalCode);
    }

    private void parsePostalCode(String postalCode) throws IllegalArgumentException {
        // validate input postal code
        if (postalCode == null || postalCode.length() != 6) {
            throw new IllegalArgumentException("Invalid value of postal code for parsing: " + postalCode);
        }

        // parse FSA values
        this.setPostalDistrictCharacter(postalCode.charAt(0));
        this.setUrbanRuralIdentifier(postalCode.charAt(1));
        this.setRegionIdentifier(postalCode.charAt(2));
        // parse LDU values
        this.setLDUFirstDigit(postalCode.charAt(3));
        this.setLDUChar(postalCode.charAt(4));
        this.setLDULastDigit(postalCode.charAt(5));
    }

    public char getPostalDistrictCharacter() {
        return postalDistrictCharacter;
    }

    public void setPostalDistrictCharacter(char postalDistrictCharacter) throws IllegalArgumentException {
        if (isValidFSAPostalDistrict(postalDistrictCharacter)) {
            this.postalDistrictCharacter = postalDistrictCharacter;
        } else {
         throw new IllegalArgumentException("Invalid Postal District Character: " + postalDistrictCharacter);
        }
    }

    public int getUrbanRuralIdentifier() {
        return urbanRuralIdentifier;
    }

    public void setUrbanRuralIdentifier(char urbanRuralIdentifierCharacter) throws IllegalArgumentException {
        try {
            int urbanRuralIdentifier = Integer.parseInt(String.valueOf(urbanRuralIdentifierCharacter));
            if (isValidFSADigit(urbanRuralIdentifier)) {
                this.urbanRuralIdentifier = urbanRuralIdentifier;
            } else {
                throw new IllegalArgumentException("Invalid Urban Rural Identifier digit: " + urbanRuralIdentifier);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse URI identifier: " + e.getMessage());
        }
    }

    public char getRegionIdentifier() {
        return regionIdentifier;
    }

    public void setRegionIdentifier(char regionIdentifier) throws IllegalArgumentException {
        if (isValidPostalCodeLetter(regionIdentifier)) {
            this.regionIdentifier = regionIdentifier;
        } else {
            throw new IllegalArgumentException("Invalid character provided for FSA third character: " + regionIdentifier);
        }
    }

    public int getLDUFirstDigit() {
        return LDUFirstDigit;
    }

    public void setLDUFirstDigit(char LDUFirstDigitChar) throws IllegalArgumentException {
        try {
            int LDUFirstDigit = Integer.parseInt(String.valueOf(LDUFirstDigitChar));
            // LDU Digit can be from 0 to 9 (inclusive)
            if (LDUFirstDigit >= 0 && LDUFirstDigit <= 9) {
                this.LDUFirstDigit = LDUFirstDigit;
            } else {
                throw new IllegalArgumentException("Invalid LDU digit: " + LDUFirstDigit);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse " + LDUFirstDigitChar + " to LDU digit: " + e.getMessage());
        }
    }

    public char getLDUChar() {
        return LDUChar;
    }

    public void setLDUChar(char LDUChar) throws IllegalArgumentException {
        if (isValidPostalCodeLetter(LDUChar)) {
            this.LDUChar = LDUChar;
        } else {
        throw new IllegalArgumentException("Invalid letter provided for LDU first character");
        }
    }

    public int getLDULastDigit() {
        return this.LDULastDigit;
    }

    public void setLDULastDigit(char LDULastDigitChar) throws IllegalArgumentException {
        try {
            int LDULastDigit = Integer.parseInt(String.valueOf(LDULastDigitChar));
            // LDU Digit can be from 0 to 9 (inclusive)
            if (LDULastDigit >= 0 && LDULastDigit <= 9) {
                this.LDULastDigit = LDULastDigit;
            } else {
                throw new IllegalArgumentException("Invalid LDU digit: " + LDULastDigit);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse " + LDULastDigitChar + " to LDU digit: " + e.getMessage());
        }
    }

    /**
     * Validate an input digit to be within the acceptable range of a FSA digit (0-9)
     * 1-9: urban postal code digit
     * 0: rural postal code digit
     * @param digit number to be validated
     * @return true, if digit is a valid FSA digit, else false
     */
    protected static boolean isValidFSADigit(int digit) {
        return (digit >= 0 && digit <= 9);
    }

    /**
     * Checks if a provided character is a valid FSA postal district character
     * @param postalDistrictCharacter character to validate
     * @return true if character is a valid FSA postal district character, else false
     */
    protected static boolean isValidFSAPostalDistrict(char postalDistrictCharacter) {
        return POSTAL_DISTRICT_CODES.containsKey(postalDistrictCharacter);
    }

    /**
     * Validate an input digit to be within the acceptable range of a Postal Code digit (0-9)
     * @param digit number to be validated
     * @return true, if digit is a valid Postal Code digit, else false
     */
    protected static boolean isValidPostalCodeDigit(int digit) {
        return (digit >= 0 && digit <= 9);
    }

    /**
     * Checks if a provided character is an allowed english letter for a postal code
     * @param letter character to be validated
     * @return true, if the letter is allowed in a postal code, else false
     */
    protected static boolean isValidPostalCodeLetter(char letter) {
        return VALID_POSTAL_CODE_LETTERS.containsKey(letter);
    }

    /**
     * Get string representation of the postal code, ex: T6X2X9 (without space)
     * @return string representation of the postal code, ex: T6X2X9 (without space)
     */
    @Override
    public String toString() {
        return "" + postalDistrictCharacter + urbanRuralIdentifier + regionIdentifier + LDUFirstDigit + LDUChar + LDULastDigit;
    }

    /**
     * Override equals to check all values are same
     * @param o object to compare with
     * @return true is object is same as current postal code, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostalCode that = (PostalCode) o;
        return postalDistrictCharacter == that.postalDistrictCharacter && urbanRuralIdentifier == that.urbanRuralIdentifier && regionIdentifier == that.regionIdentifier && LDUFirstDigit == that.LDUFirstDigit && LDUChar == that.LDUChar && LDULastDigit == that.LDULastDigit;
    }

    /**
     * Method to generate hash code for the Postal Code instances
     * @return integer representing hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(postalDistrictCharacter, urbanRuralIdentifier, regionIdentifier, LDUFirstDigit, LDUChar, LDULastDigit);
    }
}
