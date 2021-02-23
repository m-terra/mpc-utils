# mpc-utils

Reorder the sequences of AKAI MPC One/X/Live/Live2 projects according to the sequences in a song. Sequences that are not
used in the specified song will be appended after the song sequences.

## System requirements

- Up to date java runtime environment is installed
- The mpc-utils-jar is present on the file system

## Usage

    java -jar <mpc-utils-jar> <scanDirectory> <targetDirectory> [songNumber]

    <mpc-utils-jar> The binary of this software
    <scanDirectory> The directory containing the MPC projects to reorder
    <targetDirectory> The directory to save the reordered projects
    [songNumber] Optional number of the song to use - default 1