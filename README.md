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

### Project Filtering

Filters out projects that contain a sequence with the name specified by --sequenceName. Default filter name is 'Live'.

Command: filter Options: -i <arg> -o <arg> --sequenceName <arg>

### Project BPM Listing

Creates a text file 'Project_BPM.txt' in the specified input directory path. The file contains a list of all projects
and their BPM, ordered by BPM.

Command: bpm Options: -i <arg>

### QLink Mode

Changes the QLink mode of all projects to the value specified by --qlinkMode. The default is 'Project'.

Command: qlinkMode Options: -i <arg> -o <arg> --qlinkMode <arg>

### QLinks Project Configuration

Configures custom QLink assignments for all projects. QLink assignments in vertical rows from top:

* Row 1: Not mapped, reserved for individual mappings
* Row 2: Volume track 1-4, program 1-4 if --mapPrograms is set
* Row 3: mute track 1-4, program 1-4 if --mapPrograms is set
* Row 4: Master EQ high and low gain momentary, master EQ low gain, master volume

Command: qlinkMap Options: -i <arg> -o <arg> --mapPrograms

### Liveset Preparation

Filters out all projects with a 'Live' seqeuence, reorders the sequences of those, configures the QLinks and creates a
project BPM list text file. Command: liveset Options: --uniqueSequences --sequenceName --qlinkMode

Command: liveset Options: -i <arg> -o <arg> --sequenceName <arg> --qlinkMode <arg> --songNumber <arg> --mapPrograms
--uniqueSequences

## Usage--qlinkMode

    java -jar <mpc-utils-jar> <options>
        -c,--command <arg>           reorder|filter|bpm|qlinkMode|qlinkMap|liveset
        -h,--help                    show help
        -i,--inputDirectory <arg>    input directory path
        -o,--outputDirectory <arg>   output directory path
           --qlinkMode <arg>         Project|Program|PadScene|PadParam|Screen
           --mapPrograms             map programs instead of tracks to QLinks
           --sequenceName <arg>      optional sequenceName for filtering
           --songNumber <arg>        optional songNumber for reordering
           --uniqueSequences         keep the sequences unique when reordering