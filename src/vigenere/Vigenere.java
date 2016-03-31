/* ****************************************************************************
 *
 * Copyright 2013 Oleg Voevodin
 *
 * This file is part of Vigenere in Java.
 *
 * Vigenere is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vigenere is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Vigenere.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 				Vigenere.java
 *
 * Author: Oleg Voevodin
 * Release date: 27th of June 2013
 *
 * ****************************************************************************
*/


package vigenere;

public class Vigenere {

    // Таблица Виженера
    static private char[][] mTable;
    public static final char FIRST_ALPHABET_SYMBOL = 'a';
    public static final char LAST_ALPHABET_SYMBOL = 'z';

    // Инициализация таблицы
    static {
        int length = LAST_ALPHABET_SYMBOL - FIRST_ALPHABET_SYMBOL + 1;
        mTable = new char[length][length];

        for (int j = 0; j < mTable[0].length; j++) {
            mTable[0][j] = (char) (FIRST_ALPHABET_SYMBOL + j);
            System.out.print(mTable[0][j]);
        } System.out.println("");

        for (int i = 1; i < mTable.length; i++) {
            for (int j = 0; j < mTable[i].length; j++) {
                mTable[i][j] = (char) (FIRST_ALPHABET_SYMBOL + ((j + i) % length));
                System.out.print(mTable[i][j]);
            } System.out.println("");
        }
    }



    // Шифровать символ
    private static char EncryptChar(char dataChar, char keyChar) {
        if (!Contains(dataChar))
            return dataChar;

        return mTable[dataChar - FIRST_ALPHABET_SYMBOL][keyChar - FIRST_ALPHABET_SYMBOL];
    }

    // Расшифровать символ
    private static char DecryptChar(char dataChar, char keyChar) {
        if (!Contains(dataChar))
            return dataChar;

        int idx = keyChar - FIRST_ALPHABET_SYMBOL;
        for (int i = 0; i < mTable.length; i++) {
            if (mTable[i][idx] == dataChar) {
                return mTable[i][0];
            }
        }

        throw new RuntimeException("Not found");
    }

    // true, если таблица содержит символ
    public static boolean Contains(char c) {
        for (int j = 0; j < mTable[0].length; j++) {
            if (mTable[0][j] == c)
                return true;
        }
        return false;
    }

    // Шифровать
    public static String Encrypt(String text, String key) {
        if (text.isEmpty())
            throw new RuntimeException("Text is empty!");

        if (key.isEmpty())
            throw new RuntimeException("Key is empty!");

        String result = "";
        for (int i = 0; i < text.length(); i++) {
            result += EncryptChar(text.charAt(i), key.charAt(i % key.length()));
        }
        return result;
    }

    // Расшифровать
    public static String Decrypt(String text, String key) {
        if (text.isEmpty())
            throw new RuntimeException("Text is empty!");

        if (key.isEmpty())
            throw new RuntimeException("Key is empty!");

        String result = "";
        for (int i = 0; i < text.length(); i++) {
            result += DecryptChar(text.charAt(i), key.charAt(i % key.length()));
        }
        return result;
    }

    public static void main(String[] args) {
        String key = "ROVENKO";
        String text = Kasiski.readFile("/Users/andrew/Dropbox/CypheringChallenge/Sevidov/Vigenere/src/resources/text.txt");
        System.out.println(text);

        String alphabet = new String("abcdefghijklmnopqrstuvwxyz");
        String encryptedAlphabet = Vigenere.Encrypt(alphabet,key.toLowerCase());
        System.out.println(alphabet);
        System.out.println(encryptedAlphabet);
        System.out.println("----------------");

        String encrypted = Vigenere.Encrypt(text.toLowerCase(), key.toLowerCase());
        System.out.println(encrypted);
        String decrypted = Vigenere.Decrypt(encrypted.toLowerCase(), key.toLowerCase());
        System.out.println(decrypted);
    }
}
