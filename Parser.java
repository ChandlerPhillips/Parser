/*
Chandler Phillips
Donovan Cadena De La Rosa
CSC 415 - Assignment 5
 */

import java.io.File;

public class Parser {

    private Lexer scanner;
    private Token current;

    public Parser(String file) {
        try {
            scanner = new Lexer((new File(file)).toString());
            current = scanner.lex();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void match(String expected) {
        if (current.getLexeme().equals(expected)) {
            if (current.getDescription() == "comment") {
                current = scanner.lex();
            } else {
                System.out.println("Matched " + current.getLexeme());
                current = scanner.lex();
            }
            if (current == null) {
                System.out.println("Reached the end of file.");
                System.exit(0);
            }
        } else {
            System.out.println();
            System.out.println("ERROR!");
            System.out.println("Expected: " + expected);
            System.out.println("Encountered: " + current);
            System.exit(0);
        }
    }
	
	//<start> -> <stmts> (Also we check for comments and if so, we disregard)
	
    public void start() {
        System.out.println("Begin <start>");
        if (current.getDescription().equals("comment")) {
            match(current.getLexeme());
        }
        stmts();
    }

	//<stmts> -> <stmt> {<stmt>}

    public void stmts() {
        System.out.println("Begin <stmts>");
        stmt();
    }
	
	//<stmt> -> <ass-stmt> | <if-stmt> | <loop-stmt> | <text-stmt>

    public void stmt() {
        System.out.println("Begin <stmt>");
        if (current.getDescription() == "identifier") {
            switch (current.getDescription()) {
                case "identifier":
                    ass_stmt();
            }
        } else {
            switch (current.getLexeme()) {
                case "if":
                    if_stmt();
                    break;
                case "loop":
                    loop();
                    break;
                case "text":
                    text_stmt();
                    break;
                default:
                    System.out.println();
                    System.out.println("ERROR!");
                    System.out.println("Expected: =, if, loop, or text");
                    System.out.println("Encountered: " + current);
                    System.exit(0);
            }
        }

    }
	
	//<ass-stmt> -> %id% = <expr>

    public void ass_stmt() {
        System.out.println("Begin <ass-stmt>");
        match(current.getLexeme());
        match("=");
        expr();
    }
	
	//<if-stmt> -> if : <bool-expr> ( <stmts> )

    public void if_stmt() {
        System.out.println("Begin <if-stmt>");
        match("if");
        match(":");
        bool_expr();
        match("(");
        stmts();
        match(")");
    }
	
	//<loop-stmt> -> loop : <bool-expr> ( <stmts> )

    public void loop() {
        System.out.println("Being <loop-stmt>");
        match("loop");
        match(":");
        bool_expr();
        match("(");
        stmts();
        match(")");
    }
	
	//<text-stmt> -> text : <expr>

    void text_stmt() {
        System.out.println("Begin <text-stmt>");
        match("text");
        match(":");
        expr();
    }
	
	//<expr> -> <value> {+<value>}

    public void expr() {
        System.out.println("Begin <expr>");
        value();
        while (current.getLexeme().equals("+")) {
            match("+");
            value();
        }
    }
	
	//<bool-expr> -> <value> <comparison-op> <value>

    public void bool_expr() {
        System.out.println("Begin <bool-expr>");
        value();
        compareOp();
        value();
    }
	
	//<value> ->  %id% | %num% | %string% | - %num% | - %id% | - %string%

    public void value() {
        System.out.println("Begin <value>");

        if (current.getLexeme().equals("-")) {
            match(current.getLexeme());
            match(current.getLexeme());
        } else {
            match(current.getLexeme());
        }
    }
	
	//<comparison-op> -> ? | ! | < | >

    public void compareOp() {
        System.out.println("Begin <comparison-op>");
        switch (current.getLexeme()) {
            case "?":
                match("?");
                break;
            case "!":
                match("!");
                break;
            case "<":
                match("<");
                break;
            case ">":
                match(">");
                break;
            default:
                System.out.println();
                System.out.println("ERROR!");
                System.out.println("Expected: ?, !, >, or >");
                System.out.println("Encountered: " + current);
                System.exit(0);
        }
    }

    public static void main(String[] args) {
        Parser p = new Parser(args[0]);
        p.start();
        while (p.current != null) {
            p.start();
        }
    }
}

