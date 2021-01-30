# Colour Adjacencies
Reads an image and outputs the colour adjacencies.

This is a Java implementation with a command line interface.

Primary objective is to refresh my Java, and also to have a benchmark
implementation of CoulAdj.

This project declares conformity to [SemVer 2.0.0](https://semver.org/spec/v2.0.0.html).
There is currently no API version number, because there has been no public release.

**This project is still in development and is not ready for public consumption.**

"Colour" and "color" may be used interchangeably and arbitrarily in both the code
and documentation.


# About this Github repository

*   I made this Github repository public so I could share with my twitter friends.
*   It may be made private in the future.


# Known or Assumed Requirements

* Java
    * Development is made with OpenJDK 14
* Make
* Linux to run the test scripts and use the makefile (sorry 😬)



# How to Compile & Test

To compile and launch tests:
```
$ make
```

To only compile:
```
$ make only
```

To only test:
```
$ make test
```

To only test correctness:
```
$ make test-corr
```

To remove build artifacts:
```
$ make clean
```

To remove build artifacts and then compile and launch tests:
```
$ make fresh
```





# How to Use
*   This program is made to be called on the command line.
*   Can be called with 2 or 3 arguments.
*   If 2 arguments:
    - **1st** must be filepath to **image** file.
    - **2nd** must be filepath to **results** TSV file.
    ```bash
    $ java -jar ./CoulAdj.jar "path/to/image.png" "path/to/results.tsv"
    ```
*   The 3 arguments form is to use the `dont-relate-diagonals` option:
    - **1st** must be `--dont-relate-diagonals`.
    - **2nd** must be filepath to **image** file.
    - **3rd** must be filepath to **results** TSV file.
    ```bash
    $ java -jar ./CoulAdj.jar --dont-relate-diagonals "path/to/image.png" "path/to/results.tsv"
    ```




# API

## Input 
*   Source image file path
*   Results TSV file path
*   Option(s)
    * `dont-relate-diagonals`
        * By default, all 8 neighbours are considered adjacent.
        * If this option is present, CoulAdj will only consider as adjacent the 
        four (4) neighbours with a common edge. 
        (top, bottom, left, and right neighbours)

## Known limitations
*   (none yet, or maybe I forgot to update this Readme...)


## Output
*   TSV File

### TSV File
*   Tab-Separated Values
    *   [Summary on Wikipedia](https://en.wikipedia.org/wiki/Tab-separated_values) 
    *   [Official specifications](https://www.iana.org/assignments/media-types/text/tab-separated-values)

*   Data will be organized in this fashion:

    |r  |g  |b  |a  |adj_r|adj_g|adj_b|adj_a|
    |---|---|---|---|-----|-----|-----|-----|
    |0  |32 |64 |128|0    |0    |0    |255  |
    |0  |32 |64 |255|0    |0    |0    |255  |
    |0  |32 |64 |255|0    |32   |0    |255  |
    |0  |64 |0  |255|0    |0    |0    |255  |

*   The alpha column will always be included in the output. Images without an alpha channel
will get an alpha value at full opacity.

    |r  |g  |b  |a  |adj_r|adj_g|adj_b|adj_a|
    |---|---|---|---|-----|-----|-----|-----|
    |0  |32 |64 |255|0    |0    |0    |255  |
    |0  |32 |64 |255|0    |32   |0    |255  |
    |0  |64 |0  |255|0    |0    |0    |255  |


*   The rows will be sorted in ascending order.

    |r  |g  |b  |a  |adj_r|adj_g|adj_b|adj_a|
    |---|---|---|---|-----|-----|-----|-----|
    |0  |32 |64 |255|0    |0    |0    |255  |
    |0  |32 |64 |255|0    |32   |0    |255  |
    |0  |32 |64 |255|0    |128  |0    |255  |
    |0  |64 |0  |255|0    |0    |0    |255  |
    |32 |0  |0  |255|0    |0    |0    |255  |
    |255|0  |0  |255|0    |0    |0    |255  |

*   Symmetric relations will be included;
if A is adjacent to B, then B is adjacent to A, 
so this single relation will generate two rows.

    |r  |g  |b  |a  |adj_r|adj_g|adj_b|adj_a|
    |---|---|---|---|-----|-----|-----|-----|
    |0  |0  |0  |255|0    |64   |0    |255  |
    |0  |64 |0  |255|0    |0    |0    |255  |

*   Reflexive relations will *not* be included;
a color cannot be adjacent with itself.

*   Colors that differ only in their alpha value are considered distinct.

    |r  |g  |b  |a  |adj_r|adj_g|adj_b|adj_a|
    |---|---|---|---|-----|-----|-----|-----|
    |0  |0  |0  |128|0    |0    |0    |255  |
    |0  |0  |0  |255|0    |0    |0    |128  |

*   Columns will appear in this order:
    - Red
    - Green
    - Blue
    - Alpha
    - Adjacent Red
    - Adjacent Green
    - Adjacent Blue
    - Adjacent Alpha

*   The first row will contain the column names.
*   The column names will be:

    |Column Name|Color Channel  |
    |-----------|---------------|
    | `r`       |Red            |
    | `g`       |Green          |
    | `b`       |Blue           |
    | `a`       |Alpha          |
    | `adj_r`   |Adjacent Red   |
    | `adj_g`   |Adjacent Green |
    | `adj_b`   |Adjacent Blue  |
    | `adj_a`   |Adjacent Alpha |

*   The line-endings may be either in Windows (CRLF) or Unix (LF) style.
