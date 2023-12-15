# das-dir-diff-j
Simple diff tool for comparing directories written in Java.

**There is nothing to use in this project yet!**

TODO:
* write function, which collects information about all files and directories
  inside starting directory.
  * Create some structure to store the results
  * Differentiate between files, directories and "other objects" (maybe handle
    links explicitly also)
  * Identify entries with the Path relative to the starting directory.
  * Advanced: Improve the results data structure, so that one can group files
    inside subdirectories, i.e. that one can take a subdirectory and list
    it's children
  * Advanced: implement it using "walk" and "walkTree" and see if any of the two
    have any benefits.
* write function to calculate the diff.
  * First the comparison should be just by name.
  * think, how to store the results...
