# mpc-utils for AKAI MPC One/X/Live/Live2

## Non-Invasive

mpc-utils will not alter the original projects but create a new project version in the specified target directory.

### Sequence Reordering

Reorders the sequences according to a song. Default song is '1'. Sequences that are not used in the specified song will
be appended after the sequences used in the song.

### Project Filtering

Filters out projects that contain a sequence with the specified name. Default filter is 'Live'.

### Project BPM Listing

Creates a text file with the BPM of all filtered projects.

### QLinks Project Configuration

Activates QLink mode "Project" and custom QLink assignements for all projects. QLink assignements 2nd row: volume track
1-4, 3rd row: mute track 1-4

### Liveset Preparation

Filters out all projects with a 'Live' seqeuence, reorders the sequences of those, configures the QLinks and create a
project BPM List.

## Setup

- A Java Runtime Environment (JRE) 11 or greater is installed
- Download the latest binary jar from https://github.com/andybaer/mpc-utils/releases

## Usage

    usage: java -jar <mpc-utils-jar>
    -c,--command <arg>           reorder|filter|bpm|qlinks|liveset
    -h,--help                    show help
    -i,--inputDirectory <arg>    input directory path
    -o,--outputDirectory <arg>   output directory path
    --sequenceName <arg>         optional sequenceName for filtering
    --songNumber <arg>           optional songNumber for reordering
    --uniqueSequences            keep the sequences unique when reordering