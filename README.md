# das-dir-diff-j
Simple diff tool for comparing directories written in Java.

**There is nothing to use in this project yet!**

TODO:
* Compare directory files including contents (md5)
  * Done: Create implementation, which goes through the directory files, calculates their MD5 sums and
    stores them into text file.
  * Done: Create sub-command of the application, which uses the implementation from above and create those files.
  * Create unit tests for the algorithm, which writes hashes.
  * Create implementation, which would read the text files with relative paths and MD5 sums and compare
    them with the actual file system.
  * Create sub-command, which would use it.
  * Create unit tests for algorithms, which checks hashes.
* Single dir analysis:
  * Add some profiling code. Measure, what takes time - listing of the files, or comparison etc. Experiment with it.
  * For the files get file size
  * For the files calculate some check-sum/hash.
  * For large files estimate access speed (how to do it intelligently)?
  * Report following things (create object to query this information, then also create the reports and corresponding command line options):
    * Name of dir being examined
    * Number of files, dirs, links, other objects
    * Total size of files, max file size
    * List all elements
      * Sort by type, by name, by size
    * Calculate dir sizes
    * Show largest dirs
    * Show number of files inside dirs (direct children/nested)
    * Sort by number of files
  * Think about how to print the report about content of the directory.
  * Advanced: Improve the results data structure, so that one can group files
    inside subdirectories, i.e. that one can take a subdirectory and list
    it's children
  * Advanced: implement it using "walk" and "walkTree" and see if any of the two
    have any benefits.
* Dump information about some root dir as a file. Later use this file as a "side" for comparison.
* write function to calculate the diff.
  * Improve the output of comparison:
    * Sort the output
    * If directory is missing, don't print the whole content, but onsly some status (like how many files are missing).
  * Add progress and some simple profiling print-outs - to see, what takes long time.
  * Write test-cases and unit tests for this method
  * Think, how to use it in for real thing.
* Think how to make the API nice to be usable in interactive Java or Groovy session.
