package xyz.dsemikin.das.dirdiff.app;

import xyz.dsemikin.das.dirdiff.app.args.ArgsParser;
import xyz.dsemikin.das.dirdiff.lib.TempClass;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        TempClass.sayHello();

        final ArgsParser argsParser = new ArgsParser();
        argsParser.parseArgs(args);
    }
}
