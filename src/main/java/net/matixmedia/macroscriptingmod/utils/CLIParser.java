package net.matixmedia.macroscriptingmod.utils;

import java.util.ArrayList;
import java.util.Stack;

public class CLIParser {
    private final String cmdString;

    public CLIParser(String cmdString) {
        this.cmdString = cmdString;
    }

    public ArrayList<String> getTokens() throws Exception {
        ArrayList<String> finalTokens = new ArrayList<>();
        ArrayList<StringBuffer> tokens = new ArrayList<>();
        char[] inArray = this.cmdString.toCharArray();
        StringBuffer token = new StringBuffer();
        int valid = checkIfTheStringIsValid(inArray);
        if (valid == -1) {
            for (int i = 0; i <= inArray.length; i++) {

                if (i != inArray.length) {
                    if ((inArray[i] != ' ') && (inArray[i] != '"')) {
                        token.append(inArray[i]);
                    }

                    if ((inArray[i] == '"') && (inArray[i - 1] != '\\')) {
                        i = i + 1;
                        while (checkIfLastQuote(inArray, i)) {
                            token.append(inArray[i]);
                            i++;
                        }
                    }
                }
                if (i == inArray.length) {
                    tokens.add(token);
                    token = new StringBuffer();
                } else if (inArray[i] == ' ' && inArray[i] != '"') {
                    tokens.add(token);
                    token = new StringBuffer();
                }
            }
        } else {
            throw new Exception(
                    "Invalid command. Couldn't identify sequence at position "
                            + valid);
        }
        for(StringBuffer tok:tokens){
            finalTokens.add(tok.toString());
        }
        return finalTokens;
    }

    private static int checkIfTheStringIsValid(char[] inArray) {
        Stack<Character> myStack = new Stack<>();
        int pos = 0;
        for (int i = 0; i < inArray.length; i++) {
            if (inArray[i] == '"' && inArray[i - 1] != '\\') {
                pos = i;
                if (myStack.isEmpty())
                    myStack.push(inArray[i]);
                else
                    myStack.pop();
            }
        }
        if (myStack.isEmpty())
            return -1;
        else
            return pos;
    }

    private static boolean checkIfLastQuote(char[] inArray, int i) {
        if (inArray[i] == '"') {
            return inArray[i - 1] == '\\';
        } else
            return true;
    }
}
