# MPC Utils for AKAI MPC One/X/Live/Live2

## Firmware 2.9.1 Tested

## Copy On Write

mpc-utils will not alter the original projects but create a new project version in the specified target directory.

## Setup

- A Java Runtime Environment (JRE) 11 or greater is installed
- Download the latest binary jar from https://github.com/andybaer/mpc-utils/releases

## Functions

### Sequence Reordering

Reorders the sequences according to a song. Default song is '1'. Sequences that are used in multiple places of the song
will be replicated unless the --uniqueSequences flag is set. Sequences that are not used in the specified song will be
appended after the sequences used in the song.

Command: reorder Options: -i <arg> -o <arg> --songNumber <arg> --uniqueSequences

### Project BPM Listing

Creates a text file 'Project_BPM.txt' in the specified input directory path. The file contains a list of all projects
and their BPM, ordered by BPM.

Command: bpm Options: -i <arg>

### Liveset Preparation

Filters out all projects with a 'Live' or 'Liveset[type]' seqeuence, reorders the sequences of those, configures the QLinks and creates a
project BPM list text file. Command: liveset Options: --uniqueSequences --sequenceName --qlinkMode

Command: liveset Options: -i <arg> -o <arg> --sequenceName <arg> --qlinkMode <arg> --songNumber <arg> --mapPrograms
--uniqueSequences

## Usage--qlinkMode

    usage: java -jar <mpc-utils-jar> <options>
        -c,--command <arg>           reorder|bpm|liveset
        -h,--help <arg>              show help
        -i,--inputDirectory <arg>    input directory path
        -o,--outputDirectory <arg>   output directory path
        --songNumber <arg>        optional songNumber for reordering
        --uniqueSequences         keep the sequences unique when reordering