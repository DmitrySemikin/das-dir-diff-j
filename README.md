# das-dir-diff-j
Simple diff tool for comparing directories written in Java.

**There is nothing to use in this project yet!**

We already have:
* `collect-fs-items-names`        - Inspect content of the dir and print result.
* `compare-dirs-fs-items-by-name` - Compare content of two directories by comparing names.
* `collect-fs-items-md5`          - Calculate MD5 for each file in the dir and save result to file.

TODO:
* Next steps:
  * `collect-fs-items-md5` - Support "exclude directories" (currently accepts args, but not use it)
  * `collect-fs-items-md5` - Handle exception "access denied" - add option to skip dir.
    * `collect-fs-items-md5` - Collect information on skipped dirs and include it into the report.
  * new-command: Compare two files with the lists.
* Compare directory files including contents (md5)
  * Done: Create implementation, which goes through the directory files, calculates their MD5 sums and
    stores them into text file.
  * Done: Create sub-command of the application, which uses the implementation from above and create those files.
  * Create unit tests for the algorithm, which writes hashes.
  * Create implementation, which would read the text files with relative paths and MD5 sums and compare
    them with the actual file system.
  * Create sub-command, which would use it.
  * Create unit tests for algorithms, which checks hashes.
  * Create command "check integrity", which would compare given directory with given file of md5 sums.
  * Create multi-compare comand, where one directory is considered to be master and other multiple directories are compared to it and the results are reported.
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

Target volume: 3M-5M fs items.
Memory (very roughly: 0.5K*5M = 2.5G - should be OK. Adjust Java options?)


## High level vision

In fact, this vision is not for the "dir diff" library/tool, but rather for the solution,
which I intend to build later. But it will hopefully provide some insights about what
can be useful and/or needed.


### Background

It all revolves around some redundant backup copies, which are managed simply as file copies
(no storing in DP or other magic).

For now also there is no need in versioning or something like this.

What we want is to be able to check simple things like:

* Ensure, that we copied everything. Each file, and content was not corrupted during copying.
* From time to time ensure, that the content of the files was not corrupted (e.g. because
  of HDD failure/degeneration).
* Be able to check, if one directory is a redundant copy of another directory (actually
  basically it is the same as the first bullet).
* Setup some system of configs and scheduled jobs, which would regularly (or on demand)
  check integrity and consistency of the "master" copy and several slave copies. It should
  be able to deal with the situation, that some copies may be temporarily unavailable.


### 1. Integrity check

Use case: we have an archive directory, which is stored in some HDD. We want to make sure,
that the HDD did not go bad and that the content of the files is not corrupted.

For this purpose we want to create a list of the files and their MD5 sums.

And we want to have a function, which reads this list and the actual files, compares them
and if the difference is found, than this difference is reported.


#### 1.1. Integrity check - what we need

1. We need a command to create a list of files and MD5 sums
2. We need a command to read a list of files and MD5 sums, read actual files and compare
   them (i.e. actually do the check).


#### 1.2. Integrity check - what we already have

1. `collect-fs-items-md5` - this one should be enough to create the list.
   1. We don't have explicit config files, but we can use @files with input arguments as
      current solution.
   2. We already support exclusions - todo: check.


#### 1.3 Integrity check - to be implemented

* `collect-fs-items-md5` - do we need more options? Or additional features/functions?
* We need a command to compare the values from the list with the actual files.
* We probably need to improve the reporting.


### 2. TBD

TBD

