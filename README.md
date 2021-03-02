# mpc-utils

## Description

Reorders the sequences of AKAI MPC One/X/Live/Live2 projects according to the sequence of sequences in a song.

Sequences that are not used in the specified song will be appended after the song sequences.

mpc-utils will not alter the original projects but create a new project version in the specified target directory.

## Setup

- A Java Runtime Environment (JRE) 11 or greater is installed
- Download the latest binary jar from https://github.com/andybaer/mpc-utils/releases

## Usage

    java -jar <mpc-utils-jar> <command> <scanDirectory> <targetDirectory> [songNumber]

    <mpc-utils-jar> The binary of this software
    <command> [reorder|livesets|reorderAndLiveSet]
    <scanDirectory> The directory containing the MPC projects to reorder
    <targetDirectory> The directory to save the reordered projects
    [songNumber] Optional number of the song to use - default 1