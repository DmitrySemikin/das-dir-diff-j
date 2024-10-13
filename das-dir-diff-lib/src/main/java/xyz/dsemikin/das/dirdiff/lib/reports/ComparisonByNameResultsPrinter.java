package xyz.dsemikin.das.dirdiff.lib.reports;

import xyz.dsemikin.das.dirdiff.lib.algorithms.CompareDirsFsItemsByName;

import java.nio.file.Path;

public class ComparisonByNameResultsPrinter {
    public void print(final CompareDirsFsItemsByName comparisonResults) {
        System.out.println("RESULTS OF COMPARISON BY NAME:");
        System.out.println("Comapred directories: ");
        System.out.println("Dir1: " + comparisonResults.getDir1());
        System.out.println("Dir2: " + comparisonResults.getDir2());
        if (comparisonResults.getDifferentKindsFsItemPaths().isEmpty()) {
            System.out.println("No FS Items with same paths but different FS Item Kinds found.");
        } else {
            System.out.println("FS Items with same paths, but different FS Item kinds:");
            for (final Path path : comparisonResults.getDifferentKindsFsItemPaths()) {
                System.out.println("Dir1 kind: " + comparisonResults.getDir1FsItems().get(path).getKind() + ", " +
                        "Dir2 kind: " + comparisonResults.getDir2FsItems().get(path).getKind() + " - " + path);
            }
        }
        if (comparisonResults.getDir1OnlyFsItemPaths().isEmpty()) {
            System.out.println("No FS Items found, which exist only in Dir 1.");
        } else {
            System.out.println("Following FS Items were found only in Dir 1:");
            for (final Path path : comparisonResults.getDir1OnlyFsItemPaths()) {
                System.out.println(comparisonResults.getDir1FsItems().get(path).getKind().toString() + "\t-\t" + path);
            }
        }
        if (comparisonResults.getDir2OnlyFsItemPaths().isEmpty()) {
            System.out.println("No FS Items, which only exist in Dir2 was found.");
        } else {
            System.out.println("Following FS Items present only in Dir2:");
            for (final Path path : comparisonResults.getDir2OnlyFsItemPaths()) {
                System.out.println(comparisonResults.getDir2FsItems().get(path).getKind().toString() + "\t-\t" + path);
            }
        }
    }
}
