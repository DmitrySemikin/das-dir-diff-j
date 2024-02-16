# das-dir-diff-j
Simple diff tool for comparing directories written in Java.

**There is nothing to use in this project yet!**

TODO:
* IN WORK: write function, which collects information about all files and directories
  inside starting directory.
  * DONE: Create some structure to store the results
  * DONE: Differentiate between files, directories and "other objects" (maybe handle
    links explicitly also)
  * DONE: Identify entries with the Path relative to the starting directory.
  * DONE: Create unit test for the function
  * DONE: Use the function in "MainApp", so that one can use it interactively to examine
    content of some dir.
  * DONE: Think about how to handle the command line parameters of the App
  * Think about how to print the report about content of the directory.
  * Advanced: Improve the results data structure, so that one can group files
    inside subdirectories, i.e. that one can take a subdirectory and list
    it's children
  * Advanced: implement it using "walk" and "walkTree" and see if any of the two
    have any benefits.
* write function to calculate the diff.
  * DONE: First the comparison should be just by name.
  * DONE: think, how to store the results...
  * Improve the output of comparison:
    * Sort the output
    * If directory is missing, don't print the whole content, but onsly some status (like how many files are missing).
  * Add progress and some simple profiling print-outs - to see, what takes long time.
  * Write test-cases and unit tests for this method
  * Think, how to use it in for real thing.
