# mpc-utils for AKAI MPC One/X/Live/Live2

## Copy On Write

mpc-utils will not alter the original projects but create a new project version in the specified target directory.

### Sequence Reordering

Reorders the sequences according to a song. Default song is '1'. Sequences that are used in multiple places of the song
will be replicated unless the --uniqueSequences flag is set. Sequences that are not used in the specified song will be
appended after the sequences used in the song.

Command: reorder Options: --uniqueSequences

### Project Filtering

Filters out projects that contain a sequence with the name specified by --sequenceName. Default filter name is 'Live'.

Command: filter Options: --sequenceName

### Project BPM Listing

Creates a text file 'Project_BPM.txt' in the specified input directory path. The file contains a list of all projects
and their BPM, ordered by BPM.

Command: bpm Options: <none>

### QLink Mode

Changes the QLink mode of all projects to the value specified by --qlinkMode. The default is 'Project'.

Command: qlinkMode Options: --qlinkMode

### QLinks Project Configuration (qlinks)

Configures custom QLink assignements for all projects. QLink assignements:

* Row 1: Unchanged, reserved for custom map
* Row 2: Volume track 1-4
* Row 3: mute track 1-4
* Row 4: Master EQ high and low gain, master volume

Command: qlinkMap Options: <none>

### Liveset Preparation

Filters out all projects with a 'Live' seqeuence, reorders the sequences of those, configures the QLinks and creates a
project BPM list text file. Command: liveset Options: --uniqueSequences --sequenceName --qlinkMode

Command: liveset Options: --uniqueSequences --sequenceName --qlinkMode

## Setup

- A Java Runtime Environment (JRE) 11 or greater is installed
- Download the latest binary jar from https://github.com/andybaer/mpc-utils/releases

## Usage--qlinkMode

    java -jar <mpc-utils-jar> <options>
        -c,--command <arg>           reorder|filter|bpm|qlinkMode|qlinkMap|liveset
        -h,--help                    show help
        -i,--inputDirectory <arg>    input directory path
        -o,--outputDirectory <arg>   output directory path
           --qlinkMode <arg>         Project|Program|Pad|Screen
           --sequenceName <arg>      optional sequenceName for filtering
           --songNumber <arg>        optional songNumber for reordering
           --uniqueSequences         keep the sequences unique when reordering